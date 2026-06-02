package by.shakhau.dictionary.logic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;

public final class EntityTransformer {
	
	private EntityTransformer() {}
	
	public static WordEntity translatesToWord(Collection<WordTranslateEntity> wordTranslates) {
		Map<WordEntity, List<WordTranslateEntity>> wordTranslatesMap = wordTranslates.stream()
				.collect(Collectors.groupingBy(x -> x.getSourceWord()));

		wordTranslates.forEach(translate -> translate.setSourceWord(null));
		wordTranslatesMap.forEach((k, v) -> k.setTranslates(v));
		return wordTranslatesMap.keySet().stream().findFirst().orElse(null);
	}
	
	public static List<WordEntity> translatesToWords(Collection<WordTranslateEntity> wordTranslates) {
		Map<WordEntity, List<WordTranslateEntity>> wordTranslatesMap = wordTranslates.stream()
				.collect(Collectors.groupingBy(x -> x.getSourceWord()));

		wordTranslates.forEach(translate -> translate.setSourceWord(null));
		wordTranslatesMap.forEach((k, v) -> {
			LinkedHashSet<WordTranslateEntity> uniqueTranslates = new LinkedHashSet<>(v);
			k.setTranslates(new ArrayList<>(uniqueTranslates));
		});
		return wordTranslatesMap.keySet().stream().collect(Collectors.toList());
	}
}
