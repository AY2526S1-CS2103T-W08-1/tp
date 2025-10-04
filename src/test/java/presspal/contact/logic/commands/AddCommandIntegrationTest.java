package presspal.contact.logic.commands;

import static presspal.contact.logic.commands.CommandTestUtil.assertCommandFailure;
import static presspal.contact.logic.commands.CommandTestUtil.assertCommandSuccess;
import static presspal.contact.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import presspal.contact.logic.Messages;
import presspal.contact.model.Model;
import presspal.contact.model.ModelManager;
import presspal.contact.model.UserPrefs;
import presspal.contact.model.person.Person;
import presspal.contact.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
