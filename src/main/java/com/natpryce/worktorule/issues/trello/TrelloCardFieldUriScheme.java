package com.natpryce.worktorule.issues.trello;

import java.util.Optional;
import com.natpryce.worktorule.internal.IssueTrackerUriScheme;
import com.scurrilous.uritemplate.URITemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

class TrelloCardFieldUriScheme implements IssueTrackerUriScheme {
    private final URITemplate template;
    private final String apiKey;
    private final Optional<String> userToken;

    public TrelloCardFieldUriScheme(String apiKey, Optional<String> userToken, String template) {
        this.apiKey = apiKey;
        this.userToken = userToken;
        this.template = new URITemplate(template);
    }

    @Override
    public URI uriForIssue(String cardId) {
        Map<String,Object> args = new HashMap<String, Object>();
        args.put("key", apiKey);
        userToken.ifPresent(userToken -> args.put("token", userToken));
        args.put("cardId", cardId);

        try {
            return template.expand(args);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid URI syntax", e);
        }
    }
}
