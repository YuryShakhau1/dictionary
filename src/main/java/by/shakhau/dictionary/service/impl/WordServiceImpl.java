package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.logic.util.EntityTransformer;
import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.persistence.repository.WordRepository;
import by.shakhau.dictionary.service.*;
import by.shakhau.dictionary.service.bean.Word;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WordServiceImpl implements WordService {

	private WordRepository wordRepository;
	private TranslateService translateService;
	private LanguageService languageService;

    @Override
    public List<WordEntity> findAll() {
        return wordRepository.findAll();
    }

    @Override
	public WordEntity add(String word, LanguageEntity language) {
		return add(new WordEntity(word, language));
	}

	@Override
	public WordEntity add(WordEntity word) {
		LanguageEntity language = word.getLanguage();
		Long languageId = language.getId();
		if (languageId == null) {
			language = languageService.findLanguage(language.getName());
			word.setLanguage(language);
		}

		WordEntity dbWord = wordRepository.findOneByValueAndLanguageId(word.getValue(), word.getLanguage().getId());

		if (dbWord != null) {
			return dbWord;
		}

		wordRepository.save(word);
		return word;
	}

	@Override
	public WordEntity save(WordEntity word) {
		LanguageEntity wordLanguage = word.getLanguage();
		Long wordLanguageId = wordLanguage.getId();

		if (wordLanguageId == null) {
			wordLanguage = languageService.findLanguage(wordLanguage.getName());
			word.setLanguage(wordLanguage);
		}

		List<WordTranslateEntity> wordTranslates = word.getTranslates();
		for (WordTranslateEntity translate : wordTranslates) {
			WordEntity translateWord = translate.getTranslateWord();

			if (translateWord.getValue() != null) {
				translateWord.setValue(translateWord.getValue().trim().toLowerCase());
			} else {
				continue;
			}

			if (translateWord.getValue().isEmpty()) {
				continue;
			}

			translate.setSourceWord(word);
			translate.setTranslateWord(add(translateWord));
			translateService.add(translate);
		}

		wordRepository.save(word);
		return word;
	}

	@Override
	public WordEntity find(Long wordId) {
		WordEntity word = wordRepository.findById(wordId).orElse(null);
		EntityCleaning.clearLazyFields(word);
		return word;
	}

	@Override
	public void delete(Long wordId) {
		wordRepository.deleteById(wordId);
	}

	@Override
	public List<WordEntity> findWithTranslates() {
		List<WordTranslateEntity> wordTranslates = translateService.findAll();
		Map<WordEntity, List<WordTranslateEntity>> wordTranslateMap = wordTranslates.stream()
				.collect(Collectors.groupingBy(WordTranslateEntity::getSourceWord));
		List<WordEntity> words = new ArrayList<>(wordTranslateMap.keySet().size());
		for (Map.Entry<WordEntity, List<WordTranslateEntity>> wordTranslate : wordTranslateMap.entrySet()) {
			WordEntity word = wordTranslate.getKey();
			List<WordTranslateEntity> wTrs = wordTranslate.getValue().stream()
					.collect(Collectors.toList());
			word.setTranslates(wTrs);
			words.add(word);
		}
		return words;
	}

	@Override
	public WordEntity findWithTranslate(Long wordId) {
		List<WordEntity> words = transformTranslatesToWords(translateService.findBySourceWordId(wordId));

		if (words.isEmpty()) {
			WordEntity word = find(wordId);
			word.setTranslates(Collections.emptyList());
			return word;
		}

		return words.iterator().next();
	}

	@Override
	public List<WordEntity> findWithTranslate(Long userId, Long folderId, Long fileId) {
		List<WordTranslateEntity> translates = null;
		if (fileId != null) {
			translates = translateService.findByTextFileId(fileId, userId);
		} else if (folderId != null) {
			translates = translateService.findInFolderByFolderId(folderId, userId);
		} else {
			translates = translateService.findByUserId(userId);
		}
		return transformTranslatesToWords(translates);
	}

    @Override
    public List<Word> someWords() {
		List<String> someWords = Arrays.asList(
				"here",
				"that",
				"than",
				"them",
				"their",
				"then",
				"there",
				"they",
				"those",
				"thus",
				"were",
				"when",
				"where"
		);

		List<WordTranslateEntity> translates = translateService.findBySourceWordValueIdIn(someWords);
		List<WordEntity> words = transformTranslatesToWords(translates);
		words.sort(Comparator.comparing(w -> w.getValue()));
		Collections.reverse(words);
		return words.stream()
				.map(w -> new Word(w))
				.collect(Collectors.toList());
    }

	@Override
	public List<WordEntity> words() {
		List<WordTranslateEntity> translates = translateService.findAll();
		return transformTranslatesToWords(translates);
	}

	@Override
	public List<WordEntity> transformTranslatesToWords(List<WordTranslateEntity> translates) {
		EntityCleaning.clearLazyFieldsTranslate(translates);
        return EntityTransformer.translatesToWords(translates);
	}
}
