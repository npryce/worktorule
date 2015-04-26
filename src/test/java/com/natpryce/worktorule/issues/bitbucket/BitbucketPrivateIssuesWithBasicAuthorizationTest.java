package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.http.BasicAuthentication;

public class BitbucketPrivateIssuesWithBasicAuthorizationTest extends BitbucketIssuesContract {
    @Override
    protected BitbucketIssues createIssueTracker() {
        return new BitbucketIssues("npryce", "worktorule-testing-private",
                BasicAuthentication.fromEnvironmentVariables(
                        "BITBUCKET_USERNAME", "BITBUCKET_PASSWORD"));
    }
}
