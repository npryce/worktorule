package com.natpryce.worktorule;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.natpryce.snodge.JsonMutator;
import com.natpryce.snodge.Mutator;
import com.natpryce.worktorule.internal.IssueJsonPredicate;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;


public abstract class IssuesParsingContract {
    ObjectMapper objectMapper = new ObjectMapper();
    Mutator<String> mutator = new JsonMutator().forStrings();
    private final IssueJsonPredicate issueIsOpen;

    protected IssuesParsingContract(IssueJsonPredicate issueIsOpen) {
        this.issueIsOpen = issueIsOpen;
    }

    @Test
    public void shouldFailGracefully() throws IOException {
        String issueJson = Resources.toString(getClass().getResource("issue.json"), Charset.defaultCharset());

        for (String mutatedJson : mutator.mutate(issueJson, 250)) {
            assertFailsGracefullyWhenBadJson(issueIsOpen, mutatedJson);
        }
    }

    protected void assertFailsGracefullyWhenBadJson(IssueJsonPredicate issueIsOpen, String mutatedJson) throws IOException {
        JsonNode mutatedNode = objectMapper.readTree(mutatedJson);

        try {
            issueIsOpen.isOpen(mutatedNode);
            // ok
        } catch (JsonMappingException e) {
            // ok
        } catch (RuntimeException e) {
            AssertionError failure = new AssertionError("should have failed gracefully for JSON:\n" + mutatedJson);
            failure.initCause(e);
            throw failure;
        }
    }
}
