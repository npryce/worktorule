package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.InProgress;
import com.natpryce.worktorule.WorkToRuleIssueTracker;
import org.junit.Rule;
import org.junit.rules.TestRule;

@InProgress("2")
public class BitbucketPrivateIssuesTest extends BitbucketIssuesContract {
    @Rule
    public TestRule workInProgress = new IgnoreInProgress(WorkToRuleIssueTracker.PUBLIC);

    public BitbucketPrivateIssuesTest() {
        super(new BitbucketIssues("npryce", "worktorule-testing-private"));
    }
}
