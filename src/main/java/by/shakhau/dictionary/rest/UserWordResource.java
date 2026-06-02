package by.shakhau.dictionary.rest;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import by.shakhau.dictionary.logic.util.StringHelper;
import by.shakhau.dictionary.persistence.domain.*;
import by.shakhau.dictionary.persistence.repository.ExistedWordRepository;
import by.shakhau.dictionary.service.LanguageService;
import by.shakhau.dictionary.service.bean.WordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import by.shakhau.dictionary.presentation.bean.TextFrequencyView;
import by.shakhau.dictionary.presentation.bean.UserWordsView;
import by.shakhau.dictionary.service.UserWordService;

@Service
@RequestMapping("/userWord")
public class UserWordResource {

	@Autowired
	private UserWordService userWordService;
    @Autowired
    private ExistedWordRepository existedWordRepository;

	@GetMapping(value = "/list")
	public @ResponseBody UserWordsView wordList() {
		List<UserWordEntity> userWords = userWordService.findByUserIdAndWordStatus(ExportResource.USER_ID, null);
		Predicate<UserWordEntity> p = (e) -> {return e.getStatus() == null;};
		userWords.stream().anyMatch(p);
		return new UserWordsView(userWords);
	}

	Set<String> wordValues = new HashSet<>();

	@RequestMapping(
    		value = "/frequency", method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody TextFrequencyView fileWordFrequency(
    		@RequestParam(required = false) Long folderId,
    		@RequestParam(required = false) Long fileId,
			@RequestParam(required = false) Integer wordStatus,
    		@RequestParam(required = false) Integer wordStatusFrom) {
		return userWordService.wordFrequency(ExportResource.USER_ID, folderId, fileId, wordStatus, wordStatusFrom);
    }

	@RequestMapping(value = "/add/{wordId}/status/{status}", method = RequestMethod.POST)
	public ResponseEntity<WordInfo> addWord(
			@PathVariable Long wordId,
			@PathVariable Integer status) {
		return new ResponseEntity<>(userWordService.add(ExportResource.USER_ID, wordId, status), HttpStatus.OK);
	}

	@RequestMapping(value = "/word/{wordId}/change/status", method = RequestMethod.POST)
	public ResponseEntity<String> changeStatus(
			@PathVariable Long wordId,
			@RequestParam Integer statusValue) {
		userWordService.changeStatus(ExportResource.USER_ID, wordId, statusValue);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete/{userWordId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteWord(
			@PathVariable Long userWordId) {
		userWordService.delete(userWordId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
