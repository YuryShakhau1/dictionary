package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.logic.util.FileHelper;
import by.shakhau.dictionary.logic.util.StringHelper;
import by.shakhau.dictionary.persistence.domain.ExistedWordEntity;
import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import by.shakhau.dictionary.persistence.domain.UserWordEntity;
import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.persistence.repository.LanguageRepository;
import by.shakhau.dictionary.persistence.repository.TextFileWordRepository;
import by.shakhau.dictionary.persistence.repository.TranslateRepository;
import by.shakhau.dictionary.persistence.repository.UserWordRepository;
import by.shakhau.dictionary.persistence.repository.UserWordStatusRepository;
import by.shakhau.dictionary.persistence.repository.WordRepository;
import by.shakhau.dictionary.service.ExistedWordService;
import by.shakhau.dictionary.service.WordService;
import by.shakhau.dictionary.service.bean.KeyValue;
import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/normalizer")
@AllArgsConstructor
public class Normalizer {

	private WordRepository wordRepository;
	private UserWordStatusRepository userWordStatusRepository;
	private WordService wordService;
	private UserWordRepository userWordRepository;
	private TextFileWordRepository textFileWordRepository;
	private TranslateRepository translateRepository;
	private LanguageRepository languageRepository;
	private ExistedWordService existedWordService;
	
