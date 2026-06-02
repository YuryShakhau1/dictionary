package by.shakhau.dictionary.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;

public interface UserWordStatusRepository extends JpaRepository<UserWordStatusEntity, Long> {
	UserWordStatusEntity findOneByStatus(Integer status);
}
