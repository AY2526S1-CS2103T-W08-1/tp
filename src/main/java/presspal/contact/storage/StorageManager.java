package presspal.contact.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import presspal.contact.commons.core.LogsCenter;
import presspal.contact.commons.exceptions.DataLoadingException;
import presspal.contact.model.ReadOnlyContactBook;
import presspal.contact.model.ReadOnlyUserPrefs;
import presspal.contact.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ContactBookStorage contactBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code AddressBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(ContactBookStorage contactBookStorage, UserPrefsStorage userPrefsStorage) {
        this.contactBookStorage = contactBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getContactBookFilePath() {
        return contactBookStorage.getContactBookFilePath();
    }

    @Override
    public Optional<ReadOnlyContactBook> readContactBook() throws DataLoadingException {
        return readContactBook(contactBookStorage.getContactBookFilePath());
    }

    @Override
    public Optional<ReadOnlyContactBook> readContactBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return contactBookStorage.readContactBook(filePath);
    }

    @Override
    public void saveContactBook(ReadOnlyContactBook contactBook) throws IOException {
        saveContactBook(contactBook, contactBookStorage.getContactBookFilePath());
    }

    @Override
    public void saveContactBook(ReadOnlyContactBook contactBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        contactBookStorage.saveContactBook(contactBook, filePath);
    }

}
