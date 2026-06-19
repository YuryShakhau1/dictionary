package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.repository.TextFileWordRepository;
import by.shakhau.dictionary.service.ExistedWordService;
import by.shakhau.dictionary.service.FolderService;
import by.shakhau.dictionary.service.TextFileWordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TextFileWordServiceImpl implements TextFileWordService {

	private ExistedWordService existedWordService;
	private TextFileWordRepository textFileWordRepository;
	private FolderService folderService;

	@Override
	public List<TextFileWordEntity> findAll(Long userId) {
		return textFileWordRepository.findByTextFileUserId(userId);
	}

	@Override
	public List<TextFileWordEntity> prepareWords(TextFileEntity textFile) {
		List<String> wordTexts = extractWords(textFile.getValue()).stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList());

		List<TextFileWordEntity> sentenceWords = new ArrayList<>();
		AtomicLong orderIndex = new AtomicLong(0);
		wordTexts.forEach(wordName -> {
			WordEntity word = existedWordService.findOrCreateByValuesAndLanguage(wordName);

			TextFileWordEntity sentenceWord = new TextFileWordEntity();
			sentenceWord.setTextFile(textFile);
			sentenceWord.setValue(wordName);
			sentenceWord.setWord(word);
			sentenceWord.setOrderIndex(orderIndex.incrementAndGet());
			sentenceWords.add(sentenceWord);
		});
		existedWordService.resetCache();
		return sentenceWords;
	}

	private List<String> extractWords(String fileContent) {
		List<String> words = new ArrayList<>();
		boolean beginSentence = false;
		int beginIndex = 0;
		for (int i = 0; i < fileContent.length(); i++) {
			char c = fileContent.charAt(i);
			if (beginSentence && !charIsLetter(c)) {
				words.add(fileContent.substring(beginIndex, i));
				beginSentence = false;
				continue;
			}

			if (!beginSentence && charIsBeginLetter(c)) {
				beginSentence = true;
				beginIndex = i;
			}
		}
		return words;
	}

	private boolean charIsBeginLetter(char c) {
		return (c >= 'A' && c <= 'Z')
				|| (c >= 'a' && c <= 'z')
				|| (c >= 'А' && c <= 'Я')
				|| (c >= 'а' && c <= 'я')
				|| c == '\'';
	}

	private boolean charIsLetter(char c) {
		return charIsBeginLetter(c)
				|| c == '\''
				|| c == '-';
	}

	@Override
	public List<TextFileWordEntity> findByFileId(Long fileId) {
		List<TextFileWordEntity> textFileWords = textFileWordRepository.findByTextFileId(fileId);
		EntityCleaning.clearLazyFieldsTextFileWord(textFileWords);
		return textFileWords;
	}

	@Override
	public List<TextFileWordEntity> findInFolderByFolderId(Long folderId, Long userId) {
		List<Long> ids = folderService.findAllIds(folderId, userId);
		return findInFolderByFolderIds(ids);
	}

	@Override
	public List<TextFileWordEntity> findInFolderByFolderIds(List<Long> folderIds) {
		List<TextFileWordEntity> textFileWordsResult = null;
		if (!folderIds.isEmpty()) {
			textFileWordsResult = textFileWordRepository.findByTextFileFolderIdIn(folderIds);
		} else {
			textFileWordsResult = textFileWordRepository.findAll();
		}
		EntityCleaning.clearLazyFieldsTextFileWord(textFileWordsResult);
		return textFileWordsResult;
	}

	@Override
	public TextFileWordEntity findById(Long sentenceWordId) {
		return textFileWordRepository.findById(sentenceWordId).orElse(null);
	}

	@Override
	public void delete(Long sentenceWordId) {
		textFileWordRepository.deleteById(sentenceWordId);
	}

	@Override
	public void delete(Collection<TextFileWordEntity> sentenceWords) {
		textFileWordRepository.deleteAll(sentenceWords);
	}

	@Override
	public void add(Collection<TextFileWordEntity> textFileWords) {
		textFileWordRepository.saveAll(textFileWords);
	}

	@Override
	public List<TextFileWordEntity> findByWordId(Long wordId) {
		List<TextFileWordEntity> textFileWords = textFileWordRepository.findByWordId(wordId);
		EntityCleaning.clearLazyFieldsTextFileWord(textFileWords);
		return textFileWords;
	}
}
