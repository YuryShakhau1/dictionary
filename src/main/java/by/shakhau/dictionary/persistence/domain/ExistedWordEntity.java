package by.shakhau.dictionary.persistence.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "ExistedWord")
@jakarta.persistence.Entity
@NoArgsConstructor
@Getter
@Setter
public class ExistedWordEntity implements Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;
    private String transcription;
    private Long languageId;

    public ExistedWordEntity(String value, String transcription, Long languageId) {
        this.value = value;
        this.transcription = transcription;
        this.languageId = languageId;
    }
}
