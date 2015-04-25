package com.natpryce.worktorule.issues.bitbucket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.natpryce.worktorule.IssueTracker;
import com.scurrilous.uritemplate.URITemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class BitbucketIssues implements IssueTracker {
    private static final URITemplate uriTemplate =
            new URITemplate("https://bitbucket.org/api/1.0/repositories/{owner}/{repo}/issues/{issueId}");

    private static final ImmutableSet<String> openStatuses = ImmutableSet.of(
            "new",
            "on hold"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String owner;
    private final String repo;

    public BitbucketIssues(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    @Override
    public boolean isOpen(String issueId) throws IOException {
        return openStatuses.contains(objectMapper.readTree(urlFor(issueId)).get("status").asText("unknown"));
    }

    private URL urlFor(String issueId) {
        try {
            return uriTemplate.expand(ImmutableMap.<String,Object>of(
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
