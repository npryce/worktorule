package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.BuildEnvironment;

public class BitbucketPrivateIssuesWithBasicAuthorizationTest extends BitbucketIssuesContract {
    @Override
    protected BitbucketIssues createIssueTracker() {
        return new BitbucketIssues("npryce", "worktorule-testing-private",
                BuildEnvironment.authFromEnvvars(
                        "BITBUCKET_USERNAME", "BITBUCKET_PASSWORD"));
    }
}
