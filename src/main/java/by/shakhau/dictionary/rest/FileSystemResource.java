package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.logic.util.StringHelper;
import by.shakhau.dictionary.persistence.domain.FolderEntity;
import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import by.shakhau.dictionary.presentation.bean.FolderView;
import by.shakhau.dictionary.presentation.bean.TextFilesView;
import by.shakhau.dictionary.service.FolderService;
import by.shakhau.dictionary.service.LanguageService;
import by.shakhau.dictionary.service.TextFileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/fileSystem")
@AllArgsConstructor
public class FileSystemResource {

    private FolderService folderService;
    private TextFileService textFileService;
    private LanguageService languageService;
    
    @RequestMapping(
            value = "/folder/list", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody FolderView folderList(
            @RequestParam(required = false) Long folderId) {
        FolderView folderView = folderService.folderChildren(folderId, ExportResource.USER_ID);
        return folderView;
    }

    @RequestMapping(
            value = "/folder/path", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> folderPath(
            @RequestParam(required = false) Long folderId) {
        return folderService.folderPath(folderId);
    }
    
    @RequestMapping(
            value = "/folder/add", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Long addFolder(
            @RequestParam(required = false) Long parentFolderId, @RequestParam String folderName) {
        FolderEntity folder = new FolderEntity();
        folder.setName(folderName);
        folderService.addFolder(ExportResource.USER_ID, parentFolderId, folder);
        return folder.getId();
    }
    
    @RequestMapping(
            value = "/folder/rename", method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> renameFolder(
            @RequestParam Long folderId, @RequestParam String newName) {
        folderService.renameFolder(folderId, newName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/folder/delete", method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> deleteFolder(
            @RequestParam Long folderId) {
        folderService.deleteFolder(folderId, ExportResource.USER_ID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/folder/downloadWords", method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> downloadWords(
           HttpServletRequest request) {
        String folderPath = request.getServletContext().getRealPath("/WEB-INF/classes/english")
        		.replace("\\", "/");
        StringHelper.parseWords(folderPath, languageService.findLanguage("EN"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "/file/list", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody TextFilesView fileList(
            @RequestParam(required = false) Long folderId) {
        TextFilesView textFilesView = textFileService.folderFiles(ExportResource.USER_ID, folderId);
        return textFilesView;
    }
    
    @RequestMapping(
            value = "/file/upload", method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam(required = false) Long parentFolderId,
            @RequestPart MultipartFile textFile) {
        try {
            textFileService.uploadTextFile(ExportResource.USER_ID, parentFolderId, textFile.getOriginalFilename(), textFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/file/{fileId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody TextFileEntity file(
            @PathVariable Long fileId) {
    	return textFileService.findFile(fileId);
    }

    @RequestMapping(
            value = "/file/delete", method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> deleteFile(
            @RequestParam Long fileId) {
        textFileService.deleteTextFile(fileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
