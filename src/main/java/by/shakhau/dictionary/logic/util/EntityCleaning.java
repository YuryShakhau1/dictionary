package by.shakhau.dictionary.logic.util;

import by.shakhau.dictionary.persistence.domain.*;
import org.hibernate.collection.internal.PersistentBag;

import java.util.Collection;
import java.util.Collections;

public final class EntityCleaning {
	private EntityCleaning() {}
	
	public static void clearLazyFields(FolderEntity folder) {
		if (folder == null) {
			return;
		}
		if (folder.getFolders().getClass() == PersistentBag.class) {
			folder.setFolders(Collections.emptyList());
		}
		if (folder.getTextFiles().getClass() == PersistentBag.class) {
			folder.setTextFiles(Collections.emptyList());
		}
        FolderEntity parentFolder = folder.getParentFolder();
        if (parentFolder != null) {
        	clearLazyFields(parentFolder);
        }
    }
	
	public static void clearLazyFields(TextFileEntity textFile) {
		clearLazyFields(textFile.getFolder());
		if (textFile.getTextFileWords().getClass() == PersistentBag.class) {
			textFile.setTextFileWords(Collections.emptyList());
		}
    }

	public static void clearLazyFields(TextFileWordEntity fileWord) {
		WordEntity word = fileWord.getWord();
		if (word != null) {
			clearLazyFields(word);
		}
		clearLazyFields(fileWord.getTextFile());
	}

	public static void clearLazyFields(UserWordEntity userWord) {
		clearLazyFields(userWord.getStatus());
		clearLazyFields(userWord.getWord());
	}

	public static void clearLazyFields(WordEntity word) {
		if (word.getTranslates().getClass() == PersistentBag.class) {
			word.setTranslates(Collections.emptyList());
		}
		if (word.getTextFileWords().getClass() == PersistentBag.class) {
			word.setTextFileWords(Collections.emptyList());
		}
		if (word.getUserWords().getClass() == PersistentBag.class) {
			word.setUserWords(Collections.emptyList());
		}
	}

	public static void clearLazyFields(UserWordStatusEntity userWordStatus) {
		if (userWordStatus.getUserWords().getClass() == PersistentBag.class) {
			userWordStatus.setUserWords(Collections.emptyList());
		}
	}

	public static void clearLazyFields(WordTranslateEntity translate) {
		clearLazyFields(translate.getSourceWord());
		clearLazyFields(translate.getTranslateWord());
	}

	public static void clearLazyFieldsTranslate(Collection<WordTranslateEntity> translates) {
		translates.forEach(translate -> clearLazyFields(translate));
	}

	public static void clearLazyFieldsWord(Collection<WordEntity> words) {
		words.forEach(word -> clearLazyFields(word));
	}

	public static void clearLazyFieldsUserWord(Collection<UserWordEntity> userWords) {
		userWords.forEach(userWord -> clearLazyFields(userWord));
	}

	public static void clearLazyFieldsTextFileWord(Collection<TextFileWordEntity> fileWords) {
		fileWords.forEach(fileWord -> clearLazyFields(fileWord));
	}

	public static void clearLazyFieldsTextFile(Collection<TextFileEntity> textFiles) {
		textFiles.forEach(textFile -> clearLazyFields(textFile));
    }
	
	public static void clearLazyFieldsFolder(Collection<FolderEntity> folders) {
		folders.forEach(folder -> clearLazyFields(folder));
    }
}
