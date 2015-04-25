package com.natpryce.worktorule;

import java.io.IOException;

/**
 * Reports if an issue is recorded as open or closed in an issue tracking system.
 */
public interface IssueTracker {
    /**
     * Reports if the issue with the given ID is open.
     *
     * @param issueId the ID of the issue to query
     * @return true if the issue is open, false otherwise
     * @throws IOException if communication with the issue tracker failed
     */
    boolean isOpen(String issueId) throws IOException;
}
