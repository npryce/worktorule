package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.InProgress;
import com.natpryce.worktorule.WorkToRuleIssueTracker;
import com.natpryce.worktorule.http.BasicAuthentication;
import org.junit.Rule;
import org.junit.rules.TestRule;

public class BitbucketPrivateIssuesWithBasicAuthorizationTest extends BitbucketIssuesContract {
    @Override
    protected BitbucketIssues createIssueTracker() {
        return new BitbucketIssues("npryce", "worktorule-testing-private",
                BasicAuthentication.fromEnvironmentVariables(
                        "BITBUCKET_USERNAME", "BITBUCKET_PASSWORD"));
    }
}
