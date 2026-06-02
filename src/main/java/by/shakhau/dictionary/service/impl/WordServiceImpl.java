package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.logic.util.EntityTransformer;
import by.shakhau.dictionary.persistence.domain.ExistedWordEntity;
import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.persistence.repository.WordRepository;
import by.shakhau.dictionary.service.*;
import by.shakhau.dictionary.service.bean.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static by.shakhau.dictionary.logic.util.StringHelper.recognizeLanguage;

@Service
public class WordServiceImpl implements WordService {

	private static final Set<Character> CONSONANTS = new HashSet<>(
			Arrays.asList(
					'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'
			)
	);

	@Autowired
	private ExistedWordService existedWordService;

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private TranslateService translateService;

	@Autowired
	private UserWordService userWordService;

	@Autowired
	private LanguageService languageService;

    @Override
    public List<WordEntity> findAll() {
        return wordRepository.findAll();
    }

    @Override
	public WordEntity add(String word, LanguageEntity language) {
		return add(new WordEntity(word, language));
	}

	@Override
	public WordEntity add(WordEntity word) {
		LanguageEntity language = word.getLanguage();
		Long languageId = language.getId();
		if (languageId == null) {
			language = languageService.findLanguage(language.getName());
			word.setLanguage(language);
		}

		WordEntity dbWord = wordRepository.findOneByValueAndLanguageId(word.getValue(), word.getLanguage().getId());

		if (dbWord != null) {
			return dbWord;
		}

		wordRepository.save(word);
		return word;
	}

	@Override
	public WordEntity save(WordEntity word) {
		LanguageEntity wordLanguage = word.getLanguage();
		Long wordLanguageId = wordLanguage.getId();

		if (wordLanguageId == null) {
			wordLanguage = languageService.findLanguage(wordLanguage.getName());
			word.setLanguage(wordLanguage);
		}

		List<WordTranslateEntity> wordTranslates = word.getTranslates();
		for (WordTranslateEntity translate : wordTranslates) {
			WordEntity translateWord = translate.getTranslateWord();

			if (translateWord.getValue() != null) {
				translateWord.setValue(translateWord.getValue().trim().toLowerCase());
			} else {
				continue;
			}

			if (translateWord.getValue().isEmpty()) {
				continue;
			}

			translate.setSourceWord(word);
			translate.setTranslateWord(add(translateWord));
			translateService.add(translate);
		}

		wordRepository.save(word);
		return word;
	}

