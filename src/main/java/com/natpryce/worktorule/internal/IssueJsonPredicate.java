package com.natpryce.worktorule.internal;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Nullable;

public interface IssueJsonPredicate {
    boolean isOpen(JsonNode issueJson) throws JsonMappingException;
}
