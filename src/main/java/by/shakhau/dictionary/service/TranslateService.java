package by.shakhau.dictionary.service;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;

public interface TranslateService {
	void add(WordTranslateEntity translate);
	void delete(Long translateId);
	List<WordTranslateEntity> findByTextFileId(Long fileId, Long userId);
	List<WordTranslateEntity> findBySourceWordIdsIdIn(List<Long> wordIds);
	List<WordTranslateEntity> findBySourceWordValueIdIn(List<String> words);
	List<WordTranslateEntity> findByUserId(Long userId);
	List<WordTranslateEntity> findInFolderByFolderId(Long folderId, Long userId);
	List<WordTranslateEntity> findInFolderByFolderIds(List<Long> folderIds);
	List<WordTranslateEntity> findBySourceWordId(Long wordId);
	List<WordTranslateEntity> findByTranslateWordId(Long wordId);
	List<WordTranslateEntity> findAll();
}
