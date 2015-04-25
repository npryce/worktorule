package com.natpryce.worktorule.issues.bitbucket;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class BitbucketIssuesContract {
    private final BitbucketIssues issues;

    public BitbucketIssuesContract(final BitbucketIssues issues) {
        this.issues = issues;
    }

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
