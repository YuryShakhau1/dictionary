package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.presentation.bean.LanguagesView;

import java.util.List;

public interface LanguageService {
    List<LanguageEntity> findAll();
    LanguagesView languageList();
    LanguageEntity findLanguage(String name);
    void addLanguage(String languageName);
    void deleteLanguage(Long id);
}
