package com.natpryce.worktorule.issues.jira;

import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class JiraIssueUrlSchemeTest {
    @Test
    public void expandsApiUrlsForGivenJiraInstance() throws Exception {
        JiraIssueUriScheme jira = new JiraIssueUriScheme(URI.create("https://jira.example.com"));

        assertThat(jira.uriForIssue("EXAMPLE-999"),
                equalTo(URI.create("https://jira.example.com/rest/api/2/issue/EXAMPLE-999?fields=status")));
    }

    @Test
    public void expandsApiWithExplicitPort() throws Exception {
        JiraIssueUriScheme jira = new JiraIssueUriScheme(URI.create("https://jira.example.com:8443"));

        assertThat(jira.uriForIssue("EXAMPLE-999"),
                equalTo(URI.create("https://jira.example.com:8443/rest/api/2/issue/EXAMPLE-999?fields=status")));
    }
}
