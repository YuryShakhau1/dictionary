package by.shakhau.dictionary.presentation.bean;

import by.shakhau.dictionary.persistence.domain.WordEntity;

public class AnswerOptionView {
	private WordEntity word;
	private Boolean selected;

	public AnswerOptionView() {}

	public AnswerOptionView(WordEntity word, Boolean selected) {
		this.word = word;
		this.selected = selected;
	}

	public WordEntity getWord() {
		return word;
	}

	public void setWord(WordEntity word) {
		this.word = word;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}
