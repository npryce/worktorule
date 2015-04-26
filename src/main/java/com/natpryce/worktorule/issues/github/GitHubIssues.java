package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;
import com.natpryce.worktorule.internal.ProjectHostingServiceUrlScheme;

public class GitHubIssues extends JsonHttpIssueTrackerClient {
    private static final String urlTemplate =
            "https://api.github.com/repos/{owner}/{repo}/issues/{issueId}";

    private static final IssueJsonPredicate issueIsOpen = new IssueJsonPredicate() {
        @Override
        public boolean isOpen(JsonNode issueJson) throws JsonMappingException {
            return issueJson.get("state").asText("unknown").equals("open");
        }
    };

    public GitHubIssues(final String owner, final String repo) {
        super(new ProjectHostingServiceUrlScheme(urlTemplate, owner, repo),
                "application/vnd.github.v3+json",
                issueIsOpen);
    }

}
