package by.shakhau.dictionary.presentation.bean;

import java.util.List;

import by.shakhau.dictionary.persistence.domain.WordEntity;

public class WordTestQuestionView {
	private WordEntity word;
	private Integer wordStatus;
	private List<AnswerOptionView> answerOptions;

	public WordEntity getWord() {
		return word;
	}

	public void setWord(WordEntity word) {
		this.word = word;
	}

	public Integer getWordStatus() {
		return wordStatus;
	}

	public void setWordStatus(Integer wordStatus) {
		this.wordStatus = wordStatus;
	}

	public List<AnswerOptionView> getAnswerOptions() {
		return answerOptions;
	}

	public void setAnswerOptions(List<AnswerOptionView> answerOptions) {
		this.answerOptions = answerOptions;
	}
}
