package com.natpryce.worktorule.issues.jira;

import com.google.common.collect.ImmutableMap;
import com.natpryce.worktorule.internal.IssueTrackerUrlScheme;
import com.scurrilous.uritemplate.URITemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JiraIssueUrlScheme implements IssueTrackerUrlScheme {
    private static final URITemplate uriTemplate = new URITemplate("/rest/api/2/issue/{issueId}?fields=status");
    private final URI rootUri;

    public JiraIssueUrlScheme(URI jiraServerUri) {
        this.rootUri = jiraServerUri;
    }

    @Override
    public URL urlOfIssue(String issueId) {
        try {
            return rootUri.resolve(uriTemplate.expand(ImmutableMap.<String,Object>of("issueId", issueId))).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        }
    }
}
