package by.shakhau.dictionary.presentation.bean.translate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslateGroup {

	@JsonProperty("text")
	private String text;
	
	@JsonProperty("ts")
	private String transcription;
	
	@JsonProperty("tr")
	private List<TranslateValue> translates;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public List<TranslateValue> getTranslates() {
		return translates;
	}

	public void setTranslates(List<TranslateValue> translates) {
		this.translates = translates;
	}
}
