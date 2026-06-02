package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.ExistedWordEntity;

import java.util.List;

public interface ExistedWordService {

    List<ExistedWordEntity> findAll();
    ExistedWordEntity find(String value, Long languageId);
    void resetCache();
}
