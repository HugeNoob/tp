package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class DeleteTagPersonCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_success() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_FRIEND));
        DeleteTagPersonCommand deleteTagPersonCommand = new DeleteTagPersonCommand(INDEX_FIRST, tags);

        // Starting state of first person only has friend tag
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());

        // Manually remove friend tag
        Set<Tag> newTags = new HashSet<>(personToEdit.getTags());
        newTags.remove(new Tag(VALID_TAG_FRIEND));

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                newTags);

        String expectedMessage = String.format(DeleteTagPersonCommand.MESSAGE_DELETE_TAG_PERSON_SUCCESS,
                Messages.format(editedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(deleteTagPersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteTagPersonCommand deleteTagPersonCommand = new DeleteTagPersonCommand(outOfBoundIndex,
                new HashSet<>());

        assertCommandFailure(deleteTagPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteTagPersonCommand deleteTagPersonFirstCommand = new DeleteTagPersonCommand(INDEX_FIRST, new HashSet<>());
        DeleteTagPersonCommand deleteTagPersonSecondCommand = new DeleteTagPersonCommand(INDEX_SECOND, new HashSet<>());

        // same object -> returns true
        assertTrue(deleteTagPersonFirstCommand.equals(deleteTagPersonFirstCommand));

        // same values -> returns true
        DeleteTagPersonCommand deleteTagPersonFirstCommandCopy = new DeleteTagPersonCommand(INDEX_FIRST,
                new HashSet<>());
        assertTrue(deleteTagPersonFirstCommand.equals(deleteTagPersonFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteTagPersonFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteTagPersonFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteTagPersonFirstCommand.equals(deleteTagPersonSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_FRIEND));

        DeleteTagPersonCommand deleteTagPersonCommand = new DeleteTagPersonCommand(targetIndex, tags);
        String expected = DeleteTagPersonCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex
                + ", tagsToDelete="
                + tags + "}";
        assertEquals(expected, deleteTagPersonCommand.toString());
    }
}
