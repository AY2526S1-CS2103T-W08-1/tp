package presspal.contact.logic.parser;

import static presspal.contact.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_DATE;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_HEADER;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_INDEX;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_LOCATION;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.stream.Stream;

import presspal.contact.logic.commands.AddInterviewCommand;
import presspal.contact.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddInterviewCommand object.
 */
public class AddInterviewCommandParser implements Parser<AddInterviewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddInterviewCommand
     * and returns an AddInterviewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddInterviewCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_INDEX, PREFIX_HEADER, PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX, PREFIX_HEADER, PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddInterviewCommand.MESSAGE_USAGE));
        }

        //argMultimap.verifyNoDuplicatePrefixesFor(); // Verify no duplicates, Omit for now
        String date = argMultimap.getValue(PREFIX_DATE).orElse("");
        String time = argMultimap.getValue(PREFIX_TIME).orElse("");
        String location = argMultimap.getValue(PREFIX_LOCATION).orElse("");
        String header = argMultimap.getValue(PREFIX_HEADER).orElse("");
        String index = argMultimap.getValue(PREFIX_INDEX).orElse("");
        // Leave it as string for now, can create object class if we really want to,
        // depends on interview class

        //Interview interview = new Interview(date, time, location, header, index);
        // Omit for now since Interview class not created

        return new AddInterviewCommand(date + time + location + header + index);
        // Replace with interview object when Interview class is created
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap,
                                              Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
