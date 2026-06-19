package by.shakhau.dictionary.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;

public interface TextFileWordRepository extends JpaRepository<TextFileWordEntity, Long> {
	List<TextFileWordEntity> findByTextFileId(Long fileId);
	List<TextFileWordEntity> findByTextFileFolderId(Long fileId);
	List<TextFileWordEntity> findByTextFileFolderIdIn(List<Long> fileIds);
	List<TextFileWordEntity> findByTextFileUserId(Long userId);
	List<TextFileWordEntity> findByWordId(Long wordId);
}
