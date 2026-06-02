package by.shakhau.dictionary.persistence.domain;

import java.io.Serializable;

public interface Entity<T> extends Serializable {

    T getId();
}
