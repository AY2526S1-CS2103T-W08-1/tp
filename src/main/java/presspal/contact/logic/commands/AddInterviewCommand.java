package presspal.contact.logic.commands;

import static java.util.Objects.requireNonNull;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_DATE;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_HEADER;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_INDEX;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_LOCATION;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_TIME;

import presspal.contact.commons.util.ToStringBuilder;
import presspal.contact.logic.commands.exceptions.CommandException;
import presspal.contact.model.Model;

/**
 * Adds an interview to the address book.
 */
public class AddInterviewCommand extends Command {

    public static final String COMMAND_WORD = "addInterview";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an interview. "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_LOCATION + "LOCATION "
            + PREFIX_HEADER + "HEADER "
            + PREFIX_INDEX + "INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2024-10-10 "
            + PREFIX_TIME + "14:00 "
            + PREFIX_LOCATION + "123, Business St, #02-25 "
            + PREFIX_HEADER + "Interview with ABC Corp "
            + PREFIX_INDEX + "1";

    public static final String MESSAGE_SUCCESS = "New interview added: %1$s";
    public static final String MESSAGE_DUPLICATE_INTERVIEW = "This interview already exists";

    private final String toAdd; // Replace String with Interview when Interview class is created

    /**
     * Creates an AddInterviewCommand to add the specified interview.
     * @param interview the interview to add
     */
    public AddInterviewCommand(String interview) {
        requireNonNull(interview);
        toAdd = interview;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasInterview(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_INTERVIEW);
        }

        model.addInterview(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd)); // Pass the interview as argument
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddInterviewCommand)) {
            return false;
        }

        AddInterviewCommand otherCommand = (AddInterviewCommand) other;
        return toAdd.equals(otherCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("interview", toAdd)
                .toString();
    }
}
