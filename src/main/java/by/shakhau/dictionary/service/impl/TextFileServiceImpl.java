package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.logic.util.FileHelper;
import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import by.shakhau.dictionary.persistence.repository.FolderRepository;
import by.shakhau.dictionary.persistence.repository.TextFileRepository;
import by.shakhau.dictionary.presentation.bean.TextFilesView;
import by.shakhau.dictionary.service.TextFileService;
import by.shakhau.dictionary.service.TextFileWordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class TextFileServiceImpl implements TextFileService {

    private TextFileWordService textFileWordService;
    private TextFileRepository textFileRepository;
    private FolderRepository folderRepository;

    @Override
    public List<TextFileEntity> findAllFiles(Long userId) {
        return textFileRepository.findByUserId(userId);
    }

    @Override
    public TextFilesView folderFiles(Long userId, Long folderId) {
        TextFilesView textFilesView = new TextFilesView();
        List<TextFileEntity> textFiles = textFileRepository.findByFolderIdAndUserId(folderId, userId);
        EntityCleaning.clearLazyFieldsTextFile(textFiles);
        textFilesView.setTextFiles(textFiles);
        return textFilesView;
    }

    @Override
    public void uploadTextFile(Long userId, Long parentFolderId, String fileName, InputStream inputStream) {
        String fileContent = inputStreamContentUTF8(inputStream);
        TextFileEntity textFile = new TextFileEntity();
        textFile.setName(fileName);
        textFile.setValue(fileContent);
        if (parentFolderId != null) {
            textFile.setFolder(folderRepository.findById(parentFolderId).orElse(null));
        }
        textFile.setUserId(userId);
        List<TextFileWordEntity> textFileWords = textFileWordService.prepareWords(textFile);
        textFileRepository.save(textFile);
        textFileWordService.add(textFileWords);
    }

    @Override
    public TextFileEntity findFile(Long fileId) {
    	TextFileEntity textFile = textFileRepository.findById(fileId).orElse(null);
    	EntityCleaning.clearLazyFields(textFile);
        return textFile;
    }

    @Override
    public String fileName(Long fileId) {
    	return textFileRepository.findNameById(fileId);
    }

    @Override
    public void deleteTextFile(Long fileId) {
    	List<TextFileWordEntity> fileWords = textFileWordService.findByFileId(fileId);
    	textFileWordService.delete(fileWords);
        textFileRepository.deleteById(fileId);
    }

    private String inputStreamContentUTF8(InputStream inputStream) {
        return FileHelper.inputStreamContent(inputStream, "UTF8");
    }
}
