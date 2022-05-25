package com.molean.isletopia.shared.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Singleton
public @interface BeanHandlerPriority {
    @NotNull int value() default 0;
}
