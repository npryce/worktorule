package com.natpryce.worktorule.issues.bitbucket;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.natpryce.snodge.JsonMutator;
import com.natpryce.snodge.Mutator;
import com.natpryce.worktorule.issues.github.GitHubIssues;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;


public class BitbucketIssuesParsingTest {
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
            BitbucketIssues.issueIsOpen.isOpen(mutatedNode);
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
