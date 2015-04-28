package com.natpryce.worktorule.issues.trello;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Optional;
import com.natpryce.worktorule.IssueTracker;
import com.natpryce.worktorule.http.HttpConnectionSetting;
import com.natpryce.worktorule.internal.JsonHttp;

import java.io.IOException;
import java.util.Set;

import static com.fasterxml.jackson.databind.node.JsonNodeType.BOOLEAN;
import static com.fasterxml.jackson.databind.node.JsonNodeType.STRING;

public class TrelloCards implements IssueTracker {
    private final Set<String> doneListIds;
    private final JsonHttp jsonHttp;
    private final TrelloCardFieldUriScheme cardClosedUriScheme;
    private final TrelloCardFieldUriScheme cardListIdUriScheme;

    public TrelloCards(String apiKey, Optional<String> userToken, Set<String> closedIssueListIds, HttpConnectionSetting ... connectionSettings) {
        this.jsonHttp = new JsonHttp("application/json", connectionSettings);
        this.cardClosedUriScheme = new TrelloCardFieldUriScheme(apiKey, userToken,
                "https://api.trello.com/1/card/{cardId}/closed{?key,token}");
        this.cardListIdUriScheme = new TrelloCardFieldUriScheme(apiKey, userToken,
                "https://api.trello.com/1/card/{cardId}/idList{?key,token}");
        this.doneListIds = closedIssueListIds;
    }

    @Override
    public boolean isOpen(String cardId) throws IOException {
        return !cardIsClosed(cardId) && !doneListIds.contains(cardListId(cardId));
    }

    private String cardListId(String cardId) throws IOException {
        return getCardField(cardId, cardListIdUriScheme, STRING).textValue();
    }

    private JsonNode getCardField(String cardId, TrelloCardFieldUriScheme uriScheme, JsonNodeType nodeType) throws IOException {
        JsonNode valueNode = jsonHttp.getJson(uriScheme.uriForIssue(cardId)).findPath("_value");
        if (valueNode.getNodeType() != nodeType) {
            throw new JsonMappingException("unexpected JSON structure");
        }
        return valueNode;
    }

    private boolean cardIsClosed(String cardId) throws IOException {
        return getCardField(cardId, cardClosedUriScheme, BOOLEAN).booleanValue();
    }
}
