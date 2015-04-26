package com.natpryce.worktorule;

import com.natpryce.worktorule.issues.github.GitHubIssues;

public class WorkToRuleIssueTracker {
    public static final IssueTracker PUBLIC =
            new GitHubIssues("npryce", "worktorule").cached();
}
