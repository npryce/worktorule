package com.natpryce.worktorule.issues.trello;

import com.natpryce.worktorule.BuildEnvironment;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrelloPrivateCardsTest {
    TrelloCards issues = new TrelloCards(
            BuildEnvironment.getenv("TRELLO_API_KEY"),
            Optional.of(BuildEnvironment.getenv("TRELLO_USER_TOKEN")),
            singleton("55400cf0e35d9d83917e5404"));

    @Test
    public void cardsNotInDoneListsAreOpen() throws IOException {
        assertTrue(issues.isOpen("e5d5CKwD"));
        assertTrue(issues.isOpen("8Hgh3CoP"));
    }

    @Test
    public void cardsInDoneListsAreClosed() throws IOException {
        assertFalse(issues.isOpen("Pt9Mo3xx"));
    }

    @Test
    public void archivedCardIsClosed() throws IOException {
        assertFalse(issues.isOpen("N9jKDnDI"));
    }
}
