package com.natpryce.worktorule.issues.bitbucket;

public class BitbucketPublicIssuesTest extends BitbucketIssuesContract {
    public BitbucketPublicIssuesTest() {
        super(new BitbucketIssues("npryce", "worktorule-testing-public"));
    }
}
