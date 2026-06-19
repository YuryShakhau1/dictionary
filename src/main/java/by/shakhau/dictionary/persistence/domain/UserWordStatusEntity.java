package by.shakhau.dictionary.persistence.domain;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "UserWordStatus")
@jakarta.persistence.Entity
public class UserWordStatusEntity implements Entity<Long> {
	public static final Integer EXCLUDED = -1;
	public static final Integer KNOW = 0;
	public static final Integer DO_NOT_KNOW = 1;
	public static final Integer ALREADY_KNOWN = 2;
	public static final Integer ALMOST_KNOWN = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String comment;

	private Integer status;

	@OneToMany(mappedBy = "status")
	private List<UserWordEntity> userWords;

	public UserWordStatusEntity() {}

	public UserWordStatusEntity(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<UserWordEntity> getUserWords() {
		return userWords;
	}

	public void setUserWords(List<UserWordEntity> userWords) {
		this.userWords = userWords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		UserWordStatusEntity other = (UserWordStatusEntity) obj;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
}
