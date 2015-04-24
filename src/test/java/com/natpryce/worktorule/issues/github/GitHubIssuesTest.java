package com.natpryce.worktorule.issues.github;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class GitHubIssuesTest {
    GitHubIssues issues = new GitHubIssues("npryce", "worktorule");

    @Test
    public void closedIssue() throws IOException {
        assertFalse(issues.isOpen("1"));
    }
}