package by.shakhau.dictionary.persistence.repository;

import by.shakhau.dictionary.persistence.domain.WordGroupItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordGroupItemRepository extends JpaRepository<WordGroupItemEntity, Long> {

    List<WordGroupItemEntity> findByWordGroupId(Long wordGroupId);
}
