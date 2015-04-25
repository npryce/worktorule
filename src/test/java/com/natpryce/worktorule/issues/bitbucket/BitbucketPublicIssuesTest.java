package com.natpryce.worktorule.issues.bitbucket;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitbucketPublicIssuesTest extends BitbucketIssuesContract {
    public BitbucketPublicIssuesTest() {
        super(new BitbucketIssues("npryce", "worktorule-testing-public"));
    }
}
