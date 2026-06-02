package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.persistence.domain.ExistedWordEntity;
import by.shakhau.dictionary.persistence.repository.ExistedWordRepository;
import by.shakhau.dictionary.service.ExistedWordService;
import by.shakhau.dictionary.service.bean.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExistedWordServiceImpl implements ExistedWordService {

    private Map<String, Value<ExistedWordEntity>> cache = new HashMap<>();

    @Autowired
    private ExistedWordRepository existedWordRepository;

    @Override
    public List<ExistedWordEntity> findAll() {
        return existedWordRepository.findAll();
    }

    @Override
    public ExistedWordEntity find(String value, Long languageId) {
        return cache.computeIfAbsent(value, k -> new Value<>(existedWordRepository.findOneByValueAndLanguageId(value, languageId))).getValue();
    }

    @Override
    public void resetCache() {
        cache = new HashMap<>();
    }
}
