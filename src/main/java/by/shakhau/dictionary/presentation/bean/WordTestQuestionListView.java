package by.shakhau.dictionary.presentation.bean;

import java.util.List;

public class WordTestQuestionListView {
	private List<WordTestQuestionView> wordTestQuestions;

	public WordTestQuestionListView() {}

	public WordTestQuestionListView(List<WordTestQuestionView> wordTestQuestions) {
		this.wordTestQuestions = wordTestQuestions;
	}

	public List<WordTestQuestionView> getWordTestQuestions() {
		return wordTestQuestions;
	}

	public void setWordTestQuestions(List<WordTestQuestionView> wordTestQuestions) {
		this.wordTestQuestions = wordTestQuestions;
	}
}
