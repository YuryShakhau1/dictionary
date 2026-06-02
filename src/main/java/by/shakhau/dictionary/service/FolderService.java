package by.shakhau.dictionary.service;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.FolderEntity;
import by.shakhau.dictionary.presentation.bean.FolderView;

public interface FolderService {
	List<FolderEntity> findAllFolders(Long userId);
	List<FolderEntity> rootFolders(Long userId);
    FolderView folderChildren(Long parentFolderId, Long userId);
    List<String> folderPath(Long folderId);
    void addFolder(Long userId, Long parentFolderId, FolderEntity folder);
    void renameFolder(Long folderId, String newFolderName);
    void deleteFolder(Long folderId, Long userId);
    List<Long> findAllChildrenIds(Long folderId, Long userId);
    List<Long> findAllIds(Long folderId, Long userId);
}
