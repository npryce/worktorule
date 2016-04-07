package com.natpryce.worktorule.issues.trello;

import com.natpryce.worktorule.BuildEnvironment;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrelloPublicCardsTest {
    TrelloCards issues = new TrelloCards(
            BuildEnvironment.getenv("TRELLO_API_KEY"),
            Optional.<String>empty(),
            singleton("553e4062f56e8f365751330a"));

    @Test
    public void cardsNotInDoneListsAreOpen() throws IOException {
        assertTrue(issues.isOpen("HKktl18U"));
        assertTrue(issues.isOpen("Po7aGWTf"));
    }

    @Test
    public void cardsInDoneListsAreClosed() throws IOException {
        assertFalse(issues.isOpen("9Dluzr6q"));
    }

    @Test
    public void archivedCardIsClosed() throws IOException {
        assertFalse(issues.isOpen("gwPLbS16"));
    }
}
