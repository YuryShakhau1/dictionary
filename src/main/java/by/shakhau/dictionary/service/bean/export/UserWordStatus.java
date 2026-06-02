package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserWordStatus {

	private Long id;
	private String comment;
	private Integer status;

	public UserWordStatus(UserWordStatusEntity userWordStatus) {
		this.id = userWordStatus.getId();
		this.comment = userWordStatus.getComment();
		this.status = userWordStatus.getStatus();
	}
}
