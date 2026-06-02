package by.shakhau.dictionary.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
	LanguageEntity findOneByName(String name);

	@Override
	void deleteById(Long languageId);
}
