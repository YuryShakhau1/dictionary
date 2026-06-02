package by.shakhau.dictionary.persistence.repository;

import by.shakhau.dictionary.persistence.domain.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface WordRepository extends JpaRepository<WordEntity, Long> {
	WordEntity findOneByValueAndLanguageId(String value, Long languageId);
}
