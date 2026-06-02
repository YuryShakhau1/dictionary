package by.shakhau.dictionary.persistence.repository;

import by.shakhau.dictionary.persistence.domain.WordGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordGroupRepository extends JpaRepository<WordGroupEntity, Long> {

    List<WordGroupEntity> findByUserId(Long userId);
}
