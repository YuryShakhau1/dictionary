package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.WordEntity;

public interface LoadTranslateService {
	WordEntity downloadTranslate(Long wordId);
}
