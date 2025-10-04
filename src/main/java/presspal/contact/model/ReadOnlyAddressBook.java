package presspal.contact.model;

import javafx.collections.ObservableList;
import presspal.contact.model.person.Person;

/**
 * Unmodifiable view of an contact book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
