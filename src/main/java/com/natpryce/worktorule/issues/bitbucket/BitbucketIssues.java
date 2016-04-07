package com.natpryce.worktorule.issues.bitbucket;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.natpryce.worktorule.http.HttpConnectionSetting;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;
import com.natpryce.worktorule.internal.ProjectHostingServiceUriScheme;

import java.util.Arrays;
import java.util.List;

public class BitbucketIssues extends JsonHttpIssueTrackerClient {
    private static final String urlTemplate =
            "https://bitbucket.org/api/1.0/repositories/{owner}/{repo}/issues/{issueId}";

    private static final List<String> openStatuses = Arrays.asList(
            "new",
            "on hold");

    static final IssueJsonPredicate issueIsOpen = issueJson -> {
        JsonNode statusNode = issueJson.findPath("status");
        if (!statusNode.isTextual()) {
            throw new JsonMappingException("JSON does not conform to Bitbucket issue structure");
        }

        return openStatuses.contains(statusNode.asText());
    };

    public BitbucketIssues(String owner, String repo, HttpConnectionSetting... connectionSettings) {
        super(new ProjectHostingServiceUriScheme(urlTemplate, owner, repo),
                "application/json",
                issueIsOpen,
                connectionSettings);
    }
}
