package by.shakhau.dictionary.persistence.repository;

import by.shakhau.dictionary.persistence.domain.ExistedWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExistedWordRepository extends JpaRepository<ExistedWordEntity, Long> {

    ExistedWordEntity findOneByValueAndLanguageId(String value, Long languageId);
}
