package com.natpryce.worktorule.issues.bitbucket;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.natpryce.worktorule.http.HttpConnectionSetting;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;
import com.natpryce.worktorule.internal.ProjectHostingServiceUrlScheme;

public class BitbucketIssues extends JsonHttpIssueTrackerClient {
    private static final String urlTemplate =
            "https://bitbucket.org/api/1.0/repositories/{owner}/{repo}/issues/{issueId}";

    private static final ImmutableSet<String> openStatuses = ImmutableSet.of(
            "new",
            "on hold");

    static final IssueJsonPredicate issueIsOpen = new IssueJsonPredicate() {
        @Override
        public boolean isOpen(JsonNode issueJson) throws JsonMappingException {
            JsonNode statusNode = issueJson.findPath("status");
            if (statusNode.isMissingNode()) {
                throw new JsonMappingException("JSON does not conform to Bitbucket issue structure");
            }

            return openStatuses.contains(statusNode.asText());
        }
    };

    public BitbucketIssues(String owner, String repo, HttpConnectionSetting... connectionSettings) {
        super(new ProjectHostingServiceUrlScheme(urlTemplate, owner, repo),
                "application/json",
                issueIsOpen,
                connectionSettings);
    }
}
