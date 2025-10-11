package presspal.contact.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_HUSBAND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_ORGANISATION_BOB;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static presspal.contact.testutil.Assert.assertThrows;
import static presspal.contact.testutil.TypicalPersons.ALICE;
import static presspal.contact.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import presspal.contact.testutil.PersonBuilder;


public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getCategories().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withOrganisation(VALID_ORGANISATION_BOB).withCategories(VALID_CATEGORY_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different organisation -> returns false
        editedAlice = new PersonBuilder(ALICE).withOrganisation(VALID_ORGANISATION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withCategories(VALID_CATEGORY_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        // equal objects must have same hashCode
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());

        // different object should ideally have a different hashCode (not required by spec,
        // but helps catch obvious issues). If nondeterministic, remove this assertion.
        assertFalse(ALICE.hashCode() == BOB.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", organisation=" + ALICE.getOrganisation()
                + ", categories=" + ALICE.getCategories() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
