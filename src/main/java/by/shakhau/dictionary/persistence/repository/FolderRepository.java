package by.shakhau.dictionary.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import by.shakhau.dictionary.persistence.domain.FolderEntity;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    List<FolderEntity> findByParentFolderIdIsNullAndUserId(Long userId);
    List<FolderEntity> findByUserId(Long userId);
    List<FolderEntity> findByParentFolderIdAndUserId(Long folderId, Long userId);
}
