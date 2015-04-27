package com.natpryce.worktorule.issues.jira;

import com.natpryce.worktorule.IssuesParsingContract;


public class JiraIssuesParsingTest extends IssuesParsingContract {
    public JiraIssuesParsingTest() {
        super(Jira.issueIsOpen);
    }
}
