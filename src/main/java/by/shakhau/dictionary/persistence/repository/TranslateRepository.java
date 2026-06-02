package by.shakhau.dictionary.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;

public interface TranslateRepository extends JpaRepository<WordTranslateEntity, Long> {
	WordTranslateEntity findOneBySourceWordValueAndSourceWordLanguageIdAndTranslateWordValueAndTranslateWordLanguageId(
			String sourceValue, Long sourceLanguageId, String translateValue, Long translateLanguageId);
	List<WordTranslateEntity> findBySourceWordId(Long wordId);
	List<WordTranslateEntity> findBySourceWordIdIn(List<Long> sourceWordIds);
	List<WordTranslateEntity> findBySourceWordValueIn(List<String> wordValues);
	List<WordTranslateEntity> findByTranslateWordId(Long wordId);
	List<WordTranslateEntity> findBySourceWordTextFileWordsTextFileId(Long fileId);
	List<WordTranslateEntity> findBySourceWordUserWordsUserId(Long userId);
	List<WordTranslateEntity> findBySourceWordTextFileWordsTextFileFolderIdIn(List<Long> fileIds);
}
