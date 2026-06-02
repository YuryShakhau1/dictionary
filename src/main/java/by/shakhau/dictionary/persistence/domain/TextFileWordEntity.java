package by.shakhau.dictionary.persistence.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "TextFileWord")
@javax.persistence.Entity
public class TextFileWordEntity implements Entity<Long> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String value;

	@ManyToOne
	@JoinColumn(name = "wordId")
	private WordEntity word;
	
	@ManyToOne
	@JoinColumn(name = "textFileId")
	private TextFileEntity textFile;
	
	private Long orderIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public WordEntity getWord() {
		return word;
	}

	public void setWord(WordEntity word) {
		this.word = word;
	}

	public TextFileEntity getTextFile() {
		return textFile;
	}

	public void setTextFile(TextFileEntity textFile) {
		this.textFile = textFile;
	}

	public Long getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderIndex == null) ? 0 : orderIndex.hashCode());
		result = prime * result + ((textFile == null) ? 0 : textFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextFileWordEntity other = (TextFileWordEntity) obj;
		if (orderIndex == null) {
			if (other.orderIndex != null)
				return false;
		} else if (!orderIndex.equals(other.orderIndex))
			return false;
		if (textFile == null) {
			if (other.textFile != null)
				return false;
		} else if (!textFile.equals(other.textFile))
			return false;
		return true;
	}
}
