package by.shakhau.dictionary.service;

import java.util.List;

import by.shakhau.dictionary.presentation.bean.AnswerView;
import by.shakhau.dictionary.presentation.bean.WordTestQuestionListView;
import by.shakhau.dictionary.presentation.bean.WordTestQuestionView;

public interface WordTestService {
	WordTestQuestionView wordTestQuestion(Long folderId, Long fileId, Long userId);
	WordTestQuestionListView wordTestQuestionList(Long userId, Long folderId, Long fileId, Integer wordStatus);
	WordTestQuestionListView wordTestQuestionListRepeat(
			Long userId, List<WordTestQuestionView> wordTestQuestions, Integer wordStatus);
	AnswerView answerQuestion(Long userId, WordTestQuestionView wordTestQuestion);
	AnswerView answerQuestionWithLevelUp(Long userId, WordTestQuestionView wordTestQuestion);
}
