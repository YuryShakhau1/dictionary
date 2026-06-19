package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.service.ImportExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/export")
@AllArgsConstructor
public class ExportResource {

    public static final Long USER_ID = 1L;

    private ImportExportService importExportService;

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(HttpServletResponse response) {
        try {
            String result = importExportService.exportAll(USER_ID);
            response.setHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE + "charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"user-db.json\"");
            response.getWriter().write(result);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
