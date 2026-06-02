package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TextFileWord {

	private Long id;
	private String value;
	private Long wordId;
	private Long textFileId;
	private Long orderIndex;

	public TextFileWord(TextFileWordEntity textFileWord) {
		this.id = textFileWord.getId();
		this.value = textFileWord.getValue();
		if (textFileWord.getWord() != null) {
			this.wordId = textFileWord.getWord().getId();
		}
		this.textFileId = textFileWord.getTextFile().getId();
		this.orderIndex = textFileWord.getOrderIndex();
	}
}
