package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.service.bean.TranslateWord;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Word extends TranslateWord {

    private List<TranslateWord> translates;

	public Word(WordEntity word) {
        super(word);
        this.translates = word.getTranslates().stream()
                .map(t -> new TranslateWord(t.getTranslateWord()))
                .collect(Collectors.toList());
	}
}
