package by.shakhau.dictionary.presentation.bean;

import by.shakhau.dictionary.persistence.domain.WordEntity;

public class WordFrequencyView {
	private WordEntity word;
	private int frequency;
	private Integer wordStatus;

	public WordFrequencyView() {}

	public WordFrequencyView(WordEntity word, int frequency, Integer wordStatus) {
		this.word = word;
		this.frequency = frequency;
		this.wordStatus = wordStatus;
	}

	public WordEntity getWord() {
		return word;
	}

	public void setWord(WordEntity word) {
		this.word = word;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Integer getWordStatus() {
		return wordStatus;
	}

	public void setWordStatus(Integer wordStatus) {
		this.wordStatus = wordStatus;
	}
}
