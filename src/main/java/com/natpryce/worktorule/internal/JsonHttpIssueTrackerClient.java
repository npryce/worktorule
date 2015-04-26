package com.natpryce.worktorule.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.natpryce.worktorule.IssueTracker;
import com.natpryce.worktorule.http.HttpConnectionSetting;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpIssueTrackerClient implements IssueTracker {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String acceptedContentType;
    private final IssueTrackerUrlScheme urlScheme;
    private final IssueJsonPredicate isOpenPredicate;
    private final HttpConnectionSetting[] connectionSettings;

    public JsonHttpIssueTrackerClient(IssueTrackerUrlScheme urlScheme, String acceptedContentType, IssueJsonPredicate isOpenPredicate, HttpConnectionSetting ... connectionSettings) {
        this.urlScheme = urlScheme;
        this.isOpenPredicate = isOpenPredicate;
        this.acceptedContentType = acceptedContentType;
        this.connectionSettings = connectionSettings;
    }

    @Override
    public boolean isOpen(String issueId) throws IOException {
        return isOpenPredicate.isOpen(getJsonFor(issueId));
    }

    private JsonNode getJsonFor(String issueId) throws IOException {
        URL issueUrl = urlScheme.urlOfIssue(issueId);

        HttpURLConnection cx = (HttpURLConnection) issueUrl.openConnection();
        cx.setRequestProperty("Accept", acceptedContentType);
        cx.setRequestProperty("User-Agent", getClass().getPackage().getName());
        for (HttpConnectionSetting connectionSetting : connectionSettings) {
            connectionSetting.applyTo(cx);
        }

        int responseCode = cx.getResponseCode();
        if (responseCode >= 300) {
            throw new IOException("request to " + issueUrl + " failed with status " + responseCode);
        }

        return parseJson(cx);
    }

    private JsonNode parseJson(HttpURLConnection cx) throws IOException {
        Reader r = new InputStreamReader(new BufferedInputStream(cx.getInputStream()), parseCharset(cx));
        try {
            return objectMapper.readTree(r);
        } finally {
            r.close();
        }
    }

    private String parseCharset(HttpURLConnection cx) throws IOException {
        return parseCharset(parseContentType(cx));
    }

    private String parseCharset(MimeType contentType) {
        return Optional.fromNullable(contentType.getParameter("charset")).or("utf-8");
    }

    private MimeType parseContentType(HttpURLConnection cx) throws IOException {
        String contentType = cx.getContentType();
        try {
            return new MimeType(contentType);
        } catch (MimeTypeParseException e) {
            throw new IOException("failed to parse content type: " + contentType, e);
        }
    }

    /**
     * Cache issue statuses for the lifetime of the process.
     *
     * Typically, this caches for subsequent tests in the same test run.
     *
     * @return this issue tracker client, with caching applied
     */
    public IssueTracker cached() {
        return new IssueCache(this);
    }
}
