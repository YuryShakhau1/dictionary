package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.repository.LanguageRepository;
import by.shakhau.dictionary.presentation.bean.LanguagesView;
import by.shakhau.dictionary.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

	private Map<String, LanguageEntity> cache = new HashMap<>();

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public List<LanguageEntity> findAll() {
        return languageRepository.findAll();
    }

    @Override
	public LanguagesView languageList() {
		List<LanguageEntity> languages = languageRepository.findAll();
		return new LanguagesView(languages);
	}

	@Override
	public LanguageEntity findLanguage(String name) {
		if (name == null) {
			return null;
		}

        return cache.computeIfAbsent(name, k -> languageRepository.findOneByName(name));
	}

	@Override
	public void addLanguage(String languageName) {
		LanguageEntity language = new LanguageEntity();
		language.setName(languageName);
		languageRepository.save(language);
	}

	@Override
	public void deleteLanguage(Long id) {
		languageRepository.findById(id).ifPresent(lang -> {
			languageRepository.deleteById(lang.getId());
			cache.remove(lang.getName());
		});
	}
}
