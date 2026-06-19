package by.shakhau.dictionary.persistence.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "UserWord")
public class UserWordEntity implements Entity<Long> {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "wordId")
	private WordEntity word;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "statusId")
	private UserWordStatusEntity status;
	
	public UserWordEntity() {}

	public UserWordEntity(WordEntity word, Long userId) {
		this.word = word;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WordEntity getWord() {
		return word;
	}

	public void setWord(WordEntity word) {
		this.word = word;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserWordStatusEntity getStatus() {
		return status;
	}

	public void setStatus(UserWordStatusEntity status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserWordEntity other = (UserWordEntity) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
}
