package by.shakhau.dictionary.service.impl;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import by.shakhau.dictionary.service.bean.WordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.logic.util.EntityTransformer;
import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import by.shakhau.dictionary.persistence.domain.UserWordEntity;
import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.persistence.repository.UserWordRepository;
import by.shakhau.dictionary.presentation.bean.FrequencySummaryView;
import by.shakhau.dictionary.presentation.bean.TextFrequencyView;
import by.shakhau.dictionary.presentation.bean.WordFrequencyView;
import by.shakhau.dictionary.service.FolderService;
import by.shakhau.dictionary.service.TextFileWordService;
import by.shakhau.dictionary.service.TranslateService;
import by.shakhau.dictionary.service.UserWordService;
import by.shakhau.dictionary.service.UserWordStatusService;
import by.shakhau.dictionary.service.WordService;

@Service
public class UserWordServiceImpl implements UserWordService {

	@Autowired
	private UserWordRepository userWordRepository;

	@Autowired
    private TextFileWordService textFileWordService;

	@Autowired
	private UserWordStatusService userWordStatusService;
	
	@Autowired
	private TranslateService translateService;
	
	@Autowired
	private WordService wordService;
	
	@Autowired
	private FolderService folderService;

    @Override
    public List<UserWordEntity> findAll() {
        return userWordRepository.findAll();
    }

    @Override
    public List<UserWordEntity> findAll(Long userId) {
        return userWordRepository.findByUserId(userId);
    }

    @Override
	public WordInfo add(Long userId, Long wordId, Integer status) {
		WordInfo wordInfo = new WordInfo();
		WordEntity word = wordService.find(wordId);
		UserWordEntity userWord = userWordRepository.findOneByUserIdAndWordId(userId, wordId);
		if (userWord == null) {
			userWord = new UserWordEntity(word, userId);
		} else {
			wordInfo.setPrevStatus(userWord.getStatus().getStatus());
		}

		UserWordStatusEntity wordStatus = userWordStatusService.find(status);
		userWord.setStatus(wordStatus);
		userWord = userWordRepository.save(userWord);
		wordInfo.setWordId(userWord.getWord().getId());
		wordInfo.setStatus(userWord.getStatus().getStatus());
		return wordInfo;
	}

	@Override
	public void delete(Long id) {
		userWordRepository.deleteById(id);
	}

	@Override
	public void save(UserWordEntity userWord) {
		userWordRepository.save(userWord);
	}

	@Override
	public UserWordEntity findByUserIdAndWordId(Long userId, Long wordId) {
		return userWordRepository.findOneByUserIdAndWordId(userId, wordId);
	}

	@Override
	public Set<Long> findWordIdByUserIdAndWordStatus(Long userId, Integer wordStatus) {
		Set<BigInteger> wordBIds = (wordStatus != null)
			? userWordRepository.wordIdByUserIdAndStatus(userId, wordStatus)
			: userWordRepository.wordIdByUser(userId);
		Set<Long> wordIds = wordBIds.stream()
				.map(x -> x.longValue())
				.collect(Collectors.toSet());
		return wordIds;
	}

	@Override
	public List<UserWordEntity> findByUserIdAndWordStatus(Long userId, Integer wordStatus) {
        return (wordStatus != null)
			? userWordRepository.findByUserIdAndStatusStatus(userId, wordStatus)
			: userWordRepository.findByUserId(userId);
	}
	
	@Override
	public TextFrequencyView wordFrequency(
			Long userId, Long folderId, Long fileId, Integer wordStatus, Integer wordStatusFrom) {
		if (fileId != null) {
			List<WordTranslateEntity> wordTranslates = translateService.findByTextFileId(fileId, userId);
			List<WordEntity> wordsWithTranslates = EntityTransformer.translatesToWords(wordTranslates);
			List<TextFileWordEntity> textFileWords = textFileWordService.findByFileId(fileId).stream()
					.filter(fileWord -> fileWord.getWord() != null)
	    			.collect(Collectors.toList());
			
			fillTransaltes(textFileWords, wordsWithTranslates);

			return textFrequencyView(textFileWords, wordStatus, wordStatusFrom, userId);
		}
		return folderWordFrequency(folderId, wordStatus, wordStatusFrom, userId);
	}

	@Override
	public void delete(List<UserWordEntity> userWords) {
		userWordRepository.deleteAll(userWords);
	}

	@Override
	public List<UserWordEntity> findByWordId(Long wordId) {
		List<UserWordEntity> userWords = userWordRepository.findByWordId(wordId);
		EntityCleaning.clearLazyFieldsUserWord(userWords);
		return userWords;
	}

	@Override
	public void levelUpStatus(Long userId, Long wordId) {
		UserWordEntity userWord = userWordRepository.findOneByWordIdAndUserId(wordId, userId);
		Integer oldStatus = userWord.getStatus().getStatus();

		if (UserWordStatusEntity.KNOW.equals(oldStatus)) {
			return;
		}

		Integer newStatus = oldStatus + 1;
		if (newStatus > UserWordStatusEntity.ALMOST_KNOWN) {
			newStatus = UserWordStatusEntity.KNOW;
		}
		UserWordStatusEntity userWordStatus = userWordStatusService.find(newStatus);
		userWord.setStatus(userWordStatus);
		userWordRepository.save(userWord);
	}

