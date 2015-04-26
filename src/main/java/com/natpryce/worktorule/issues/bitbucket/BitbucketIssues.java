package com.natpryce.worktorule.issues.bitbucket;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;
import com.natpryce.worktorule.internal.ProjectHostingServiceUrlScheme;

public class BitbucketIssues extends JsonHttpIssueTrackerClient {
    private static final String urlTemplate =
            "https://bitbucket.org/api/1.0/repositories/{owner}/{repo}/issues/{issueId}";

    private static final ImmutableSet<String> openStatuses = ImmutableSet.of(
            "new",
            "on hold");

    private static final IssueJsonPredicate issueIsOpen = new IssueJsonPredicate() {
        @Override
        public boolean isOpen(JsonNode issueJson) throws JsonMappingException {
            return openStatuses.contains(issueJson.get("status").asText("unknown"));
        }
    };

    public BitbucketIssues(String owner, String repo) {
        super(new ProjectHostingServiceUrlScheme(urlTemplate, owner, repo), issueIsOpen);
    }
}
