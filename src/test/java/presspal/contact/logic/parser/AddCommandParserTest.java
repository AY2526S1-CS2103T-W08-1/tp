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
import static presspal.contact.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.ORGANISATION_DESC_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.ORGANISATION_DESC_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static presspal.contact.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_FRIEND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_HUSBAND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_ORGANISATION_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_EMAIL;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_NAME;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static presspal.contact.logic.parser.CliSyntax.PREFIX_PHONE;
import static presspal.contact.logic.parser.CommandParserTestUtil.assertParseFailure;
import static presspal.contact.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static presspal.contact.testutil.TypicalPersons.AMY;
import static presspal.contact.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import presspal.contact.logic.Messages;
import presspal.contact.logic.commands.AddCommand;
import presspal.contact.model.category.Category;
import presspal.contact.model.person.Email;
import presspal.contact.model.person.Name;
import presspal.contact.model.person.Organisation;
import presspal.contact.model.person.Person;
import presspal.contact.model.person.Phone;
import presspal.contact.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withCategories(VALID_CATEGORY_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ORGANISATION_DESC_BOB + CATEGORY_DESC_FRIEND, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleCategories = new PersonBuilder(BOB)
                .withCategories(VALID_CATEGORY_FRIEND, VALID_CATEGORY_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ORGANISATION_DESC_BOB + CATEGORY_DESC_HUSBAND + CATEGORY_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleCategories));
    }

    @Test
    public void parse_repeatedNonCategoryValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ORGANISATION_DESC_BOB + CATEGORY_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple organisations
        assertParseFailure(parser, ORGANISATION_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ORGANISATION));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ORGANISATION_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ORGANISATION,
                        PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid organisation
        assertParseFailure(parser, INVALID_ORGANISATION_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ORGANISATION));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid organisation
        assertParseFailure(parser, validExpectedPersonString + INVALID_ORGANISATION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ORGANISATION));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withCategories().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ORGANISATION_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ORGANISATION_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ORGANISATION_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ORGANISATION_DESC_BOB,
                expectedMessage);

        // missing organisation prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ORGANISATION_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ORGANISATION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ORGANISATION_DESC_BOB
                + CATEGORY_DESC_HUSBAND + CATEGORY_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ORGANISATION_DESC_BOB
                + CATEGORY_DESC_HUSBAND + CATEGORY_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ORGANISATION_DESC_BOB
                + CATEGORY_DESC_HUSBAND + CATEGORY_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid organisation
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ORGANISATION_DESC
                + CATEGORY_DESC_HUSBAND + CATEGORY_DESC_FRIEND, Organisation.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ORGANISATION_DESC_BOB
                + INVALID_CATEGORY_DESC + VALID_CATEGORY_FRIEND, Category.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ORGANISATION_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ORGANISATION_DESC_BOB + CATEGORY_DESC_HUSBAND + CATEGORY_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
