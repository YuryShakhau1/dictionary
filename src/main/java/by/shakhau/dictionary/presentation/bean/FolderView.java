package by.shakhau.dictionary.presentation.bean;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.FolderEntity;

public class FolderView {
    private Long currentFolderId;
    private Long parentFolderId;
    private List<FolderEntity> folders;

    public Long getCurrentFolderId() {
        return currentFolderId;
    }

    public void setCurrentFolderId(Long currentFolderId) {
        this.currentFolderId = currentFolderId;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public List<FolderEntity> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderEntity> folders) {
        this.folders = folders;
    }
}
