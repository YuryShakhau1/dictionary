package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.*;
import by.shakhau.dictionary.persistence.domain.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class UserWord {

	private Long id;
	private long wordId;
	private long userId;
	private Long statusId;

	public UserWord(UserWordEntity userWord) {
		this.id = userWord.getId();
		this.wordId = userWord.getWord().getId();
		this.userId = userWord.getUserId();
		this.statusId = userWord.getStatus().getId();
	}
}
