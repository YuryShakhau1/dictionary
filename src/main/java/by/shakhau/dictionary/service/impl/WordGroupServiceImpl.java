package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.persistence.domain.WordGroupEntity;
import by.shakhau.dictionary.persistence.domain.WordGroupItemEntity;
import by.shakhau.dictionary.persistence.repository.WordGroupItemRepository;
import by.shakhau.dictionary.persistence.repository.WordGroupRepository;
import by.shakhau.dictionary.service.WordGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordGroupServiceImpl implements WordGroupService {

    private WordGroupRepository repository;
    private WordGroupItemRepository wordGroupItemRepository;

    @Override
    public List<WordGroupEntity> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<WordGroupItemEntity> findWordGroupItems(Long wordGroupId) {
        return wordGroupItemRepository.findByWordGroupId(wordGroupId);
    }

    @Override
    public void delete(Long wordGroupId) {
        repository.deleteById(wordGroupId);
    }
}
