package by.shakhau.dictionary.logic.util;

import by.shakhau.dictionary.persistence.domain.LanguageEntity;
import by.shakhau.dictionary.persistence.domain.WordEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringHelper {

	private StringHelper() {}
	
	public static List<WordEntity> parseWords(String folderPath, LanguageEntity language) {
		List<WordEntity> resultWords = new ArrayList<>();
		File folder = new File(folderPath);
		File[] filse = folder.listFiles();
		List<String> stringWords = new ArrayList<>();
		for (File file : filse) {
			stringWords.addAll(parseFile(file));
		}
		for (String stringWord : stringWords) {
			resultWords.add(new WordEntity(stringWord, language));
		}
		return resultWords;
	}

	private static List<String> parseFile(File file) {
		List<String> words = new ArrayList<>();
		String path = file.getAbsolutePath();
		if (path.endsWith("_rus.txt")) {
			return Collections.emptyList();
		}
		String content = FileHelper.fileContent(path, "UTF-8").toLowerCase();
		String[] lines = content.split("\n");
		for (String l : lines) {
			String line = l.trim();
			String[] stringWords = line.split(" ");
			String word = findWord(stringWords);
			if (word == null) {
				continue;
			}
			words.add(word);
		}
		return words;
	}
	
	private static String findWord(String[] stringWords) {
		List<String> wordList = Arrays.asList(stringWords);
		int index = -1;
		for (int i = 0; i < wordList.size(); i++) {
			String word = wordList.get(i);
			if (word.contains("[")
					|| "I".equals(word)
					|| "=".equals(word)
					|| word.startsWith("1")) {
				index = i;
				break;
			}
		}
		String resultString = null;
		for (int i = 0; i < index; i++) {
			if (!LanguageEntity.EN.equals(recognizeLanguage(wordList.get(i)))) {
				index = i;
				break;
			}
		}
		if (index > 0) {
			resultString = String.join(" ", wordList.subList(0, index));
		}
		return resultString;
	}
	
	public static String recognizeLanguage(String text) {
		if (!isWord(text)) {
			return null;
		}

		int latinCount = 0;
		int kirilicCount = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')) {
				latinCount++;
			} else if ((c >= 'А' && c <= 'Я')
					|| (c >= 'а' && c <= 'я')
					|| c == 'ё') {
				kirilicCount++;
			}
		}
		if (latinCount > kirilicCount) {
			return LanguageEntity.EN;
		}
		if (latinCount < kirilicCount) {
			return LanguageEntity.RU;
		}
        return null;
	}

	private static boolean isWord(String text) {
		if (text.isEmpty()) {
			return false;
		}

		char begin = text.charAt(0);
		char end = text.charAt(text.length() - 1);
		return charIsValid(begin) && charIsValid(end);
	}

	private static boolean charIsValid(char c) {
		return ((c >= 'a' && c <= 'z')
				|| (c >= 'A' && c <= 'Z')
				|| (c >= 'А' && c <= 'Я')
				|| (c >= 'а' && c <= 'я')
				|| c == 'ё');
	}
}
