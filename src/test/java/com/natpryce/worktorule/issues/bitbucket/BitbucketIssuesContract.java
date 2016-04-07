package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.IssueTracker;
import com.natpryce.worktorule.SkipWhenOffline;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class BitbucketIssuesContract {
    @Rule
    public TestRule skipOffline = new SkipWhenOffline();

    private IssueTracker issues;

    @Before
    public void setUp() {
        issues = createIssueTracker();
    }

    protected abstract IssueTracker createIssueTracker();

    @Test
    public void openIssue() throws IOException {
        assertTrue(issues.isOpen("1"));
    }

    @Test
    public void closedIssue() throws IOException {
        assertFalse(issues.isOpen("2"));
    }

    @Test
    public void onHoldIssue() throws IOException {
        assertTrue(issues.isOpen("3"));
    }
}
