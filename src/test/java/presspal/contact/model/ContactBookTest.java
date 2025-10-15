package presspal.contact.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_CATEGORY_HUSBAND;
import static presspal.contact.logic.commands.CommandTestUtil.VALID_ORGANISATION_BOB;
import static presspal.contact.testutil.Assert.assertThrows;
import static presspal.contact.testutil.TypicalPersons.ALICE;
import static presspal.contact.testutil.TypicalPersons.getTypicalContactBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presspal.contact.model.person.Person;
import presspal.contact.model.person.exceptions.DuplicatePersonException;
import presspal.contact.testutil.PersonBuilder;


public class ContactBookTest {

    private final ContactBook contactBook = new ContactBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), contactBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> contactBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyContactBook_replacesData() {
        ContactBook newData = getTypicalContactBook();
        contactBook.resetData(newData);
        assertEquals(newData, contactBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE)
                .withOrganisation(VALID_ORGANISATION_BOB)
                .withCategories(VALID_CATEGORY_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        ContactBookStub newData = new ContactBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> contactBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> contactBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInContactBook_returnsFalse() {
        assertFalse(contactBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInContactBook_returnsTrue() {
        contactBook.addPerson(ALICE);
        assertTrue(contactBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInContactBook_returnsTrue() {
        contactBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE)
                .withOrganisation(VALID_ORGANISATION_BOB)
                .withCategories(VALID_CATEGORY_HUSBAND)
                .build();
        assertTrue(contactBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> contactBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = ContactBook.class.getCanonicalName() + "{persons=" + contactBook.getPersonList() + "}";
        assertEquals(expected, contactBook.toString());
    }

    /**
     * A stub ReadOnlyContactBook whose persons list can violate interface constraints.
     */
    private static class ContactBookStub implements ReadOnlyContactBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        ContactBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
