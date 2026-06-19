package by.shakhau.dictionary.persistence.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "WordTranslate")
@jakarta.persistence.Entity
public class WordTranslateEntity implements Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
    @JoinColumn(name = "sourceWordId")
    private WordEntity sourceWord;

    @ManyToOne
    @JoinColumn(name = "translateWordId")
    private WordEntity translateWord;

    public WordTranslateEntity() {}

    public WordTranslateEntity(WordEntity sourceWord, WordEntity translateWord) {
		super();
		this.sourceWord = sourceWord;
		this.translateWord = translateWord;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WordEntity getSourceWord() {
		return sourceWord;
	}

	public void setSourceWord(WordEntity sourceWord) {
		this.sourceWord = sourceWord;
	}

	public WordEntity getTranslateWord() {
		return translateWord;
	}

	public void setTranslateWord(WordEntity translateWord) {
		this.translateWord = translateWord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceWord == null) ? 0 : sourceWord.hashCode());
		result = prime * result + ((translateWord == null) ? 0 : translateWord.hashCode());
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
		WordTranslateEntity other = (WordTranslateEntity) obj;
		if (sourceWord == null) {
			if (other.sourceWord != null)
				return false;
		} else if (!sourceWord.equals(other.sourceWord))
			return false;
		if (translateWord == null) {
			if (other.translateWord != null)
				return false;
		} else if (!translateWord.equals(other.translateWord))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WordTranslate [id=" + id + ", sourceWord=" + sourceWord + ", translateWord=" + translateWord + "]";
	}
}
