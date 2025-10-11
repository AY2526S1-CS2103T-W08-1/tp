package presspal.contact.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static presspal.contact.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OrganisationTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Organisation(null));
    }

    @Test
    public void constructor_invalidOrganisation_throwsIllegalArgumentException() {
        String invalidOrganisation = "";
        assertThrows(IllegalArgumentException.class, () -> new Organisation(invalidOrganisation));
    }

    @Test
    public void isValidOrganisation() {
        // null address
        assertThrows(NullPointerException.class, () -> Organisation.isValidOrganisation(null));

        // invalid addresses
        assertFalse(Organisation.isValidOrganisation("")); // empty string
        assertFalse(Organisation.isValidOrganisation(" ")); // spaces only

        // valid addresses
        assertTrue(Organisation.isValidOrganisation("CAPT"));
        assertTrue(Organisation.isValidOrganisation("A")); // one character
        assertTrue(Organisation.isValidOrganisation("College of Alice & Peter Tan")); // long organisation
    }

    @Test
    public void equals() {
        Organisation organisation = new Organisation("Valid Organisation");

        // same values -> returns true
        assertTrue(organisation.equals(new Organisation("Valid Organisation")));

        // same object -> returns true
        assertTrue(organisation.equals(organisation));

        // null -> returns false
        assertFalse(organisation.equals(null));

        // different types -> returns false
        assertFalse(organisation.equals(5.0f));

        // different values -> returns false
        assertFalse(organisation.equals(new Organisation("Other Valid Organisation")));
    }
}
