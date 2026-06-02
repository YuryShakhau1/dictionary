package by.shakhau.dictionary.service.bean.export;

import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor
@Getter
@Setter
public class TextFile {

    private Long id;
    private String name;
    private String value;
    private Long folderId;
    private Long userId;

	public TextFile(TextFileEntity textFile) {
		this.id = textFile.getId();
		this.name = new String(Base64.getEncoder().encode(textFile.getName().getBytes(StandardCharsets.UTF_8)));
		this.value = new String(Base64.getEncoder().encode(textFile.getValue().getBytes(StandardCharsets.UTF_8)));
		if (textFile.getFolder() != null) {
			this.folderId = textFile.getFolder().getId();
		}
		this.userId = textFile.getUserId();
	}
}
