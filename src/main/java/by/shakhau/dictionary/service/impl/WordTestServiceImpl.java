package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.logic.util.EntityTransformer;
import by.shakhau.dictionary.persistence.domain.UserWordEntity;
import by.shakhau.dictionary.persistence.domain.UserWordStatusEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.persistence.domain.WordTranslateEntity;
import by.shakhau.dictionary.presentation.bean.AnswerOptionView;
import by.shakhau.dictionary.presentation.bean.AnswerView;
import by.shakhau.dictionary.presentation.bean.WordTestQuestionListView;
import by.shakhau.dictionary.presentation.bean.WordTestQuestionView;
import by.shakhau.dictionary.service.TranslateService;
import by.shakhau.dictionary.service.UserWordService;
import by.shakhau.dictionary.service.UserWordStatusService;
import by.shakhau.dictionary.service.WordTestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WordTestServiceImpl implements WordTestService {
	private static final Random RANDOM = new Random();
	private static final int ANSWER_COUNT = 5;

	private TranslateService translateService;
	private UserWordService userWordService;
	private UserWordStatusService userWordStatusService;

	@Override
	public WordTestQuestionView wordTestQuestion(Long folderId, Long fileId, Long userId) {
		List<WordTranslateEntity> translates = findTranslates(folderId, fileId, userId);
		return wordTestPoint(userId, translates, null);
	}

	@Override
	public WordTestQuestionListView wordTestQuestionList(
			Long userId, Long folderId, Long fileId, Integer wordStatus) {
		List<WordTranslateEntity> translates = findTranslates(folderId, fileId, userId);
        return wordTestQuestionList(userId, translates, wordStatus);
	}

	@Override
	public WordTestQuestionListView wordTestQuestionListRepeat(
			Long userId, List<WordTestQuestionView> wordTestQuestions, Integer wordStatus) {
		List<Long> wordIds = wordTestQuestions.stream()
			.map(x -> x.getWord().getId())
			.collect(Collectors.toList());
		List<WordTranslateEntity> translates = translateService.findBySourceWordIdsIdIn(wordIds);
		return wordTestQuestionList(userId, translates, wordStatus);
	}
	
	@Override
	public AnswerView answerQuestion(Long userId, WordTestQuestionView wordTestQuestion) {
		List<AnswerOptionView> answerOptions = wordTestQuestion.getAnswerOptions();
		Long wordId = wordTestQuestion.getWord().getId();
		Set<WordEntity> translates = translateService.findBySourceWordId(wordId).stream()
				.map(WordTranslateEntity::getTranslateWord)
				.collect(Collectors.toSet());
		List<WordEntity> caseWords = answerOptions.stream()
				.map(AnswerOptionView::getWord)
				.collect(Collectors.toList());
		WordEntity translateWord = findWordTranslate(translates, caseWords);

		AnswerOptionView selection = answerOptions.stream()
			.filter(answerOption -> answerOption.getSelected())
			.findFirst()
			.orElse(null);
		
		WordEntity selectedWord = selection != null ? selection.getWord() : null;
		Long answerId = selectedWord != null ? selectedWord.getId() : null;
		
		if (translateWord == null) {
			return new AnswerView(null, answerId);
		}

		if (answerId == null || !selectedWord.getId().equals(translateWord.getId())) {
			UserWordEntity userWord = userWordService.findByUserIdAndWordId(userId, wordId);
			userWord.setStatus(userWordStatusService.find(UserWordStatusEntity.DO_NOT_KNOW));
			userWordService.save(userWord);
		}

		return new AnswerView(translateWord.getId(), answerId);
	}

	@Override
	public AnswerView answerQuestionWithLevelUp(Long userId, WordTestQuestionView wordTestQuestion) {
		AnswerView answerView = answerQuestion(userId, wordTestQuestion);
		Long wordId = wordTestQuestion.getWord().getId();
		Long correctId = answerView.getCorrectId();

		if (correctId != null && correctId.equals(answerView.getAnswerId())) {
			userWordService.levelUpStatus(userId, wordId);
		} else {
			userWordService.changeStatus(userId, wordId, UserWordStatusEntity.DO_NOT_KNOW);
		}

		return answerView;
	}
	
	private WordTestQuestionListView wordTestQuestionList(
			Long userId, List<WordTranslateEntity> translates, Integer wordStatus) {
		List<UserWordEntity> userWords = userWordService.findByUserIdAndWordStatus(userId, wordStatus);
		translates = findTranslateForUserWords(translates, userWords);
		Map<WordEntity, UserWordStatusEntity> userWordStatuses = userWords.stream()
				.filter(userWord -> userWord.getStatus() != null)
				.collect(Collectors.toMap(UserWordEntity::getWord, UserWordEntity::getStatus));
		return wordTestPointList(userId, translates, userWordStatuses, wordStatus);
	}
	
	private List<WordTranslateEntity> findTranslates(Long folderId, Long fileId, Long userId) {
		if (fileId != null) {
			return translateService.findByTextFileId(fileId, userId);
		}
		if (folderId != null) {
			translateService.findInFolderByFolderId(folderId, userId);
		}
        return translateService.findByUserId(userId);
	}
	
	private WordTestQuestionView wordTestPoint(
			Long userId,
			List<WordTranslateEntity> translates, Integer status) {
		Set<WordEntity> wordTranslates = translates.stream()
				.map(WordTranslateEntity::getTranslateWord)
				.collect(Collectors.toSet());

		List<WordEntity> userWordsWithTranslate = selectUserWordsWithTranslate(userId, translates, status);
		
		WordEntity word = selectRandom(userWordsWithTranslate, RANDOM);
		WordTestQuestionView wordAnswerOptionsView
			= prepareWordTestQuestionView(wordTranslates, word);
		return wordAnswerOptionsView;
	}

	private List<WordTranslateEntity> findTranslateForUserWords(List<WordTranslateEntity> translates, List<UserWordEntity> userWords) {
		Set<WordEntity> words = userWords.stream()
			.map(userWord -> userWord.getWord())
			.collect(Collectors.toSet());
		return translates.stream()
			.filter(translate -> words.contains(translate.getSourceWord()))
			.collect(Collectors.toList());
	}
	
	private WordTestQuestionListView wordTestPointList(
			Long userId,
			List<WordTranslateEntity> translates,
			Map<WordEntity, UserWordStatusEntity> userWordStatuses,
			Integer status) {
		if (translates == null) {
			return null;
		}

		Collections.shuffle(translates, RANDOM);
		Set<WordEntity> wordTranslates = translates.stream()
				.map(WordTranslateEntity::getTranslateWord)
				.collect(Collectors.toSet());

		List<WordEntity> userWordsWithTranslate = selectUserWordsWithTranslate(userId, translates, status);
		Collections.shuffle(userWordsWithTranslate);
		
		List<WordTestQuestionView> wordTestQuestions = new ArrayList<>(userWordsWithTranslate.size());
		userWordsWithTranslate.forEach(uw -> {
			WordTestQuestionView wordAnswerOptionsView
				= prepareWordTestQuestionView(wordTranslates, uw);
			wordAnswerOptionsView.setWordStatus(userWordStatuses.get(uw).getStatus());
			wordAnswerOptionsView.getWord().setTranslates(Collections.emptyList());
			wordTestQuestions.add(wordAnswerOptionsView);
		});

		return new WordTestQuestionListView(wordTestQuestions);
	}

	private List<WordEntity> selectUserWordsWithTranslate(
			Long userId, List<WordTranslateEntity> translates, Integer wordStatus) {
		List<WordEntity> wordsWithTranslate = EntityTransformer.translatesToWords(translates);
		Set<Long> userWordIds = userWordService.findWordIdByUserIdAndWordStatus(userId, wordStatus);

		List<WordEntity> userWordsWithTranslate = wordsWithTranslate.stream()
			.filter(w -> userWordIds.contains(w.getId()))
			.distinct()
			.collect(Collectors.toList());
		return userWordsWithTranslate;
	}
	
	private WordTestQuestionView prepareWordTestQuestionView(
			Set<WordEntity> wordsWithTranslate, WordEntity word) {
		List<WordEntity> wordTranslate = word.getTranslates().stream()
				.map(WordTranslateEntity::getTranslateWord)
				.collect(Collectors.toList());

		WordEntity answerWord = selectRandom(wordTranslate, RANDOM);

		List<WordEntity> answerWords = wordsWithTranslate.stream()
				.filter(w -> !wordTranslate.contains(w))
				.collect(Collectors.toList());
		answerWords = selectShuffledList(answerWords, ANSWER_COUNT - 1, RANDOM);
		answerWords.add(answerWord);
		Collections.shuffle(answerWords);

		List<AnswerOptionView> answerOptionViews = answerWords.stream()
				.map(w -> new AnswerOptionView(w, false))
				.collect(Collectors.toList());

		WordTestQuestionView wordAnswerOptionsView = new WordTestQuestionView();
		wordAnswerOptionsView.setWord(word);
		wordAnswerOptionsView.setAnswerOptions(answerOptionViews);

		return wordAnswerOptionsView;
	}

	private <T> List<T> selectShuffledList(
			Collection<T> collection, int maxShuffleSize, Random random) {
		List<T> list = new LinkedList<>(collection);
		int listCount = list.size();
		if (maxShuffleSize > listCount) {
			maxShuffleSize = listCount;
		}
		List<T> resultList = new ArrayList<>(maxShuffleSize);
		for (int i = 0; i < maxShuffleSize; i++) {
			T element = offerRandom(list, random);
			resultList.add(element);
		}
		return resultList;
	}

	private <T> T offerRandom(List<T> list, Random random) {
		int index = random.nextInt(list.size());
		T result = list.get(index);
		list.remove(index);
		return result;
	}

	private <T> T selectRandom(List<T> list, Random random) {
		int index = random.nextInt(list.size());
		T result = list.get(index);
		return result;
	}

	private WordEntity findWordTranslate(Set<WordEntity> translates, List<WordEntity> caseWords) {
		Optional<WordEntity> optionalWord = caseWords.stream()
			.filter(caseWord -> translates.contains(caseWord))
			.findFirst();
		return optionalWord.isPresent() ? optionalWord.get() : null;
	}
}
