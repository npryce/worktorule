package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;
import com.natpryce.worktorule.internal.ProjectHostingServiceUrlScheme;

import javax.annotation.Nullable;

public class GitHubIssues extends JsonHttpIssueTrackerClient {
    private static final String urlTemplate =
            "https://api.github.com/repos/{owner}/{repo}/issues/{issueId}";

    public GitHubIssues(final String owner, final String repo) {
        super(new ProjectHostingServiceUrlScheme(urlTemplate, owner, repo),
                "application/vnd.github.v3+json",
                issueIsOpen);
    }

    static final IssueJsonPredicate issueIsOpen = new IssueJsonPredicate() {
        @Override
        public boolean isOpen(JsonNode issueJson) throws JsonMappingException {
            if (issueJson.isObject()) {
                JsonNode state = issueJson.get("state");
                if (state != null && state.isTextual()) {
                    return state.textValue().equals("open");
                }
            }

            throw new JsonMappingException("JSON does not conform to GitHub issue structure");
        }
    };

}
