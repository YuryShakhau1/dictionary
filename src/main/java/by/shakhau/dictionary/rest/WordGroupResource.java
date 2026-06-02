package by.shakhau.dictionary.rest;

import by.shakhau.dictionary.persistence.domain.WordGroupEntity;
import by.shakhau.dictionary.persistence.domain.WordGroupItemEntity;
import by.shakhau.dictionary.service.WordGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/word-group")
public class WordGroupResource {

    @Autowired
    private WordGroupService wordGroupService;

    @GetMapping("/list")
    public List<WordGroupEntity> wordGroups() {
        return wordGroupService.findByUserId(ExportResource.USER_ID);
    }

    @PostMapping("/list")
    public List<WordGroupEntity> createGroup() {
        return wordGroupService.findByUserId(ExportResource.USER_ID);
    }

    @GetMapping("/items/{wordGroupId}")
    public List<WordGroupItemEntity> wordGroupItems(
            @PathVariable Long wordGroupId) {
        return wordGroupService.findWordGroupItems(wordGroupId);
    }
}
