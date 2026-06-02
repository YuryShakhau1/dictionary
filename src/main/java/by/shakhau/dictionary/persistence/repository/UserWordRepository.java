package by.shakhau.dictionary.persistence.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.shakhau.dictionary.persistence.domain.UserWordEntity;

public interface UserWordRepository extends JpaRepository<UserWordEntity, Long> {
	UserWordEntity findOneByUserIdAndWordId(Long userId, Long wordId);
	
	@Query(value = "SELECT w.id FROM UserWord uw "
			+ "INNER JOIN Word w ON uw.wordId = w.id "
			+ "INNER JOIN User u ON uw.userId = u.id "
			+ "WHERE u.id=:userId", nativeQuery = true)
	Set<BigInteger> wordIdByUser(@Param("userId") Long userId);
	
	@Query(value = "SELECT w.id FROM UserWord uw "
			+ "INNER JOIN Word w ON uw.wordId = w.id "
			+ "INNER JOIN User u ON uw.userId = u.id "
			+ "INNER JOIN UserWordStatus s ON uw.statusId = s.id "
			+ "WHERE u.id=:userId AND s.status = :status", nativeQuery = true)
	Set<BigInteger> wordIdByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
	
	@Query(value = "SELECT w.id FROM UserWord uw "
			+ "INNER JOIN Word w ON uw.wordId = w.id "
			+ "INNER JOIN User u ON uw.userId = u.id "
			+ "INNER JOIN UserWordStatus s ON uw.statusId = s.id "
			+ "WHERE u.id = :userId AND s.status >= :status", nativeQuery = true)
	Set<BigInteger> wordIdByUserIdAndStatusFrom(@Param("userId") Long userId, @Param("status") Integer status);
	
	List<UserWordEntity> findByUserId(Long userId);
	List<UserWordEntity> findByUserIdAndStatusStatus(Long userId, Integer status);
	List<UserWordEntity> findByUserIdAndStatusStatusGreaterThanEqual(Long userId, Integer status);
	List<UserWordEntity> findByWordId(Long wordId);
	UserWordEntity findOneByWordIdAndUserId(Long wordId, Long userId);
}
