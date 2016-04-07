package com.natpryce.worktorule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A JUnit test rule that skips failing tests if they are annotated as {@link InProgress}.
 *
 * The rule fails tests if they pass but are still annotated as {@link InProgress}, or
 * are related only to closed issues.
 */
@SuppressWarnings("UnusedDeclaration")
public class IgnoreInProgress implements TestRule {
    private final IssueTracker issueTracker;
    private final Function<Description, Set<String>> associatedIssues;

    public IgnoreInProgress(IssueTracker issueTracker) {
        this(issueTracker, (description)-> emptySet());
    }

    public IgnoreInProgress(IssueTracker issueTracker, Function<Description,Set<String>> associatedIssues) {
        this.issueTracker = issueTracker;
        this.associatedIssues = associatedIssues;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        final Set<String> issueIds = issueIdsForTest(description);

        if (issueIds.isEmpty()) {
            return base;
        } else {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    Set<String> openIssueIds = filterOpen(issueIds);
                    assertTrue("test annotated as InProgress, but no open issues found", !openIssueIds.isEmpty());

                    try {
                        base.evaluate();
                    } catch (org.junit.internal.AssumptionViolatedException e) {
                        throw e;
                    } catch (Throwable t) {
                        throw new org.junit.AssumptionViolatedException("known issue", t);
                    }

                    fail("test passed when annotated as in progress");
                }
            };
        }
    }

    private Set<String> filterOpen(Set<String> issueIds) throws IOException {
        Set<String> open = new TreeSet<>();
        for (String issueId : issueIds) {
            if (issueTracker.isOpen(issueId)) {
                open.add(issueId);
            }
        }
        return open;
    }

    private Set<String> issueIdsForTest(Description description) {
        Set<String> issueIds = issueIdsFrom(description.getAnnotation(InProgress.class));

        Class<?> c = description.getTestClass();
        while (c != null) {
            issueIds.addAll(issueIdsFrom(c.getAnnotation(InProgress.class)));
            c = c.getSuperclass();
        }

        issueIds.addAll(associatedIssues.apply(description));

        return issueIds;
    }

    private Set<String> issueIdsFrom(@Nullable InProgress annotation) {
        return Optional.ofNullable(annotation)
                .map(InProgress::value)
                .map(ids -> setOf(ids))
                .orElse(emptySet());
    }

    private static Set<String> setOf(String[] elements) {
        return new TreeSet<>(asList(elements));
    }
}
