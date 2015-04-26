package com.natpryce.worktorule.issues.github;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.natpryce.snodge.JsonMutator;
import com.natpryce.snodge.Mutator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;


public class GitHubIssuesParsingTest {
    ObjectMapper objectMapper = new ObjectMapper();
    Mutator<String> mutator = new JsonMutator().forStrings();

    String issueJson;

    @Before
    public void setUp() throws Exception {
        issueJson = Resources.toString(Resources.getResource(getClass(), "issue.json"),
                Charset.defaultCharset());
    }

    @Test
    public void shouldFailGracefully() throws IOException {
        for (String mutatedJson : mutator.mutate(issueJson, 250)) {
            assertFailsGracefullyWhenBadJson(mutatedJson);
        }
    }

    private void assertFailsGracefullyWhenBadJson(String mutatedJson) throws IOException {
        JsonNode mutatedNode = objectMapper.readTree(mutatedJson);
        try {
            GitHubIssues.issueIsOpen.isOpen(mutatedNode);
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
