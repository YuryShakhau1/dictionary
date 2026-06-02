package by.shakhau.dictionary.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import by.shakhau.dictionary.presentation.bean.AnswerView;
import by.shakhau.dictionary.presentation.bean.WordTestQuestionListView;
import by.shakhau.dictionary.presentation.bean.WordTestQuestionView;
import by.shakhau.dictionary.service.WordTestService;

@RestController
@RequestMapping("/word/test")
public class WordTestResource {

	@Autowired
	private WordTestService wordTestService;

	@RequestMapping(value = "/question", method = RequestMethod.GET)
	public @ResponseBody WordTestQuestionView questionOptions(
			@RequestParam(required = false) Long folderId,
			@RequestParam(required = false) Long fileId) {
		return wordTestService.wordTestQuestion(folderId, fileId, ExportResource.USER_ID);
	}

	@RequestMapping(value = "/question/list", method = RequestMethod.GET)
	public @ResponseBody WordTestQuestionListView questionOptionsList(
			@RequestParam(required = false) Long folderId,
			@RequestParam(required = false) Long fileId,
			@RequestParam(required = false) Integer status) {
		return wordTestService.wordTestQuestionList(ExportResource.USER_ID, folderId, fileId, status);
	}

	@RequestMapping(value = "/question/list/repeat", method = RequestMethod.POST)
	public @ResponseBody WordTestQuestionListView questionOptionsListRepeat(
			@RequestBody List<WordTestQuestionView> wordTestQuestions) {
		return wordTestService.wordTestQuestionListRepeat(ExportResource.USER_ID, wordTestQuestions, null);
	}
	
	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public @ResponseBody AnswerView answer(
			@RequestBody WordTestQuestionView wordTestQuestion) {
		return wordTestService.answerQuestion(ExportResource.USER_ID, wordTestQuestion);
	}

	@RequestMapping(value = "/answer/level/up", method = RequestMethod.POST)
	public @ResponseBody AnswerView answerWithLevelUp(
			@RequestBody WordTestQuestionView wordTestQuestion) {
		return wordTestService.answerQuestionWithLevelUp(ExportResource.USER_ID, wordTestQuestion);
	}
}
