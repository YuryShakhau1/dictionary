package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Language {

    private Long id;
    private String name;

    public Language(LanguageEntity language) {
        this.id = language.getId();
        this.name = language.getName();
    }
}
