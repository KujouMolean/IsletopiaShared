package com.molean.isletopia.shared.utils;

import java.util.Objects;

public interface TeConsumer<T,O,K> {
    void accept(T t, O o, K k);

    default TeConsumer<T, O, K> andThen(TeConsumer<? super T, ? super O, ? super K> after) {
        Objects.requireNonNull(after);
        return (l, o, k) -> {
            accept(l, o, k);
            after.accept(l, o, k);
        };
    }
}
