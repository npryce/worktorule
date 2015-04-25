package com.natpryce.worktorule.issues.bitbucket;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitbucketPublicIssuesTest {
    BitbucketIssues issues = new BitbucketIssues("npryce", "worktorule-testing-public");

    @Test
    public void openIssue() throws IOException {
        assertTrue(issues.isOpen("1"));
    }

    @Test
    public void closedIssue() throws IOException {
        assertFalse(issues.isOpen("2"));
    }
}
