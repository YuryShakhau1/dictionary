package by.shakhau.dictionary.service;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.service.bean.Word;

public interface WordService {
	List<WordEntity> findAll();
	WordEntity add(String word, LanguageEntity language);
	WordEntity add(WordEntity word);
	WordEntity save(WordEntity word);
	WordEntity find(Long wordId);
	void delete(Long wordId);
	List<WordEntity> findWithTranslates();
	WordEntity findWithTranslate(Long wordId);
	List<WordEntity> findWithTranslate(Long userId, Long folderId, Long fileId);
	List<Word> someWords();
	List<WordEntity> words();
	List<WordEntity> transformTranslatesToWords(List<WordTranslateEntity> translates);
}
