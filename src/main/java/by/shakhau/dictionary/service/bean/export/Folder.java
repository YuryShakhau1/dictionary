package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.FolderEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class Folder  {

    private Long id;
    private String name;
    private long userId;
    private Long parentFolderId;

    public Folder(FolderEntity folder) {
        this.id = folder.getId();
        this.name = new String(Base64.getEncoder().encode(folder.getName().getBytes(StandardCharsets.UTF_8)));
        this.userId = folder.getUserId();
        this.parentFolderId = folder.getParentFolder().getId();
    }
}
