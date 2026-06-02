package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;

import java.util.Collection;
import java.util.List;

public interface TextFileWordService {
	List<TextFileWordEntity> findAll(Long userId);
	List<TextFileWordEntity> prepareWords(TextFileEntity textFile);
	void add(Collection<TextFileWordEntity> textFileWords);
	List<TextFileWordEntity> findByFileId(Long fileId);
	List<TextFileWordEntity> findInFolderByFolderId(Long folderId, Long userId);
	List<TextFileWordEntity> findInFolderByFolderIds(List<Long> folderIds);
	TextFileWordEntity findById(Long sentenceWordId);
	void delete(Long sentenceWordId);
	void delete(Collection<TextFileWordEntity> sentenceWords);
	List<TextFileWordEntity> findByWordId(Long wordId);
}
