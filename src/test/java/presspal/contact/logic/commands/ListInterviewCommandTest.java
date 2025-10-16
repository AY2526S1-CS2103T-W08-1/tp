package presspal.contact.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import static presspal.contact.logic.commands.CommandTestUtil.*;
import static presspal.contact.logic.commands.CommandTestUtil.showPersonAtIndex;
import static presspal.contact.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static presspal.contact.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static presspal.contact.testutil.TypicalPersons.getTypicalContactBook;

import org.junit.jupiter.api.Test;

import presspal.contact.commons.core.index.Index;
import presspal.contact.logic.Messages;
import presspal.contact.model.Model;
import presspal.contact.model.ModelManager;
import presspal.contact.model.UserPrefs;

public class ListInterviewCommandTest {

    private Model model = new ModelManager(getTypicalContactBook(), new UserPrefs());

    //execute_validIndexUnfilteredList_success

    //execute_validIndexFilteredList_success

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ListInterviewCommand listInterviewCommand = new ListInterviewCommand(outOfBoundIndex);

        assertCommandFailure(listInterviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of contact book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getContactBook().getPersonList().size());

        ListInterviewCommand listInterviewCommand = new ListInterviewCommand(outOfBoundIndex);

        assertCommandFailure(listInterviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ListInterviewCommand listInterviewFirstCommand = new ListInterviewCommand(INDEX_FIRST_PERSON);
        ListInterviewCommand listInterviewSecondCommand = new ListInterviewCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(listInterviewFirstCommand.equals(listInterviewFirstCommand));

        // same values -> returns true
        ListInterviewCommand listInterviewFirstCommandCopy = new ListInterviewCommand(INDEX_FIRST_PERSON);
        assertTrue(listInterviewFirstCommand.equals(listInterviewFirstCommandCopy));

        // different types -> returns false
        assertFalse(listInterviewFirstCommand.equals(1));

        // null -> returns false
        assertFalse(listInterviewFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(listInterviewFirstCommand.equals(listInterviewSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        ListInterviewCommand listInterviewCommand = new ListInterviewCommand(targetIndex);
        String expected = ListInterviewCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, listInterviewCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
