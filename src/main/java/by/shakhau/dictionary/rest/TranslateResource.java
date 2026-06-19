package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.service.LoadTranslateService;
import by.shakhau.dictionary.service.TranslateService;
import by.shakhau.dictionary.service.UserWordService;
import by.shakhau.dictionary.service.WordService;
import by.shakhau.dictionary.service.bean.Word;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/translate")
@AllArgsConstructor
public class TranslateResource {

	private WordService wordService;
	private UserWordService userWordService;
	private TranslateService translateService;
	private LoadTranslateService loadTranslateService;
	
	@RequestMapping(value = "/word", method = RequestMethod.GET)
	public @ResponseBody WordEntity translateWord(
			@RequestParam Long wordId) {
		WordEntity word = wordService.findWithTranslate(wordId);
		return word;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<WordEntity> translateList(
			@RequestParam(required = false) Long folderId,
			@RequestParam(required = false) Long fileId) {
		return wordService.findWithTranslate(ExportResource.USER_ID, folderId, fileId);
	}

	@RequestMapping(value = "/some-words", method = RequestMethod.GET)
	public @ResponseBody List<Word> someWords() {
		return wordService.someWords();
	}

	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public @ResponseBody List<WordEntity> translateList(
			@RequestParam(required = false) Long folderId,
			@RequestParam(required = false) Long fileId,
			@RequestParam(required = false) Integer wordStatus) {
		return userWordService.findWithUserTranslate(ExportResource.USER_ID, folderId, fileId, wordStatus);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<String> saveTranslate(
			@RequestBody WordEntity word) {
		wordService.save(word);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<String> deleteTranslate(
			@RequestParam Long translateId) {
		translateService.delete(translateId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody WordEntity translateSearch(
			@RequestParam Long wordId) {
		return loadTranslateService.downloadTranslate(wordId);
	}
}