	@RequestMapping(value = "/normalize", method = RequestMethod.GET)
	public ResponseEntity<String> normalize() {
		removeTrash();
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ResponseEntity<String> uploadWords(ServletRequest request) {
		String realPath = request.getServletContext().getRealPath("/").replace("\\", "/");
		String folderPath = realPath + "WEB-INF/classes/english";
		List<String> files = FileHelper.folderFiles(folderPath);
		Set<String> wordValues = new HashSet<>();
		files.forEach(file -> {
			String content = FileHelper.fileContent(file, "UTF-8");
			List<String> buff = findWords(content);
			wordValues.addAll(buff);
		});

		LanguageEntity language = languageRepository.findOneByName(LanguageEntity.EN);
		List<WordEntity> words = wordValues.stream()
				.filter(wordValue -> LanguageEntity.EN.equals(StringHelper.recognizeLanguage(wordValue)))
				.map(wordValue -> new WordEntity(wordValue, language))
				.collect(Collectors.toList());

		words.forEach(word -> {
			wordService.add(word);
		});
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void removeTrash() {
		Map<KeyValue<String, Long>, List<TextFileWordEntity>> textFileWords = textFileWordRepository.findAll().stream()
				.filter(fw -> fw.getWord() != null)
				.collect(Collectors.groupingBy(fw -> new KeyValue<>(fw.getWord().getValue(), fw.getWord().getLanguage().getId())));
		List<ExistedWordEntity> allExistedWords = existedWordService.findAll();
		Map<KeyValue<String, Long>, ExistedWordEntity> existedWords = allExistedWords.stream()
				.collect(Collectors.toMap(ew -> new KeyValue<>(ew.getValue(), ew.getLanguageId()), ew -> ew));
		List<WordEntity> words = wordRepository.findAll();

		Map<KeyValue<String, Long>, List<WordEntity>> wordMap = words.stream()
				.collect(Collectors.groupingBy(w -> new KeyValue<>(w.getValue(), w.getLanguage().getId())));

		wordMap = wordMap.entrySet().stream()
				.filter(es -> es.getValue().size() > 1)
				.collect(Collectors.toMap(es -> es.getKey(), es -> es.getValue()));

		List<WordEntity> wordsToDelete = new ArrayList<>();
		List<UserWordEntity> userWordsToDelete = new ArrayList<>();
		List<WordTranslateEntity> wordTranslatesToDelete = new ArrayList<>();
		List<TextFileWordEntity> textFileWordsToUpdate = new ArrayList<>();
		for (List<WordEntity> ws : wordMap.values()) {
			if (ws.size() == 1) {
				continue;
			}

			int saveIndex = -1;
			for (int i = 0 ; i < ws.size(); i++) {
				WordEntity w = ws.get(i);
				List<WordTranslateEntity> sTranslates = translateRepository.findBySourceWordId(w.getId());
				List<WordTranslateEntity> tTranslates = translateRepository.findByTranslateWordId(w.getId());
				if (!sTranslates.isEmpty() || !tTranslates.isEmpty()) {
					saveIndex = i;
					break;
				}
				i++;
			}
			for (int i = 0 ; i < ws.size(); i++) {
				if (saveIndex == i) {
					continue;
				}
				WordEntity w = ws.get(i);
				List<TextFileWordEntity> tfWords = textFileWords.computeIfAbsent(
						new KeyValue<>(w.getValue(), w.getLanguage().getId()), k -> new ArrayList<>());
				tfWords.forEach(tfW -> tfW.setWord(null));
				textFileWordsToUpdate.addAll(tfWords);
				List<WordTranslateEntity> sTranslates = translateRepository.findBySourceWordId(w.getId());
				List<WordTranslateEntity> tTranslates = translateRepository.findByTranslateWordId(w.getId());
				wordTranslatesToDelete.addAll(sTranslates);
				wordTranslatesToDelete.addAll(tTranslates);
				userWordsToDelete.addAll(userWordRepository.findByWordId(w.getId()));
				wordsToDelete.add(w);
			}
			if (wordsToDelete.size() >= 1000) {
				textFileWordRepository.saveAll(textFileWordsToUpdate);
				translateRepository.deleteAll(wordTranslatesToDelete);
				userWordRepository.deleteAll(userWordsToDelete);
				wordRepository.deleteAll(wordsToDelete);
				wordTranslatesToDelete = new ArrayList<>();
				userWordsToDelete = new ArrayList<>();
				wordsToDelete = new ArrayList<>();
			}
		}
		textFileWordRepository.saveAll(textFileWordsToUpdate);
		translateRepository.deleteAll(wordTranslatesToDelete);
		userWordRepository.deleteAll(userWordsToDelete);
		wordRepository.deleteAll(wordsToDelete);

		List<WordEntity> wordsToUpdate = new ArrayList<>();
		wordsToDelete = new ArrayList<>();
		userWordsToDelete = new ArrayList<>();
		wordTranslatesToDelete = new ArrayList<>();
		words = wordRepository.findAll();
		for (WordEntity word : words) {
			if (!LanguageEntity.EN.equals(word.getLanguage().getName())) {
				continue;
			}

			List<UserWordEntity> userWords = userWordRepository.findByWordId(word.getId());
			ExistedWordEntity existedWordFound = existedWords.get(new KeyValue<>(word.getValue(), word.getLanguage().getId()));
			if (existedWordFound == null) {
				System.out.println(word.getValue());
				List<TextFileWordEntity> tfWords = textFileWords.computeIfAbsent(
						new KeyValue<>(word.getValue(), word.getLanguage().getId()), k -> new ArrayList<>());
				tfWords.forEach(tfW -> tfW.setWord(null));
				textFileWordsToUpdate.addAll(tfWords);
				wordTranslatesToDelete.addAll(translateRepository.findBySourceWordId(word.getId()));
				wordTranslatesToDelete.addAll(translateRepository.findByTranslateWordId(word.getId()));
				userWordsToDelete.addAll(userWords);
				wordsToDelete.add(word);
				if (wordsToDelete.size() >= 1000) {
					textFileWordRepository.saveAll(textFileWordsToUpdate);
					translateRepository.deleteAll(wordTranslatesToDelete);
					userWordRepository.deleteAll(userWordsToDelete);
					wordRepository.deleteAll(wordsToDelete);
					wordRepository.saveAll(wordsToUpdate);
					wordTranslatesToDelete = new ArrayList<>();
					userWordsToDelete = new ArrayList<>();
					wordsToDelete = new ArrayList<>();
					wordsToUpdate = new ArrayList<>();
				}
			} else if (!existedWordFound.getTranscription().isEmpty()) {
				word.setTranscription(existedWordFound.getTranscription());
				wordsToUpdate.add(word);
			}
		}
		textFileWordRepository.saveAll(textFileWordsToUpdate);
		translateRepository.deleteAll(wordTranslatesToDelete);
		userWordRepository.deleteAll(userWordsToDelete);
		wordRepository.deleteAll(wordsToDelete);
		wordRepository.saveAll(wordsToUpdate);
	}

	private List<String> findWords(String content) {
		String[] fileLines = content.split("\n");
		List<String> words = new ArrayList<>(fileLines.length);
		for (String fileLine : fileLines) {
			String word = firstWord(fileLine.trim());
			
			if (word.isEmpty()) {
				continue;
			}
			
			words.add(word);
		}
		return words;
	}
	
	private String firstWord(String str) {
		int index = str.indexOf(' ');
		if (index < 0) {
			return "";
		}
		return str.substring(0, index).trim().toLowerCase();
	}
	
	private void wordsToLowerCase(List<WordEntity> words) {
		List<WordEntity> wordChanged = new ArrayList<>();
		words.forEach(word -> {
			String valueBefore = word.getValue();
			String valueAfter = valueBefore.toLowerCase();
			if (!valueBefore.equals(valueAfter)) {
				word.setValue(valueAfter);
				wordChanged.add(word);
			}
		});
		wordRepository.saveAll(wordChanged);
	}

	private void deleteDuplicatedWords(List<WordEntity> words) {
		Map<WordEntity, WordEntity> wordMap = new HashMap<>();
		words.forEach(word -> {
			WordEntity wordFound = wordMap.get(word);
			if (wordFound == null) {
				wordMap.put(word, word);
			} else {
				Long wordId = word.getId();
				List<UserWordEntity> userWords = userWordRepository.findByWordId(wordId);
				List<TextFileWordEntity> textFileWords = textFileWordRepository.findByWordId(wordId);
				userWords.forEach(userWord -> {
					userWord.setWord(wordFound);
					userWordRepository.save(userWord);
				});
				textFileWords.forEach(textFileWord -> {
					textFileWord.setWord(wordFound);
					textFileWordRepository.save(textFileWord);
				});
				wordRepository.delete(word);
			}
		});
	}

	private void deleteDuplicatedTranslates() {
		List<WordTranslateEntity> translates = translateRepository.findAll();
		EntityCleaning.clearLazyFieldsTranslate(translates);
		Set<WordTranslateEntity> translateSet = new HashSet<>();
		List<WordTranslateEntity> translatesToDelete = new ArrayList<>();
		translates.forEach(translate -> {
			if (!translateSet.contains(translate)) {
				translateSet.add(translate);
			} else {
				translatesToDelete.add(translate);
			}
		});
		translateRepository.deleteAll(translatesToDelete);
	}
	
	private void changeStatusForKnown() {
		List<UserWordEntity> userWords = userWordRepository.findAll().stream()
				.filter(userWord -> userWord.getStatus().getStatus() == UserWordStatusEntity.ALREADY_KNOWN)
				.collect(Collectors.toList());
		UserWordStatusEntity knowStatus = userWordStatusRepository.findOneByStatus(UserWordStatusEntity.KNOW);
		List<UserWordEntity> userWordsToSave = new ArrayList<>(1000);
		userWords.forEach(userWord -> {
			WordEntity word = userWord.getWord();
			WordEntity wordWithTranslate = wordService.findWithTranslate(word.getId());
			if (wordWithTranslate.getTranslates().isEmpty()) {
				userWord.setStatus(knowStatus);
				userWordsToSave.add(userWord);
			}
		});
		userWordRepository.saveAll(userWordsToSave);
	}
}
