package com.natpryce.worktorule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotates a test (class or method) to indicate that the test is expected to fail
 * because of ongoing work that is being tracked in an issue tracker.
 *
 * Used by the {@link IgnoreInProgress} test rule.
 */
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InProgress {
    /**
     * @return The ID (or IDs) of the issue(s) under which the work is being tracked.
     */
    String[] value();

}
