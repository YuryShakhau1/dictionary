package by.shakhau.dictionary.service.bean.export;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ImportExport {

    private List<Language> languages;
    private List<Folder> folders;
    private List<TextFile> files;
    private List<Word> words;
    private List<UserWordStatus> statuses;
    private List<UserWord> userWords;
    private List<TextFileWord> textFileWords;
}
