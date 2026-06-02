package by.shakhau.dictionary.logic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class HttpLoader {

    public static StringBuilder loadHttp(String urlStr) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("user-agent",
            		"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36");
            urlConnection.setRequestProperty("content-type", "text/html; charset=utf-8");
            urlConnection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            urlConnection.setRequestProperty("accept-encoding", "gzip, deflate, sdch");
            urlConnection.setRequestProperty("accept-language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
            if ("gzip".equals(urlConnection.getContentEncoding())) {
            	reader = new BufferedReader(new InputStreamReader(
            			new GZIPInputStream(urlConnection.getInputStream()), "UTF-8"));
            } else {
	            reader = new BufferedReader(new InputStreamReader(
	            		urlConnection.getInputStream(), "UTF-8"));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return sb;
    }
}
