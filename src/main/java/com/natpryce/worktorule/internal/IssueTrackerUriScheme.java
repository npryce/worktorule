package com.natpryce.worktorule.internal;

import java.net.URI;

public interface IssueTrackerUriScheme {
    URI uriForIssue(String issueId);
}
