package com.natpryce.worktorule.internal;

import com.google.common.collect.ImmutableMap;
import com.scurrilous.uritemplate.URITemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class ProjectHostingServiceUriScheme implements IssueTrackerUriScheme {
    private final URITemplate uriTemplate;
    private final String owner;
    private final String repo;

    public ProjectHostingServiceUriScheme(String template, String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        uriTemplate = new URITemplate(template);
    }

    @Override
    public URI uriForIssue(String issueId) {
        try {
            return uriTemplate.expand(ImmutableMap.<String, Object>of(
                    "owner", owner,
                    "repo", repo,
                    "issueId", issueId));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        }
    }
}
