package by.shakhau.dictionary.service.bean;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Word {

    private Long id;
    private String value;
    private String transcription;
    private LanguageEntity language;
    private List<WordTranslate> translates;

    public Word(WordEntity word) {
        this.id = word.getId();
        this.value = word.getValue();
        this.transcription = word.getTranscription();
        this.language = word.getLanguage();
        this.translates = word.getTranslates().stream()
                .map(t -> new WordTranslate(t.getId(), new TranslateWord(t.getTranslateWord())))
                .collect(Collectors.toList());
    }

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

    public List<WordTranslate> getTranslates() {
        return translates;
    }

    public void setTranslates(List<WordTranslate> translates) {
        this.translates = translates;
    }
}
