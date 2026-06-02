package by.shakhau.dictionary.persistence.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ExistedWord")
@javax.persistence.Entity
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
