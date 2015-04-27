package com.natpryce.worktorule.issues.jira;

import com.natpryce.worktorule.BuildEnvironment;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JiraWithBasicAuthorizationTest {
    Jira issues = new Jira("http://jira.nap",
            BuildEnvironment.authFromEnvvars("JIRA_USERNAME", "JIRA_PASSWORD"));

    @Test
    public void openIssue() throws IOException {
        assertTrue(issues.isOpen(BuildEnvironment.getenv("JIRA_OPEN_ISSUE_ID")));
    }

    @Test
    public void closedIssue() throws IOException {
        assertFalse(issues.isOpen(BuildEnvironment.getenv("JIRA_CLOSED_ISSUE_ID")));
    }
}
