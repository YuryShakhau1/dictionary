package by.shakhau.dictionary.presentation.bean;

import java.util.List;

public class TextFrequencyView {
	private List<WordFrequencyView> frequencies;
	private FrequencySummaryView summary;

	public List<WordFrequencyView> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(List<WordFrequencyView> frequencies) {
		this.frequencies = frequencies;
	}

	public FrequencySummaryView getSummary() {
		return summary;
	}

	public void setSummary(FrequencySummaryView summary) {
		this.summary = summary;
	}
}
