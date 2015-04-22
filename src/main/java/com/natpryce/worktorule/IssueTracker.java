package com.natpryce.worktorule;

import java.io.IOException;

public interface IssueTracker {
    boolean isOpen(String issueId) throws IOException;
}
