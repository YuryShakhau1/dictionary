package by.shakhau.dictionary.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.shakhau.dictionary.persistence.domain.TextFileEntity;

public interface TextFileRepository extends JpaRepository<TextFileEntity, Long> {
    List<TextFileEntity> findByFolderIdAndUserId(Long folderId, Long userId);
    
    @Query("SELECT name FROM TextFileEntity WHERE id = :fileId")
    String findNameById(@Param("fileId") Long fileId);

    List<TextFileEntity> findByUserId(Long userId);
}
