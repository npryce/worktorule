package com.natpryce.worktorule;

import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.InProgress;
import com.natpryce.worktorule.IssueTracker;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class IgnoreInProgressTest {
    FakeIssueTracker issueTracker = new FakeIssueTracker();
    IgnoreInProgress rule = new IgnoreInProgress(issueTracker);

    @Test
    public void runsTestWithoutWrapperIfNotAnnotated() throws Throwable {
        Statement base = new PassingTest();
        Statement testWithRuleApplied = rule.apply(base, descriptionOfTest(ClassWithNoAnnotations.class));

        assertThat(testWithRuleApplied, sameInstance(base));
    }

    @Test
    public void handlesUnannotatedTestNotAssociatedWithAClass() throws Throwable {
        Statement base = new PassingTest();
        Statement testWithRuleApplied =
                rule.apply(base, Description.createTestDescription("not-a-class", "exampleTest"));

        assertThat(testWithRuleApplied, sameInstance(base));
    }

    public static class ExampleFailure extends Throwable {}

    @Test
    public void skipsFailingTestIfAnnotatedWithOpenIssue() throws Throwable {
        issueTracker.open("issue-id");

        Statement testWithRuleApplied = rule.apply(
                new FailingTest(new ExampleFailure()),
                descriptionOfTest(ClassWithNoAnnotations.class, new InProgressAnnotation("issue-id")));

        try {
            testWithRuleApplied.evaluate();
        }
        catch (AssumptionViolatedException e) {
            // expected
        }
    }

    @Test
    public void failsTestIfAnnotatedAsInProgressButIssuesNotOpen() throws Throwable {
        Statement testWithRuleApplied = rule.apply(
                new PassingTest(),
                descriptionOfTest(ClassWithNoAnnotations.class, new InProgressAnnotation("closed-issue")));

        try {
            testWithRuleApplied.evaluate();
        }
        catch (AssertionError expected) {
            return;
        }

        Assert.fail("test should have failed");
    }

    @Test
    public void failsPassingTestIfAnnotatedWithOpenIssues() throws Throwable {
        issueTracker.open("issue-id");

        Statement testWithRuleApplied = rule.apply(
                new PassingTest(),
                descriptionOfTest(ClassWithNoAnnotations.class, new InProgressAnnotation("issue-id")));

        try {
            testWithRuleApplied.evaluate();
        }
        catch (AssertionError expected) {
            return;
        }

        Assert.fail("should have failed the test");
    }

    @Test(expected = IOException.class)
    public void failsTestWithErrorIfCommunicationWithIssueTrackerFails() throws Throwable {
        issueTracker.failure = new IOException("pop!");

        Statement testWithRuleApplied = rule.apply(
                new PassingTest(),
                descriptionOfTest(ClassWithNoAnnotations.class, new InProgressAnnotation("issue-id")));

        testWithRuleApplied.evaluate();
    }


    private Description descriptionOfTest(Class<?> testClass, Annotation ... annotations) {
        return Description.createTestDescription(testClass, "exampleTestName", annotations);
    }

    private static class ClassWithNoAnnotations {}

    public static class FailingTest extends Statement {
        public boolean wasEvaluated = false;
        private final Throwable t;

        public FailingTest(Throwable t) {
            this.t = t;
        }

        @Override
        public void evaluate() throws Throwable {
            wasEvaluated = true;
            throw t;
        }
    }

    private static class PassingTest extends Statement {
        public boolean wasEvaluated = false;

        @Override
        public void evaluate() throws Throwable {
            wasEvaluated = true;
        }
    }

    private static class FakeIssueTracker implements IssueTracker {
        public IOException failure = null;
        private final Set<String> openIssues = newHashSet();

        public void open(String ... issues) {
            openIssues.addAll(asList(issues));
        }

        @Override
        public boolean isOpen(String issueId) throws IOException {
            if (failure != null) {
                throw failure;
            }
            else {
                return openIssues.contains(issueId);
            }
        }
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private class InProgressAnnotation implements InProgress {
        private final String[] issueIds;

        public InProgressAnnotation(String ... issueIds) {
            this.issueIds = issueIds;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return InProgress.class;
        }

        @Override
        public String[] value() {
            return issueIds;
        }
    }
}
