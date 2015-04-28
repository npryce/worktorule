package com.natpryce.worktorule.issues.jira;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.natpryce.worktorule.http.HttpConnectionSetting;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import com.natpryce.worktorule.internal.JsonHttpIssueTrackerClient;

import java.net.URI;

public class Jira extends JsonHttpIssueTrackerClient {
    public Jira(String jiraServerUrl, HttpConnectionSetting... connectionSettings) {
        super(new JiraIssueUriScheme(URI.create(jiraServerUrl)), "application/json", issueIsOpen, connectionSettings);
    }

    static final IssueJsonPredicate issueIsOpen = new IssueJsonPredicate() {
        @Override
        public boolean isOpen(JsonNode issueJson) throws JsonMappingException {
            JsonNode statusCategoryKeyNode = issueJson.findPath("fields").findPath("status").findPath("statusCategory").findPath("key");
            if (!statusCategoryKeyNode.isTextual()) {
                throw new JsonMappingException("JSON does not conform to JIRA issue structure");
            }
            return !statusCategoryKeyNode.asText().equals("done");
        }
    };
}
