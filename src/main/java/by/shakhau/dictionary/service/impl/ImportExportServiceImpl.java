package by.shakhau.dictionary.service.impl;

import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import by.shakhau.dictionary.persistence.domain.UserWordEntity;
import by.shakhau.dictionary.service.FolderService;
import by.shakhau.dictionary.service.ImportExportService;
import by.shakhau.dictionary.service.LanguageService;
import by.shakhau.dictionary.service.TextFileService;
import by.shakhau.dictionary.service.TextFileWordService;
import by.shakhau.dictionary.service.UserWordService;
import by.shakhau.dictionary.service.UserWordStatusService;
import by.shakhau.dictionary.service.WordService;
import by.shakhau.dictionary.service.bean.TranslateWord;
import by.shakhau.dictionary.service.bean.export.Folder;
import by.shakhau.dictionary.service.bean.export.ImportExport;
import by.shakhau.dictionary.service.bean.export.Language;
import by.shakhau.dictionary.service.bean.export.TextFile;
import by.shakhau.dictionary.service.bean.export.TextFileWord;
import by.shakhau.dictionary.service.bean.export.UserWord;
import by.shakhau.dictionary.service.bean.export.UserWordStatus;
import by.shakhau.dictionary.service.bean.export.Word;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImportExportServiceImpl implements ImportExportService {

    private LanguageService languageService;
    private FolderService folderService;
    private TextFileService textFileService;
    private WordService wordService;
    private UserWordStatusService userWordStatusService;
    private UserWordService userWordService;
    private TextFileWordService textFileWordService;

    public String exportAll(Long userId) {
        ObjectMapper mapper = new ObjectMapper();
        ImportExport export = new ImportExport();
        export.setLanguages(languageService.findAll().stream().map(Language::new).collect(Collectors.toList()));
        export.setFolders(folderService.findAllFolders(userId).stream().map(Folder::new).collect(Collectors.toList()));
        export.setFiles(textFileService.findAllFiles(userId).stream().map(TextFile::new).collect(Collectors.toList()));
        export.setWords(words(userId));
        export.setStatuses(userWordStatusService.findAll().stream().map(UserWordStatus::new).collect(Collectors.toList()));
        export.setUserWords(userWordService.findAll(userId).stream().map(UserWord::new).collect(Collectors.toList()));
        export.setTextFileWords(textFileWordService.findAll(userId).stream().map(TextFileWord::new).collect(Collectors.toList()));
        try {
            return mapper.writeValueAsString(export);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Word> words(Long userId) {
        List<UserWordEntity> userWords = userWordService.findAll(userId);
        List<TextFileWordEntity> textFileWords = textFileWordService.findAll(userId);
        Set<Word> wordSet = wordService.findWithTranslates().stream().map(Word::new).collect(Collectors.toSet());
        for (Word word : wordSet) {
            List<TranslateWord> translates = new ArrayList<>(new HashSet<>(word.getTranslates()));
            translates.sort(Comparator.comparing(TranslateWord::getValue));
            word.setTranslates(translates);
        }
        wordSet.addAll(userWords.stream().map(uw -> new Word(uw.getWord())).collect(Collectors.toList()));
        wordSet.addAll(textFileWords.stream()
                .filter(tfw -> tfw.getWord() != null)
                .map(tfw -> new Word(tfw.getWord())).collect(Collectors.toList()));
        List<Word> words = new ArrayList<>(wordSet);
        words.sort(Comparator.comparing(TranslateWord::getId));
        return words;
    }
}
