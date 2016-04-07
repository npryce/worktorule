package com.natpryce.worktorule.issues.jira;

import com.natpryce.worktorule.internal.IssueTrackerUriScheme;
import com.scurrilous.uritemplate.URITemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

class JiraIssueUriScheme implements IssueTrackerUriScheme {
    private static final URITemplate uriTemplate = new URITemplate("/rest/api/2/issue/{issueId}?fields=status");
    private final URI rootUri;

    public JiraIssueUriScheme(URI jiraServerUri) {
        this.rootUri = jiraServerUri;
    }

    @Override
    public URI uriForIssue(String issueId) {
        try {
            Map<String,Object> bindings = new HashMap<>();
            bindings.put("issueId", issueId);

            return rootUri.resolve(uriTemplate.expand(bindings));

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        }
    }
}
