package com.natpryce.worktorule;

import com.google.common.base.Function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InProgress {
    String[] value();

    Function<InProgress, String[]> ids = new Function<InProgress, String[]>() {
        @Override
        public String[] apply(InProgress input) {
            return input.value();
        }
    };
}
