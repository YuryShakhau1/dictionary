package by.shakhau.dictionary.presentation.bean.translate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EnRu {
	
	@JsonProperty("regular")
	private List<TranslateGroup> regular;

	public List<TranslateGroup> getRegular() {
		return regular;
	}

	public void setRegular(List<TranslateGroup> regular) {
		this.regular = regular;
	}
}
