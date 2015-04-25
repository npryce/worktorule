package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.natpryce.worktorule.IssueTracker;
import com.scurrilous.uritemplate.URITemplate;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

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
        JsonNode jsonNode = getJsonFor(issueId);
        return jsonNode.get("state").asText("unknown").equals("open");
    }

    private JsonNode getJsonFor(String issueId) throws IOException {
        URL issueUrl = urlFor(issueId);
        HttpURLConnection cx = (HttpURLConnection)issueUrl.openConnection();
        cx.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int responseCode = cx.getResponseCode();
        if (responseCode >= 300) {
            throw new IOException("request to " + issueUrl + " failed with status " + responseCode);
        }

        Reader r = new InputStreamReader(new BufferedInputStream(cx.getInputStream()), parseCharset(cx));
        try {
            return objectMapper.readTree(r);
        }
        finally {
            r.close();
        }
    }

    private String parseCharset(HttpURLConnection cx) throws IOException {
        return parseCharset(parseContentType(cx));
    }

    private String parseCharset(MimeType contentType) {
        return Optional.ofNullable(contentType.getParameter("charset")).orElse("utf-8");
    }

    private MimeType parseContentType(HttpURLConnection cx) throws IOException {
        String contentType = cx.getContentType();
        try {
            return new MimeType(contentType);
        } catch (MimeTypeParseException e) {
            throw new IOException("failed to parse content type: " + contentType, e);
        }
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
