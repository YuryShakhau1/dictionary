package by.shakhau.dictionary.persistence.domain;

import javax.persistence.*;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "WordGroupItem")
public class WordGroupItemEntity implements Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wordGroupId")
    private WordGroupEntity wordGroup;

    @ManyToOne
    @JoinColumn(name = "wordId")
    private WordEntity word;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WordGroupEntity getWordGroup() {
        return wordGroup;
    }

    public void setWordGroup(WordGroupEntity wordGroup) {
        this.wordGroup = wordGroup;
    }

    public WordEntity getWord() {
        return word;
    }

    public void setWord(WordEntity word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordGroupItemEntity that = (WordGroupItemEntity) o;
        return Objects.equals(wordGroup, that.wordGroup) && Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordGroup, word);
    }
}
