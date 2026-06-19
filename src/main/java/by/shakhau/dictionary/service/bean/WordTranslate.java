package by.shakhau.dictionary.service.bean;

import by.shakhau.dictionary.persistence.domain.Entity;
import by.shakhau.dictionary.persistence.domain.WordEntity;

import jakarta.persistence.*;

public class WordTranslate implements Entity<Long> {

    private Long id;

    private TranslateWord translateWord;

    public WordTranslate(Long id, TranslateWord translateWord) {
		this.id = id;
		this.translateWord = translateWord;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TranslateWord getTranslateWord() {
		return translateWord;
	}

	public void setTranslateWord(TranslateWord translateWord) {
		this.translateWord = translateWord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		WordTranslate other = (WordTranslate) obj;
		if (translateWord == null) {
			if (other.translateWord != null)
				return false;
		} else if (!translateWord.equals(other.translateWord))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WordTranslate [id=" + id + ", translateWord=" + translateWord + "]";
	}
}
