package com.natpryce.worktorule.issues.github;

import com.natpryce.worktorule.IssuesParsingContract;


public class GitHubIssuesParsingTest extends IssuesParsingContract {
    public GitHubIssuesParsingTest() {
        super(GitHubIssues.issueIsOpen);
    }
}
