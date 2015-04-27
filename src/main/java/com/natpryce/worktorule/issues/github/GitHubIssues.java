package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.natpryce.worktorule.http.HttpConnectionSetting;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;
import com.natpryce.worktorule.internal.ProjectHostingServiceUrlScheme;

public class GitHubIssues extends JsonHttpIssueTrackerClient {
    private static final String urlTemplate =
            "https://api.github.com/repos/{owner}/{repo}/issues/{issueId}";

    public GitHubIssues(final String owner, final String repo, HttpConnectionSetting ... connectionSettings) {
        super(new ProjectHostingServiceUrlScheme(urlTemplate, owner, repo),
                "application/vnd.github.v3+json",
                issueIsOpen,
                connectionSettings);
    }

    static final IssueJsonPredicate issueIsOpen = new IssueJsonPredicate() {
        @Override
        public boolean isOpen(JsonNode issueJson) throws JsonMappingException {
            JsonNode stateNode = issueJson.findPath("state");
            if (!stateNode.isTextual()) {
                throw new JsonMappingException("JSON does not conform to GitHub issue structure");
            }

            return stateNode.asText().equals("open");
        }
    };
}
