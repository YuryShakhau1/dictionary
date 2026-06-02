package by.shakhau.dictionary.presentation.bean;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.UserWordEntity;

public class UserWordsView {
	private List<UserWordEntity> userWords;

	public UserWordsView() {}

	public UserWordsView(List<UserWordEntity> userWords) {
		this.userWords = userWords;
	}

	public List<UserWordEntity> getUserWords() {
		return userWords;
	}

	public void setUserWords(List<UserWordEntity> userWords) {
		this.userWords = userWords;
	}
}
