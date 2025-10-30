package presspal.contact.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static presspal.contact.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static presspal.contact.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_INDEX;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_INTERVIEW_INDEX;
import static presspal.contact.testutil.Assert.assertThrows;
import static presspal.contact.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static presspal.contact.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import presspal.contact.logic.commands.AddCategoryCommand;
import presspal.contact.logic.commands.AddCommand;
import presspal.contact.logic.commands.AddInterviewCommand;
import presspal.contact.logic.commands.ClearCommand;
import presspal.contact.logic.commands.DeleteCommand;
import presspal.contact.logic.commands.DeleteInterviewCommand;
import presspal.contact.logic.commands.EditCategoryCommand.EditCategoryDescriptor;
import presspal.contact.logic.commands.EditCommand;
import presspal.contact.logic.commands.EditCommand.EditPersonDescriptor;
import presspal.contact.logic.commands.ExitCommand;
import presspal.contact.logic.commands.FindCommand;
import presspal.contact.logic.commands.HelpCommand;
import presspal.contact.logic.commands.ListInterviewCommand;
import presspal.contact.logic.parser.exceptions.ParseException;
import presspal.contact.model.person.Person;
import presspal.contact.model.person.PersonContainsKeywordsPredicate;
import presspal.contact.testutil.EditCategoryDescriptorBuilder;
import presspal.contact.testutil.EditPersonDescriptorBuilder;
import presspal.contact.testutil.InterviewBuilder;
import presspal.contact.testutil.PersonBuilder;
import presspal.contact.testutil.PersonUtil;

public class ContactBookParserTest {

    private final ContactBookParser parser = new ContactBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + "i/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + "i/" + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_addCategory() throws Exception {
        Person person = new PersonBuilder().withCategories("friends", "vip").build();
        EditCategoryDescriptor descriptor = new EditCategoryDescriptorBuilder(person).build();
        AddCategoryCommand command = (AddCategoryCommand) parser.parseCommand(
                AddCategoryCommand.COMMAND_WORD + " " + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PersonUtil.getEditCategoryDescriptorDetails(descriptor));
        assertEquals(new AddCategoryCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_deleteCategory() throws Exception {
        Person person = new PersonBuilder().withCategories("friends", "vip").build();
        EditCategoryDescriptor descriptor = new EditCategoryDescriptorBuilder(person).build();
        presspal.contact.logic.commands.DeleteCategoryCommand command =
                (presspal.contact.logic.commands.DeleteCategoryCommand) parser.parseCommand(
                presspal.contact.logic.commands.DeleteCategoryCommand.COMMAND_WORD + " "
                        + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PersonUtil.getEditCategoryDescriptorDetails(descriptor));
        assertEquals(new presspal.contact.logic.commands.DeleteCategoryCommand(INDEX_FIRST_PERSON, descriptor),
                command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_listInterview() throws Exception {
        ListInterviewCommand command = (ListInterviewCommand) parser.parseCommand(
                ListInterviewCommand.COMMAND_WORD + " " + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ListInterviewCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_deleteInterview() throws Exception {
        DeleteInterviewCommand command = (DeleteInterviewCommand) parser.parseCommand(
                DeleteInterviewCommand.COMMAND_WORD + " "
                        + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_INTERVIEW_INDEX + INDEX_SECOND_PERSON.getOneBased()); // person=1, interview=2
        assertEquals(new DeleteInterviewCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON), command);
    }

    @Test
    public void parseCommand_addInterview() throws Exception {
        AddInterviewCommand command = (AddInterviewCommand) parser.parseCommand(
                AddInterviewCommand.COMMAND_WORD + " "
                        + "i/" + INDEX_FIRST_PERSON.getOneBased()
                        + " " + "h/Sample Interview A"
                        + " " + "d/2024-10-10"
                        + " " + "t/14:00"
                        + " " + "l/Sample Location A");
        assertEquals(new AddInterviewCommand(new InterviewBuilder()
                .withHeader("Sample Interview A")
                .withLocation("Sample Location A")
                .withDate(LocalDateTime.parse("2024-10-10T14:00"))
                .build(), INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_nextInterview() throws Exception {
        assertTrue(parser.parseCommand("nextInterview")
                instanceof presspal.contact.logic.commands.NextInterviewCommand);
        assertTrue(parser.parseCommand("nextInterview  ")
                instanceof presspal.contact.logic.commands.NextInterviewCommand);
        assertTrue(parser.parseCommand(" nextInterview")
                instanceof presspal.contact.logic.commands.NextInterviewCommand);
    }
}
