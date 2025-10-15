package presspal.contact.logic.parser;

import static presspal.contact.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import presspal.contact.commons.core.index.Index;
import presspal.contact.logic.commands.InterviewDeleteCommand;
import presspal.contact.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new InterviewDeleteCommand object.
 * Expected format: interview delete INDEX_PERSON INDEX_INTERVIEW
 */
public class InterviewDeleteCommandParser implements Parser<InterviewDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the InterviewDeleteCommand
     * and returns a InterviewDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public InterviewDeleteCommand parse(String args) throws ParseException {
        try {
            String trimmedArgs = args.trim();
            String[] tokens = trimmedArgs.split("\\s+");

            if (tokens.length != 2) {
                throw new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT, InterviewDeleteCommand.MESSAGE_USAGE));
            }

            Index personIndex = ParserUtil.parseIndex(tokens[0]);
            Index interviewIndex = ParserUtil.parseIndex(tokens[1]);

            return new InterviewDeleteCommand(personIndex, interviewIndex);

        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, InterviewDeleteCommand.MESSAGE_USAGE), pe);
        }
    }
}
