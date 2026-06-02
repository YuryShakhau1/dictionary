package by.shakhau.dictionary.presentation.bean;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;

public class LanguagesView {

    private List<LanguageEntity> languages;

    public LanguagesView(List<LanguageEntity> languages) {
        this.languages = languages;
    }

    public List<LanguageEntity> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageEntity> languages) {
        this.languages = languages;
    }
}
