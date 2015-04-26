package com.natpryce.worktorule.internal;

import com.google.common.collect.ImmutableMap;
import com.scurrilous.uritemplate.URITemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ProjectHostingServiceUrlScheme implements IssueTrackerUrlScheme {
    private final URITemplate uriTemplate;
    private final String owner;
    private final String repo;

    public ProjectHostingServiceUrlScheme(String template, String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        uriTemplate = new URITemplate(template);
    }

    @Override
    public URL urlOfIssue(String issueId) {
        try {
            return uriTemplate.expand(ImmutableMap.<String, Object>of(
                    "owner", owner,
                    "repo", repo,
                    "issueId", issueId)).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URL syntax", e);
        }
    }
}
