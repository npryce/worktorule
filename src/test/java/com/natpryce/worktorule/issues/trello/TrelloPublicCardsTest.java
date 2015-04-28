package com.natpryce.worktorule.issues.trello;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.natpryce.worktorule.BuildEnvironment;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrelloPublicCardsTest {
    TrelloCards issues = new TrelloCards(
            BuildEnvironment.getenv("TRELLO_API_KEY"),
            Optional.<String>absent(),
            ImmutableSet.of("553e4062f56e8f365751330a"));

    @Test
    public void cardsNotInDoneListsAreOpen() throws IOException {
        assertTrue(issues.isOpen("HKktl18U"));
        assertTrue(issues.isOpen("Po7aGWTf"));
    }

    @Test
    public void archivedCardIsClosed() throws IOException {
        assertFalse(issues.isOpen("gwPLbS16"));
    }

    @Test
    public void cardsInDoneListsAreClosed() throws IOException {
        assertFalse(issues.isOpen("9Dluzr6q"));
    }
}
