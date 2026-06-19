package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.persistence.domain.WordEntity;
import by.shakhau.dictionary.service.WordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Service
@RequestMapping("/word")
@AllArgsConstructor
public class WordResource {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private WordService wordService;
	
	@RequestMapping(value = "/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void export(
			@RequestParam(required = false) Long folderId,
			@RequestParam(required = false) Long fileId,
			HttpServletResponse response) {
		List<WordEntity> words = wordService.words();
		String result = null;
		try {
			result = OBJECT_MAPPER.writeValueAsString(words);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
		try {
			response.setHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE + "charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"words.json\"");
			response.getWriter().write(result);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
