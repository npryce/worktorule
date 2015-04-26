package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.IssueTracker;

public class BitbucketPublicIssuesTest extends BitbucketIssuesContract {
    @Override
    protected IssueTracker createIssueTracker() {
        return new BitbucketIssues("npryce", "worktorule-testing-public");
    }
}
