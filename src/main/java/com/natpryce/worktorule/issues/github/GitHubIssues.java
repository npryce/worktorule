package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.natpryce.worktorule.IssueTracker;
import com.scurrilous.uritemplate.URITemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class GitHubIssues implements IssueTracker {
    private static final URITemplate uriTemplate =
            new URITemplate("https://api.github.com/repos/{owner}/{repo}/issues/{issueId}");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String owner;
    private final String repo;

    public GitHubIssues(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    @Override
    public boolean isOpen(String issueId) throws IOException {
        return Objects.equals(objectMapper.readTree(urlFor(issueId)).get("state").asText("unknown"), "open");
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
