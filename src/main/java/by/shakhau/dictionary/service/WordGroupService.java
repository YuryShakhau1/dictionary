package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.WordGroupEntity;
import by.shakhau.dictionary.persistence.domain.WordGroupItemEntity;

import java.util.List;

public interface WordGroupService {

    List<WordGroupEntity> findByUserId(Long userId);
    List<WordGroupItemEntity> findWordGroupItems(Long wordGroupId);
    void delete(Long wordGroupId);
}
