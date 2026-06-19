package by.shakhau.dictionary.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "Word")
@jakarta.persistence.Entity
public class WordEntity implements Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private String transcription;

    @ManyToOne
    @JoinColumn(name = "languageId")
    private LanguageEntity language;

    @OneToMany(mappedBy = "translateWord")
    private List<WordTranslateEntity> translates;

    @OneToMany(mappedBy = "word")
	@JsonIgnore
    private List<UserWordEntity> userWords;

    @OneToMany(mappedBy = "word")
	@JsonIgnore
    private List<TextFileWordEntity> textFileWords;

    public WordEntity() {}

	public WordEntity(String value, LanguageEntity language) {
		super();
		this.value = value;
		this.language = language;
	}

	@Override
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

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public LanguageEntity getLanguage() {
        return language;
    }

    public void setLanguage(LanguageEntity language) {
        this.language = language;
    }

    public List<WordTranslateEntity> getTranslates() {
        return translates;
    }

    public void setTranslates(List<WordTranslateEntity> translate) {
        this.translates = translate;
    }

	public List<UserWordEntity> getUserWords() {
		return userWords;
	}

	public void setUserWords(List<UserWordEntity> userWords) {
		this.userWords = userWords;
	}

	public List<TextFileWordEntity> getTextFileWords() {
		return textFileWords;
	}

	public void setTextFileWords(List<TextFileWordEntity> textFileWords) {
		this.textFileWords = textFileWords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		WordEntity other = (WordEntity) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Word [id=" + id + ", value=" + value + ", transcription=" + transcription + ", language=" + language;
	}
}
