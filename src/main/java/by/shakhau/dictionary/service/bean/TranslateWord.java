package by.shakhau.dictionary.service.bean;

import by.shakhau.dictionary.persistence.domain.Entity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Getter
@Setter
public class TranslateWord implements Entity<Long> {

    private Long id;
    private String value;
    private String transcription;

    private long languageId;

    public TranslateWord(WordEntity word) {
        this.id = word.getId();
        this.value = new String(Base64.getEncoder().encode(word.getValue().getBytes(StandardCharsets.UTF_8)));
        this.transcription = word.getTranscription() != null
                ? new String(Base64.getEncoder().encode(word.getTranscription().getBytes(StandardCharsets.UTF_8)))
                : "";
        this.languageId = word.getLanguage().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslateWord that = (TranslateWord) o;
        return Objects.equals(value, that.value) && Objects.equals(languageId, that.languageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, languageId);
    }
}
