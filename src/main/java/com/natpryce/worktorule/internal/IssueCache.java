package com.natpryce.worktorule.internal;

import com.natpryce.worktorule.IssueTracker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Caches issue statuses for the lifetime of the process.
 *
 * Typically, this caches for subsequent tests in the same test run.
 */
public class IssueCache implements IssueTracker {
    private final Map<String, Boolean> cache = new HashMap<String, Boolean>();
    private final IssueTracker tracker;

    public IssueCache(IssueTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public boolean isOpen(String issueId) throws IOException {
        if (cache.containsKey(issueId)) {
            return cache.get(issueId);
        }
        else {
            boolean isOpen = tracker.isOpen(issueId);
            cache.put(issueId, isOpen);
            return isOpen;
        }
    }
}
