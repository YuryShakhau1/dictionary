package by.shakhau.dictionary.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import by.shakhau.dictionary.logic.util.HttpLoader;
import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.presentation.bean.translate.TranslateContainer;
import by.shakhau.dictionary.presentation.bean.translate.TranslateGroup;
import by.shakhau.dictionary.presentation.bean.translate.TranslateValue;
import by.shakhau.dictionary.service.LanguageService;
import by.shakhau.dictionary.service.LoadTranslateService;
import by.shakhau.dictionary.service.WordService;


@Service
public class LoadTranslateServiceImpl implements LoadTranslateService {
	private static final int MAX_TRANSLATE_COUNT = 5;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private WordService wordService;
	
	@Override
	public WordEntity downloadTranslate(Long wordId) {
		WordEntity foundWord = wordService.find(wordId);
		String jsonContent = HttpLoader.loadHttp("https://dictionary.yandex.net/dicservice.json/lookupMultiple?srv=tr-text&text=" + foundWord.getValue() + "&dict=en-ru.regular&flags=39")
				 .toString();
		ObjectMapper mapper = new ObjectMapper();
		TranslateContainer translateContainer = null;

		try {
			translateContainer = mapper.readValue(jsonContent, TranslateContainer.class);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		WordEntity word = translateContainerToWord(translateContainer);
		foundWord.setTranscription(word.getTranscription());
		foundWord.setTranslates((word.getTranslates() != null) ? word.getTranslates() : Collections.emptyList());
		return foundWord;
	}
	
	private WordEntity translateContainerToWord(TranslateContainer translateContainer) {
		WordEntity word = new WordEntity();
		List<TranslateGroup> translateGroups = translateContainer.getEnRu().getRegular();
		
		if (translateGroups.isEmpty()) {
			return word;
		}
		
		List<WordTranslateEntity> wordTranslates = new ArrayList<>(MAX_TRANSLATE_COUNT);
		LanguageEntity ruLandguage = languageService.findLanguage(LanguageEntity.RU);

		int translateGroupsSize = translateGroups.size();
		int groupIndex = 0;
		int valueIndex = 0;
		int failureCount = 0;
		while (groupIndex < translateGroupsSize) {
			TranslateGroup translateGroup = translateGroups.get(groupIndex);
			String transalteText = translateGroup.getText();
			String transcriptionText = translateGroup.getTranscription();
			word.setValue(transalteText);
			word.setTranscription(transcriptionText);

			List<TranslateValue> translateValues = translateGroup.getTranslates();

			TranslateValue translateValue = null;
			if (valueIndex < translateValues.size()) {
				translateValue = translateValues.get(valueIndex);
			} else {
				failureCount++;
			}
			
			if (translateValue != null) {
				wordTranslates.add(new WordTranslateEntity(null, new WordEntity(translateValue.getText(), ruLandguage)));
				if (wordTranslates.size() == MAX_TRANSLATE_COUNT) {
					break;
				}
			}
			
			groupIndex++;
			if (groupIndex == translateGroupsSize) {
				if (failureCount == translateGroupsSize) {
					break;
				}
				valueIndex++;
				groupIndex = 0;
				failureCount = 0;
			}
		}

		word.setTranslates(wordTranslates);

		return word;
	}
}
