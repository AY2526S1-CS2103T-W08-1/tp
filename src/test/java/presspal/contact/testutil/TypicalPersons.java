package presspal.contact.testutil;

import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_FRIEND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_HUSBAND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_ORGANISATION_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_ORGANISATION_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import presspal.contact.model.AddressBook;
import presspal.contact.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withOrganisation("NUS").withEmail("alice@example.com")
            .withPhone("94351253")
            .withCategories("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withOrganisation("NTU")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withCategories("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withOrganisation("wall street").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withOrganisation("10th street").withCategories("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withOrganisation("michegan ave").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withOrganisation("little tokyo").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withOrganisation("CAPT").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withOrganisation("little india").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withOrganisation("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withOrganisation(VALID_ORGANISATION_AMY).withCategories(VALID_CATEGORY_FRIEND)
            .build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withOrganisation(VALID_ORGANISATION_BOB)
            .withCategories(VALID_CATEGORY_HUSBAND, VALID_CATEGORY_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
