package by.shakhau.dictionary.service;

import java.io.InputStream;
import java.util.List;

import by.shakhau.dictionary.persistence.domain.FolderEntity;
import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import by.shakhau.dictionary.presentation.bean.TextFilesView;

public interface TextFileService {
    List<TextFileEntity> findAllFiles(Long userId);
    TextFilesView folderFiles(Long userId, Long folderId);
    void uploadTextFile(Long userId, Long parentFolderId, String fileName, InputStream inputStream);
    TextFileEntity findFile(Long fileId);
    String fileName(Long fileId);
    void deleteTextFile(Long fileId);
}
