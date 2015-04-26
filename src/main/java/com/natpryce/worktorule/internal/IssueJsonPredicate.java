package com.natpryce.worktorule.internal;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface IssueJsonPredicate {
    boolean isOpen(JsonNode issueJson) throws JsonMappingException;
}
