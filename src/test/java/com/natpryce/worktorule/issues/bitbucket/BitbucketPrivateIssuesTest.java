package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.InProgress;
import com.natpryce.worktorule.WorkToRuleIssueTracker;
import com.natpryce.worktorule.issues.github.GitHubIssues;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitbucketPrivateIssuesTest {
    @Rule
    public TestRule workInProgress = new IgnoreInProgress(WorkToRuleIssueTracker.PUBLIC);

    BitbucketIssues issues = new BitbucketIssues("npryce", "worktorule-testing-private");

    @Test
    @InProgress("2")
    public void openIssue() throws IOException {
        assertTrue(issues.isOpen("1"));
    }

    @Test
    @InProgress("2")
    public void closedIssue() throws IOException {
        assertFalse(issues.isOpen("2"));
    }
}
