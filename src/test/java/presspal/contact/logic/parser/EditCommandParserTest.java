package presspal.contact.logic.parser;

import static presspal.contact.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static presspal.contact.logic.commands.CommandTestUtil.CATEGORY_DESC_FRIEND;
import static presspal.contact.logic.commands.CommandTestUtil.CATEGORY_DESC_HUSBAND;
import static presspal.contact.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static presspal.contact.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static presspal.contact.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static presspal.contact.logic.commands.CommandTestUtil.INVALID_ORGANISATION_DESC;
import static presspal.contact.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static presspal.contact.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.ORGANISATION_DESC_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.ORGANISATION_DESC_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_FRIEND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_HUSBAND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_ORGANISATION_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_EMAIL;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_PHONE;
import static presspal.contact.logic.parser.CommandParserTestUtil.assertParseFailure;
import static presspal.contact.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static presspal.contact.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static presspal.contact.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static presspal.contact.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import presspal.contact.commons.core.index.Index;
import presspal.contact.logic.Messages;
import presspal.contact.logic.commands.EditCommand;
import presspal.contact.logic.commands.EditCommand.EditPersonDescriptor;
import presspal.contact.model.category.Category;
import presspal.contact.model.person.Email;
import presspal.contact.model.person.Name;
import presspal.contact.model.person.Organisation;
import presspal.contact.model.person.Phone;
import presspal.contact.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String CATEGORY_EMPTY = " " + PREFIX_CATEGORY;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ORGANISATION_DESC,
                Organisation.MESSAGE_CONSTRAINTS); // invalid organisation
        assertParseFailure(parser, "1" + INVALID_CATEGORY_DESC, Category.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + CATEGORY_DESC_FRIEND + CATEGORY_DESC_HUSBAND + CATEGORY_EMPTY,
                Category.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + CATEGORY_DESC_FRIEND + CATEGORY_EMPTY + CATEGORY_DESC_HUSBAND,
                Category.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + CATEGORY_EMPTY + CATEGORY_DESC_FRIEND + CATEGORY_DESC_HUSBAND,
                Category.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ORGANISATION_AMY
                + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + CATEGORY_DESC_HUSBAND
                + EMAIL_DESC_AMY + ORGANISATION_DESC_AMY + NAME_DESC_AMY + CATEGORY_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withOrganisation(VALID_ORGANISATION_AMY)
                .withCategories(VALID_CATEGORY_HUSBAND, VALID_CATEGORY_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // organisation
        userInput = targetIndex.getOneBased() + ORGANISATION_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withOrganisation(VALID_ORGANISATION_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + CATEGORY_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withCategories(VALID_CATEGORY_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ORGANISATION_DESC_AMY + EMAIL_DESC_AMY
                + CATEGORY_DESC_FRIEND + PHONE_DESC_AMY + ORGANISATION_DESC_AMY + EMAIL_DESC_AMY + CATEGORY_DESC_FRIEND
                + PHONE_DESC_BOB + ORGANISATION_DESC_BOB + EMAIL_DESC_BOB + CATEGORY_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ORGANISATION));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ORGANISATION_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ORGANISATION_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ORGANISATION));
    }

    @Test
    public void parse_resetCategories_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + CATEGORY_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withCategories().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
