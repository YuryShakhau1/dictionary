package by.shakhau.dictionary.logic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class FileHelper {

	private FileHelper() {}
	
	public static List<String> folderFiles(String folderPath) {
		File folder = new File(folderPath);
		File[] fileList = folder.listFiles();
		List<String> files = new ArrayList<>(fileList.length);
		for (File file : fileList) {
			files.add(file.getAbsolutePath());
		}
		return files;
	}
	
	public static String fileContent(String path, String encoding) {
		InputStream is = null;
		String fileContent = null;
		try {
			is = new FileInputStream(path);
			fileContent = inputStreamContent(is, encoding);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileContent;
	}
	
	public static String inputStreamContent(InputStream inputStream, String encoding) {
        StringBuilder sb = new StringBuilder();
        Reader reader = null;
        int buffSize = 8196;
        char[] buff = new char[buffSize];
        try {
            reader = new InputStreamReader(inputStream, Charset.forName(encoding));
            String streamLine = null;
            boolean firstTime = true;
            int byteRead = 0;
            while ((byteRead = reader.read(buff, 0, buffSize)) >= 0) {
                streamLine = new String(buff, 0, byteRead);
                if (firstTime && streamLine.startsWith("\uFEFF")) {
                    streamLine = streamLine.substring(1);
                    firstTime = false;
                }
                sb.append(streamLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