	@Override
	public WordEntity findOrCreateByValuesAndLanguage(String wordValue) {
		LanguageEntity language = languageService.findLanguage(recognizeLanguage(wordValue));
		if (language == null) {
			return null;
		}

		String foundValue = null;
		if (wordValue.endsWith("s")) {
			if (wordValue.endsWith("ies")) {
				String search = wordValue.substring(0, wordValue.length() - 3) + "y";
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				}
			} else if (wordValue.endsWith("es")) {
				String search = wordValue.substring(0, wordValue.length() - 1);
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				} else {
					search = wordValue.substring(0, wordValue.length() - 2);
					word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					}
				}
			} else if (wordValue.length() > 2) {
				String search = wordValue.substring(0, wordValue.length() - 1);
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				}
			}
		} else if (wordValue.endsWith("ed")) {
			if (wordValue.endsWith("icked")) {
				String search = wordValue.substring(0, wordValue.length() - 3);
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				} else {
					search = wordValue.substring(0, wordValue.length() - 2);
					word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					}
				}
			} else if (wordValue.endsWith("ied")) {
				String search = wordValue.substring(0, wordValue.length() - 3) + "y";
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				}
			} else if (wordValue.length() > 3) {
				String search = wordValue.substring(0, wordValue.length() - 1);
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				} else {
					search = wordValue.substring(0, wordValue.length() - 2);
					word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					} else if (hasDoubleLetters(wordValue, 2)) {
						search = wordValue.substring(0, wordValue.length() - 3);
						word = existedWordService.find(search, language.getId());
						if (word != null) {
							foundValue = word.getValue();
						} else {
							search = wordValue.substring(0, wordValue.length() - 2);
							word = existedWordService.find(search, language.getId());
							if (word != null) {
								foundValue = word.getValue();
							}
						}
					}
				}
			}
		} else if (wordValue.endsWith("ing")) {
			if (wordValue.endsWith("ying")) {
				String search = wordValue.substring(0, wordValue.length() - 3);
				ExistedWordEntity word = existedWordService.find(search, language.getId());
				if (word != null) {
					foundValue = word.getValue();
				} else {
					search = wordValue.substring(0, wordValue.length() - 4) + "ie";
					word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					}
				}
			} else {
				boolean hasDoubleLetters = hasDoubleLetters(wordValue, 3);
				if (hasDoubleLetters) {
					String search = wordValue.substring(0, wordValue.length() - 4);
					ExistedWordEntity word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					} else {
						search = wordValue.substring(0, wordValue.length() - 3);
						word = existedWordService.find(search, language.getId());
						if (word != null) {
							foundValue = word.getValue();
						}
					}
				} else if (wordValue.endsWith("icking")) {
					String search = wordValue.substring(0, wordValue.length() - 4);
					ExistedWordEntity word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					} else {
						search = wordValue.substring(0, wordValue.length() - 3);
						word = existedWordService.find(search, language.getId());
						if (word != null) {
							foundValue = word.getValue();
						}
					}
				} else {
					String search = wordValue.substring(0, wordValue.length() - 3);
					ExistedWordEntity word = existedWordService.find(search, language.getId());
					if (word != null) {
						foundValue = word.getValue();
					} else {
						search = wordValue.substring(0, wordValue.length() - 3) + "e";
						word = existedWordService.find(search, language.getId());
						if (word != null) {
							foundValue = word.getValue();
						}
					}
				}
			}
		}

		WordEntity word = null;
		if (foundValue != null) {
			word = wordRepository.findOneByValueAndLanguageId(foundValue, language.getId());
			if (word == null) {
				word = new WordEntity(foundValue, language);
				word = wordRepository.save(word);
			}
			return word;
		}

		word = wordRepository.findOneByValueAndLanguageId(wordValue, language.getId());
		if (word != null) {
			return word;
		}

		ExistedWordEntity existedWord = existedWordService.find(wordValue, language.getId());
		if (existedWord != null) {
			word = new WordEntity(wordValue, language);
			word.setTranscription(existedWord.getTranscription());
			word = wordRepository.save(word);
		}

		return word;
	}

	@Override
	public WordEntity find(Long wordId) {
		WordEntity word = wordRepository.findById(wordId).orElse(null);
		EntityCleaning.clearLazyFields(word);
		return word;
	}

	@Override
	public void delete(Long wordId) {
		wordRepository.deleteById(wordId);
	}

	@Override
	public List<WordEntity> findWithTranslates() {
		List<WordTranslateEntity> wordTranslates = translateService.findAll();
		Map<WordEntity, List<WordTranslateEntity>> wordTranslateMap = wordTranslates.stream()
				.collect(Collectors.groupingBy(WordTranslateEntity::getSourceWord));
		List<WordEntity> words = new ArrayList<>(wordTranslateMap.keySet().size());
		for (Map.Entry<WordEntity, List<WordTranslateEntity>> wordTranslate : wordTranslateMap.entrySet()) {
			WordEntity word = wordTranslate.getKey();
			List<WordTranslateEntity> wTrs = wordTranslate.getValue().stream()
					.collect(Collectors.toList());
			word.setTranslates(wTrs);
			words.add(word);
		}
		return words;
	}

	@Override
	public WordEntity findWithTranslate(Long wordId) {
		List<WordEntity> words = findWithTranslate(translateService.findBySourceWordId(wordId));

		if (words.isEmpty()) {
			WordEntity word = find(wordId);
			word.setTranslates(Collections.emptyList());
			return word;
		}

		return words.iterator().next();
	}

	@Override
	public List<WordEntity> findWithTranslate(Long userId, Long folderId, Long fileId) {
		List<WordTranslateEntity> translates = null;
		if (fileId != null) {
			translates = translateService.findByTextFileId(fileId, userId);
		} else if (folderId != null) {
			translates = translateService.findInFolderByFolderId(folderId, userId);
		} else {
			translates = translateService.findByUserId(userId);
		}
		return findWithTranslate(translates);
	}

    @Override
    public List<Word> someWords() {
		List<String> someWords = Arrays.asList(
				"here",
				"that",
				"than",
				"them",
				"their",
				"then",
				"there",
				"they",
				"those",
				"thus",
				"were",
				"when",
				"where"
		);

		List<WordTranslateEntity> translates = translateService.findBySourceWordValueIdIn(someWords);
		List<WordEntity> words = findWithTranslate(translates);
		words.sort(Comparator.comparing(w -> w.getValue()));
		Collections.reverse(words);
		return words.stream()
				.map(w -> new Word(w))
				.collect(Collectors.toList());
    }

    @Override
	public List<WordEntity> findWithUserTranslate(Long userId, Long folderId, Long fileId, Integer wordStatus) {
		List<WordTranslateEntity> translates = null;
		if (fileId != null) {
			translates = translateService.findByTextFileId(fileId, userId);
		} else if (folderId != null) {
			translates = translateService.findInFolderByFolderId(folderId, userId);
		} else {
			translates = translateService.findByUserId(userId);
		}
		Set<Long> userWordIds = userWordService.findWordIdByUserIdAndWordStatus(userId, wordStatus);

		translates = translates.stream().filter(x -> userWordIds.contains(x.getSourceWord().getId()))
				.collect(Collectors.toList());
		return findWithTranslate(translates);
	}

	@Override
	public List<WordEntity> words() {
		List<WordTranslateEntity> translates = translateService.findAll();
		return findWithTranslate(translates);
	}

	private boolean hasDoubleLetters(String text, int offsetFromEnd) {
		if (text.length() < offsetFromEnd + 2) {
			return false;
		}

		char c1 = text.charAt(text.length() - offsetFromEnd - 2);
		if (!CONSONANTS.contains(c1)) {
			return false;
		}

		return c1 == text.charAt(text.length() - offsetFromEnd - 1);
	}

	private List<WordEntity> findWithTranslate(List<WordTranslateEntity> translates) {
		EntityCleaning.clearLazyFieldsTranslate(translates);
        return EntityTransformer.translatesToWords(translates);
	}
}
