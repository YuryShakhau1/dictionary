package by.shakhau.dictionary.service;

import by.shakhau.dictionary.persistence.domain.UserWordEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.presentation.bean.TextFrequencyView;
import by.shakhau.dictionary.service.bean.WordInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface UserWordService {
	List<UserWordEntity> findAll();
	List<UserWordEntity> findAll(Long userId);
	WordInfo add(Long userId, Long wordId, Integer status);
	void levelUpStatus(Long userId, Long wordId);
	void changeStatus(Long userId, Long wordId, Integer wordStatus);
	void delete(Long id);
	void delete(List<UserWordEntity> userWords);
	void save(UserWordEntity userWord);
	List<UserWordEntity> findByWordId(Long wordId);
	UserWordEntity findByUserIdAndWordId(Long userId, Long wordId);
	List<UserWordEntity> findByUserIdAndWordStatus(Long userId, Integer wordStatus);
	List<WordEntity> findWithUserTranslate(Long userId, Long folderId, Long fileId, Integer wordStatus);
	Set<Long> findWordIdByUserIdAndWordStatus(Long userId, Integer wordStatus);
	TextFrequencyView wordFrequency(Long userId, Long folderId, Long fileId, Integer wordStatus, Integer wordStatusFrom);
}
