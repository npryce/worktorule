package com.natpryce.worktorule.internal;

import com.scurrilous.uritemplate.URITemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
            Map<String, Object> bindings = new HashMap<>();
            bindings.put("owner", owner);
            bindings.put("repo", repo);
            bindings.put("issueId", issueId);

            return uriTemplate.expand(bindings);

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        }
    }
}
