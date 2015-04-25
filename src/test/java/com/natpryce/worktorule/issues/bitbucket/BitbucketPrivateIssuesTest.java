package com.natpryce.worktorule.issues.bitbucket;

import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.InProgress;
import com.natpryce.worktorule.IssueTracker;
import com.natpryce.worktorule.WorkToRuleIssueTracker;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@InProgress("2")
public class BitbucketPrivateIssuesTest extends BitbucketIssuesContract {
    @Rule
    public TestRule workInProgress = new IgnoreInProgress(WorkToRuleIssueTracker.PUBLIC);

    public BitbucketPrivateIssuesTest() {
        super(new BitbucketIssues("npryce", "worktorule-testing-private"));
    }
}
