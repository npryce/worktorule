package com.natpryce.worktorule;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.union;
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

    public IgnoreInProgress(IssueTracker issueTracker) {
        this.issueTracker = issueTracker;
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
        Set<String> open = newHashSet();
        for (String issueId : issueIds) {
            if (issueTracker.isOpen(issueId)) {
                open.add(issueId);
            }
        }
        return open;
    }

    private Set<String> issueIdsForTest(Description description) {
        Set<String> issueIds = ids(description.getAnnotation(InProgress.class));

        Class<?> c = description.getTestClass();
        while (c != null) {
            issueIds = union(issueIds, ids(c.getAnnotation(InProgress.class)));
            c = c.getSuperclass();
        }

        return issueIds;
    }

    private Set<String> ids(@Nullable InProgress annotation) {
        return ImmutableSet.copyOf(Optional.fromNullable(annotation)
                .transform(toIds)
                .or(new String[0]));
    }

    private static final Function<InProgress, String[]> toIds = new Function<InProgress, String[]>() {
        @Override
        public String[] apply(InProgress input) {
            return input.value();
        }
    };
}
