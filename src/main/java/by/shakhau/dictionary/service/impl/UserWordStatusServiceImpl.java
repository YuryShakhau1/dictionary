package by.shakhau.dictionary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;
import by.shakhau.dictionary.persistence.repository.UserWordStatusRepository;
import by.shakhau.dictionary.service.UserWordStatusService;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserWordStatusServiceImpl implements UserWordStatusService {

	private UserWordStatusRepository userWordStatusRepository;

    @Override
    public List<UserWordStatusEntity> findAll() {
        return userWordStatusRepository.findAll();
    }

    @Override
	public UserWordStatusEntity find(Integer status) {
		if (status == null) {
			return null;
		}

		UserWordStatusEntity userWordStatus = userWordStatusRepository.findOneByStatus(status);
		EntityCleaning.clearLazyFields(userWordStatus);
		return userWordStatus;
	}

	@Override
	public void save(UserWordStatusEntity status) {
		userWordStatusRepository.save(status);
	}
}
