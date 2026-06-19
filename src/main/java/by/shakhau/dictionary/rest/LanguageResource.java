package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.presentation.bean.LanguagesView;
import by.shakhau.dictionary.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/language")
@AllArgsConstructor
public class LanguageResource {

    private LanguageService languageService;
    
    @RequestMapping(
            value = "/list", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody LanguagesView languagesList() {
        return languageService.languageList();
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addLanguage(
            @RequestParam String languageName) {
        languageService.addLanguage(languageName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> deleteLanguage(
            @RequestParam Long languageId) {
        languageService.deleteLanguage(languageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
