package com.molean.isletopia.shared.utils;

import java.util.Objects;

public interface QuConsumer<T, O, K, V> {
    void accept(T t, O o, K k, V v);

    default QuConsumer<T, O, K, V> andThen(QuConsumer<? super T, ? super O, ? super K, ? super V> after) {
        Objects.requireNonNull(after);
        return (l, o, k, v) -> {
            accept(l, o, k, v);
            after.accept(l, o, k, v);
        };
    }
}