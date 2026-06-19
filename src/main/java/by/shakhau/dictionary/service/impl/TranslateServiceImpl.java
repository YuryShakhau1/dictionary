package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.persistence.repository.TranslateRepository;
import by.shakhau.dictionary.persistence.repository.WordRepository;
import by.shakhau.dictionary.service.FolderService;
import by.shakhau.dictionary.service.TranslateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TranslateServiceImpl implements TranslateService {

	private TranslateRepository translateRepository;
	private FolderService folderService;
	private WordRepository wordRepository;

	@Override
	public void add(WordTranslateEntity translate) {
		WordEntity sourceWord = translate.getSourceWord();
		WordEntity translateWord = translate.getTranslateWord();
		if (translateRepository.findOneBySourceWordValueAndSourceWordLanguageIdAndTranslateWordValueAndTranslateWordLanguageId(
				sourceWord.getValue(), sourceWord.getLanguage().getId(), 
				translateWord.getValue(), translateWord.getLanguage().getId()) != null) {
			return;
		}
		translateRepository.save(translate);
	}

	@Override
	public void delete(Long translateId) {
		WordTranslateEntity translate = translateRepository.findById(translateId).orElse(null);
		WordEntity translateWord = translate.getTranslateWord();
		translateRepository.deleteById(translateId);
		List<WordTranslateEntity> translates = findByTranslateWordId(translateWord.getId());
		if (translates.isEmpty()) {
			wordRepository.deleteById(translateWord.getId());
		}
	}

	@Override
	public List<WordTranslateEntity> findByTextFileId(Long fileId, Long userId) {
		List<WordTranslateEntity> translates = translateRepository.findBySourceWordTextFileWordsTextFileId(fileId);
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findBySourceWordIdsIdIn(List<Long> wordIds) {
		List<WordTranslateEntity> translates = translateRepository.findBySourceWordIdIn(wordIds);
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findBySourceWordValueIdIn(List<String> words) {
		List<WordTranslateEntity> translates = translateRepository.findBySourceWordValueIn(words);
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findByUserId(Long userId) {
		List<WordTranslateEntity> translates = userId != null
				? translateRepository.findBySourceWordUserWordsUserId(userId)
				: translateRepository.findAll();
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findInFolderByFolderId(Long folderId, Long userId) {
		List<Long> ids = folderService.findAllIds(folderId, userId);
		return findInFolderByFolderIds(ids);
	}

	@Override
	public List<WordTranslateEntity> findInFolderByFolderIds(List<Long> folderIds) {
		List<WordTranslateEntity> translates = null;
		if (!folderIds.isEmpty()) {
			translates = translateRepository.findBySourceWordTextFileWordsTextFileFolderIdIn(folderIds);
		} else {
			translates = translateRepository.findAll();
		}
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findBySourceWordId(Long wordId) {
		List<WordTranslateEntity> translates = translateRepository.findBySourceWordId(wordId);
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findByTranslateWordId(Long wordId) {
		List<WordTranslateEntity> translates = translateRepository.findByTranslateWordId(wordId);
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}

	@Override
	public List<WordTranslateEntity> findAll() {
		List<WordTranslateEntity> translates = translateRepository.findAll();
		EntityCleaning.clearLazyFieldsTranslate(translates);
		return translates;
	}
}
