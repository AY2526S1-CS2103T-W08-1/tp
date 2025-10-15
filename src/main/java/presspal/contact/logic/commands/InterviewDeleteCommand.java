package presspal.contact.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import presspal.contact.commons.core.index.Index;
import presspal.contact.logic.commands.exceptions.CommandException;
import presspal.contact.model.Model;
import presspal.contact.model.interview.Interview;
import presspal.contact.model.person.Person;

/**
 * Deletes an interview from a contact identified using it's displayed index from the contact book.
 */
public class InterviewDeleteCommand extends Command {

    public static final String COMMAND_WORD = "interview delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an interview from a contact.\n"
            + "Parameters: INDEX_PERSON INDEX_INTERVIEW\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_SUCCESS = "Interview %1$s deleted from %2$s";
    public static final String MESSAGE_INVALID_PERSON_INDEX = "Please provide a valid person index from 1-%d";
    public static final String MESSAGE_INVALID_INTERVIEW_INDEX = "Please provide a valid interview index from 1-%d";

    private final Index personIndex;
    private final Index interviewIndex;

    /**
     * @param personIndex of the person in the filtered person list to edit
     * @param interviewIndex of the interview in the person's interview list to delete
     */
    public InterviewDeleteCommand(Index personIndex, Index interviewIndex) {
        this.personIndex = personIndex;
        this.interviewIndex = interviewIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_PERSON_INDEX, lastShownList.size()));
        }

        Person personToEdit = lastShownList.get(personIndex.getZeroBased());
        List<Interview> interviewList = personToEdit.getInterviews();

        if (interviewIndex.getZeroBased() >= interviewList.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_INTERVIEW_INDEX, interviewList.size()));
        }

        Interview interviewToDelete = interviewList.get(interviewIndex.getZeroBased());

        Person updatedContact = personToEdit.removeInterview(interviewToDelete);

        model.setPerson(personToEdit, updatedContact);

        return new CommandResult(String.format(
                MESSAGE_DELETE_SUCCESS,
                interviewToDelete,
                updatedContact.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof InterviewDeleteCommand
                && personIndex.equals(((InterviewDeleteCommand) other).personIndex)
                && interviewIndex.equals(((InterviewDeleteCommand) other).interviewIndex));
    }
}
