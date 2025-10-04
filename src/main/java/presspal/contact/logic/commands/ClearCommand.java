package presspal.contact.logic.commands;

import static java.util.Objects.requireNonNull;

import presspal.contact.model.AddressBook;
import presspal.contact.model.Model;

/**
 * Clears the contact book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
