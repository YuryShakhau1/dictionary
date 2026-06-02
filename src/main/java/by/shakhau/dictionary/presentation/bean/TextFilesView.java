package by.shakhau.dictionary.presentation.bean;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.TextFileEntity;

public class TextFilesView {
    private List<TextFileEntity> textFiles;

    public List<TextFileEntity> getTextFiles() {
        return textFiles;
    }

    public void setTextFiles(List<TextFileEntity> textFiles) {
        this.textFiles = textFiles;
    }
}
