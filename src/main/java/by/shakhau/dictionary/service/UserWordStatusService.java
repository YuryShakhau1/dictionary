package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;

import java.util.List;

public interface UserWordStatusService {
	List<UserWordStatusEntity> findAll();
	UserWordStatusEntity find(Integer status);
	void save(UserWordStatusEntity status);
}
