package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.persistence.domain.ExistedWordEntity;
import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.repository.ExistedWordRepository;
import by.shakhau.dictionary.persistence.repository.WordRepository;
import by.shakhau.dictionary.service.ExistedWordService;
import by.shakhau.dictionary.service.LanguageService;
import by.shakhau.dictionary.service.bean.Value;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static by.shakhau.dictionary.logic.util.StringHelper.recognizeLanguage;

@Service
@AllArgsConstructor
public class ExistedWordServiceImpl implements ExistedWordService {

    private static final Set<Character> CONSONANTS = new HashSet<>(
            Arrays.asList(
                    'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'
            )
    );

    private Map<String, Value<ExistedWordEntity>> cache = new HashMap<>();

    private ExistedWordRepository existedWordRepository;
    private WordRepository wordRepository;
    private LanguageService languageService;

    @Override
    public List<ExistedWordEntity> findAll() {
        return existedWordRepository.findAll();
    }

    @Override
    public ExistedWordEntity find(String value, Long languageId) {
        return cache.computeIfAbsent(value, k -> new Value<>(existedWordRepository.findOneByValueAndLanguageId(value, languageId))).getValue();
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
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                }
            } else if (wordValue.endsWith("es")) {
                String search = wordValue.substring(0, wordValue.length() - 1);
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                } else {
                    search = wordValue.substring(0, wordValue.length() - 2);
                    word = find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    }
                }
            } else if (wordValue.length() > 2) {
                String search = wordValue.substring(0, wordValue.length() - 1);
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                }
            }
        } else if (wordValue.endsWith("ed")) {
            if (wordValue.endsWith("icked")) {
                String search = wordValue.substring(0, wordValue.length() - 3);
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                } else {
                    search = wordValue.substring(0, wordValue.length() - 2);
                    word = find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    }
                }
            } else if (wordValue.endsWith("ied")) {
                String search = wordValue.substring(0, wordValue.length() - 3) + "y";
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                }
            } else if (wordValue.length() > 3) {
                String search = wordValue.substring(0, wordValue.length() - 1);
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                } else {
                    search = wordValue.substring(0, wordValue.length() - 2);
                    word = find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    } else if (hasDoubleLetters(wordValue, 2)) {
                        search = wordValue.substring(0, wordValue.length() - 3);
                        word = find(search, language.getId());
                        if (word != null) {
                            foundValue = word.getValue();
                        } else {
                            search = wordValue.substring(0, wordValue.length() - 2);
                            word = find(search, language.getId());
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
                ExistedWordEntity word = find(search, language.getId());
                if (word != null) {
                    foundValue = word.getValue();
                } else {
                    search = wordValue.substring(0, wordValue.length() - 4) + "ie";
                    word = find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    }
                }
            } else {
                boolean hasDoubleLetters = hasDoubleLetters(wordValue, 3);
                if (hasDoubleLetters) {
                    String search = wordValue.substring(0, wordValue.length() - 4);
                    ExistedWordEntity word = find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    } else {
                        search = wordValue.substring(0, wordValue.length() - 3);
                        word = find(search, language.getId());
                        if (word != null) {
                            foundValue = word.getValue();
                        }
                    }
                } else if (wordValue.endsWith("icking")) {
                    String search = wordValue.substring(0, wordValue.length() - 4);
                    ExistedWordEntity word = find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    } else {
                        search = wordValue.substring(0, wordValue.length() - 3);
                        word = find(search, language.getId());
                        if (word != null) {
                            foundValue = word.getValue();
                        }
                    }
                } else {
                    String search = wordValue.substring(0, wordValue.length() - 3);
                    ExistedWordEntity word =find(search, language.getId());
                    if (word != null) {
                        foundValue = word.getValue();
                    } else {
                        search = wordValue.substring(0, wordValue.length() - 3) + "e";
                        word = find(search, language.getId());
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

        ExistedWordEntity existedWord = find(wordValue, language.getId());
        if (existedWord != null) {
            word = new WordEntity(wordValue, language);
            word.setTranscription(existedWord.getTranscription());
            word = wordRepository.save(word);
        }

        return word;
    }
    
    @Override
    public void resetCache() {
        cache = new HashMap<>();
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
}
