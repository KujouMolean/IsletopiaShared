package com.molean.isletopia.shared;

import com.molean.isletopia.shared.pojo.WrappedMessageObject;

public interface MessageHandler<T> {
    void handle(WrappedMessageObject wrappedMessageObject, T message);
}
