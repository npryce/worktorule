package com.natpryce.worktorule.issues.jira;

import com.google.common.collect.ImmutableMap;
import com.natpryce.worktorule.internal.IssueTrackerUriScheme;
import com.scurrilous.uritemplate.URITemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class JiraIssueUriScheme implements IssueTrackerUriScheme {
    private static final URITemplate uriTemplate = new URITemplate("/rest/api/2/issue/{issueId}?fields=status");
    private final URI rootUri;

    public JiraIssueUriScheme(URI jiraServerUri) {
        this.rootUri = jiraServerUri;
    }

    @Override
    public URI uriForIssue(String issueId) {
        try {
            return rootUri.resolve(uriTemplate.expand(ImmutableMap.<String, Object>of("issueId", issueId)));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        }
    }
}