	@Override
	public void changeStatus(Long userId, Long wordId, Integer wordStatus) {
		UserWordStatusEntity userWordStatus = userWordStatusService.find(wordStatus);

		if (userWordStatus == null) {
			return;
		}

		UserWordEntity userWord = userWordRepository.findOneByWordIdAndUserId(wordId, userId);
		userWord.setStatus(userWordStatus);
		userWordRepository.save(userWord);
	}
	
	private void fillTransaltes(List<TextFileWordEntity> textFileWords, Collection<WordEntity> wordsWithTranslates) {
		Map<Long, WordEntity> wordsWithTranslate = wordsWithTranslates.stream()
				.collect(Collectors.toMap(WordEntity::getId, word -> word));
		textFileWords.forEach(tw -> {
			WordEntity word = tw.getWord();
			WordEntity wordWithTranslate = wordsWithTranslate.get(word.getId());
			if (wordWithTranslate != null) {
				word.setTranslates(wordWithTranslate.getTranslates());
			}
		});
	}
	
	private TextFrequencyView folderWordFrequency(
			Long folderId, Integer wordStatus, Integer wordStatusFrom, Long userId) {
		List<Long> folderIds = folderService.findAllIds(folderId, userId);
		Set<WordTranslateEntity> wordTranslateSet = translateService.findInFolderByFolderIds(folderIds).stream()
				.collect(Collectors.toSet());
		List<WordEntity> wordsWithTranslateList = EntityTransformer.translatesToWords(wordTranslateSet);
		List<TextFileWordEntity> folderWords = textFileWordService.findInFolderByFolderIds(folderIds).stream()
				.filter(fileWord -> fileWord.getWord() != null)
    			.collect(Collectors.toList());

		fillTransaltes(folderWords, wordsWithTranslateList);

		return textFrequencyView(folderWords, wordStatus, wordStatusFrom, userId);
	}

	private TextFrequencyView textFrequencyView(
			List<TextFileWordEntity> textFileWords, Integer wordStatus, Integer wordStatusFrom, Long userId) {
		Set<UserWordEntity> allUserWords = userWordRepository.findByUserId(userId).stream()
				.collect(Collectors.toSet());
		Map<Long, Integer> wordStatuses = allUserWords.stream()
				.collect(Collectors.toMap(uw -> uw.getWord().getId(), uw -> uw.getStatus().getStatus()));
		List<WordEntity> allFileWords = textFileWords.stream()
				.map(TextFileWordEntity::getWord)
				.collect(Collectors.toList());

		List<WordEntity> fileWords = allFileWords.stream()
				.filter(w -> {
					Integer status = wordStatuses.get(w.getId());
					if (wordStatus == null && wordStatusFrom == null) {
						return (status == null);
					}
					if (wordStatus != null) {
						return wordStatus.equals(status);
					}
					if (status == null) {
						return false;
					}
					return status.compareTo(wordStatusFrom) >= 0;
				})
				.collect(Collectors.toList());
		
    	Map<WordEntity, Integer> wordFrequencies = new HashMap<>();
    	fileWords.forEach(
			w -> wordFrequencies.merge(w, 1, Integer::sum)
		);

    	List<WordFrequencyView> frequencies = wordFrequencyViews(wordFrequencies, wordStatuses);

    	FrequencySummaryView frequencySummary = buildSummary(new HashSet<>(allFileWords), wordStatuses);
    	TextFrequencyView frequency = new TextFrequencyView();
    	frequency.setFrequencies(frequencies);
    	frequency.setSummary(frequencySummary);
    	return frequency;
	}

	private List<WordFrequencyView> wordFrequencyViews(
			Map<WordEntity, Integer> wordFrequencies,
			Map<Long, Integer> wordStatuses) {
		return wordFrequencies.entrySet().stream()
	    		.map(x -> new WordFrequencyView(x.getKey(), x.getValue(), wordStatuses.get(x.getKey().getId())))
	    		.sorted((freq1, freq2) -> freq2.getFrequency() - freq1.getFrequency())
	    		.collect(Collectors.toList());
	}
	
	private FrequencySummaryView buildSummary(Set<WordEntity> words, Map<Long, Integer> wordStatuses) {
		FrequencySummaryView frequencySummary = new FrequencySummaryView();
		frequencySummary.setDoNotKnowSize(sizeForStatus(words, wordStatuses, UserWordStatusEntity.DO_NOT_KNOW));
		frequencySummary.setAlreadyKnowSize(sizeForStatus(words, wordStatuses, UserWordStatusEntity.ALREADY_KNOWN));
		frequencySummary.setAlmostKnowSize(sizeForStatus(words, wordStatuses, UserWordStatusEntity.ALMOST_KNOWN));
		frequencySummary.setKnowSize(sizeForStatus(words, wordStatuses, UserWordStatusEntity.KNOW));
		frequencySummary.setUnclassifiedSize(sizeForStatus(words, wordStatuses, null));
		return frequencySummary;
	}

	private long sizeForStatus(Set<WordEntity> words, Map<Long, Integer> wordStatuses, Integer status) {
		return words.stream()
			.filter(w -> {
				Integer userWordStatus = wordStatuses.get(w.getId());
				if (userWordStatus == null) {
					return status == null;
				}
				return userWordStatus.equals(status);
			})
			.count();
	}
}
