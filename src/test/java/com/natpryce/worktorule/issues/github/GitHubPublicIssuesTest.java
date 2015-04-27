package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GitHubPublicIssuesTest {
    GitHubIssues issues = new GitHubIssues("npryce", "worktorule-testing");

    @Before
    public void checkRateLimit() throws IOException {
        JsonNode rateLimitNode = new ObjectMapper().readTree(new URL("https://api.github.com/rate_limit"));
        int requestsRemaining = rateLimitNode.findPath("resources").findPath("core").findPath("remaining").intValue();

        if (requestsRemaining <= 0) {
            throw new AssumptionViolatedException("API rate limit exceeded");
        }
    }

    @Test
    public void openIssue() throws IOException {
        assertTrue(issues.isOpen("1"));
    }

    @Test
    public void closedIssue() throws IOException {
        assertFalse(issues.isOpen("2"));
    }
}
