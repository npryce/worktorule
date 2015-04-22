package com.natpryce.worktorule;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.sun.istack.internal.Nullable;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.util.Set;

import static com.google.common.collect.Sets.union;

public class IgnoreInProgress implements TestRule {
    private final IssueTracker issueTracker;

    public IgnoreInProgress(IssueTracker issueTracker) {
        this.issueTracker = issueTracker;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        final Set<String> issueIds = inProgressIssueIdsForTest(description);

        if (issueIds.isEmpty()) {
            return base;
        }
        else {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                    }
                    catch (Throwable t) {
                        if (isKnownIssue()) {
                            throw new AssumptionViolatedException("known issue", t);
                        }
                        else {
                            throw t;
                        }
                    }

                    if (isKnownIssue()) {
                        Assert.fail("test passed when annotated as in progress");
                    }
                }

                private boolean isKnownIssue() throws IOException {
                    for (String issueId : issueIds) {
                        if (issueTracker.isOpen(issueId)) {
                            return true;
                        }
                    }
                    return false;
                }
            };
        }
    }

    private Set<String> inProgressIssueIdsForTest(Description description) {
        Set<String> issueIds = ids(description.getAnnotation(InProgress.class));

        Class<?> c = description.getTestClass();
        while (c != null) {
            issueIds = union(issueIds, ids(c.getAnnotation(InProgress.class)));
            c = c.getSuperclass();
        }

        return issueIds;
    }

    Set<String> ids(@Nullable InProgress annotation) {
        return ImmutableSet.copyOf(Optional.fromNullable(annotation)
                .transform(InProgress.ids)
                .or(new String[0]));
    }
}
