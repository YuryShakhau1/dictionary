package by.shakhau.dictionary.service.bean;

import lombok.Getter;

@Getter
public class Value<T> {

    private final T value;

    public Value(T value) {
        this.value = value;
    }
}
