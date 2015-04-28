package com.natpryce.worktorule.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.natpryce.worktorule.IssueTracker;
import com.natpryce.worktorule.http.HttpConnectionSetting;

import java.io.IOException;

public class JsonHttpIssueTrackerClient implements IssueTracker {
    private final IssueTrackerUriScheme urlScheme;
    private final IssueJsonPredicate isOpenPredicate;

    private final JsonHttp jsonHttp;

    public JsonHttpIssueTrackerClient(IssueTrackerUriScheme urlScheme, String acceptedContentType, IssueJsonPredicate isOpenPredicate, HttpConnectionSetting ... connectionSettings) {
        this.urlScheme = urlScheme;
        this.isOpenPredicate = isOpenPredicate;
        jsonHttp = new JsonHttp(acceptedContentType, connectionSettings);
    }

    @Override
    public boolean isOpen(String issueId) throws IOException {
        return isOpenPredicate.isOpen(getJsonFor(issueId));
    }

    private JsonNode getJsonFor(String issueId) throws IOException {
        return jsonHttp.getJson(urlScheme.uriForIssue(issueId));
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
