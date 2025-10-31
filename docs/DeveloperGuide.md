---
layout: default.md
title: Developer Guide
pageNav: 3
---
<style>
/* Make all H2 big & dark */
h2 {
  color: #0C3878 !important;
  font-size: 3rem !important;
  font-weight: 800 !important;
  line-height: 1.2;
  margin-top: 2rem;
  margin-bottom: .9rem;
}
</style>

<style>
h1 {
  color: #E84C23 !important;
  font-size: 4rem !important;
  font-weight: 800 !important;
  line-height: 1.2;
  margin-top: 2rem;
  margin-bottom: .9rem;
}
</style>

![PressPalAbout](images/PressPalAbout.png)

# **Developer Guide**

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level 3 project created by the School of Computing, National University of Singapore.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `ContactBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `ContactBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `ContactBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="800" />


The `Model` component,

* stores the contact book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both contact book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `ContactBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `presspal.contact.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedContactBook`. It extends `ContactBook` with an undo/redo history, stored internally as an `contactBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedContactBook#commit()` — Saves the current contact book state in its history.
* `VersionedContactBook#undo()` — Restores the previous contact book state from its history.
* `VersionedContactBook#redo()` — Restores a previously undone contact book state from its history.

These operations are exposed in the `Model` interface as `Model#commitContactBook()`, `Model#undoContactBook()` and `Model#redoContactBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedContactBook` will be initialized with the initial contact book state, and the `currentStatePointer` pointing to that single contact book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the contact book. The `delete` command calls `Model#commitContactBook()`, causing the modified state of the contact book after the `delete 5` command executes to be saved in the `contactBookStateList`, and the `currentStatePointer` is shifted to the newly inserted contact book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitContactBook()`, causing another modified contact book state to be saved into the `contactBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitContactBook()`, so the contact book state will not be saved into the `contactBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoContactBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous contact book state, and restores the contact book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial ContactBook state, then there are no previous ContactBook states to restore. The `undo` command uses `Model#canUndoContactBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoContactBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the contact book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `contactBookStateList.size() - 1`, pointing to the latest contact book state, then there are no undone ContactBook states to restore. The `redo` command uses `Model#canRedoContactBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the contact book, such as `list`, will usually not call `Model#commitContactBook()`, `Model#undoContactBook()` or `Model#redoContactBook()`. Thus, the `contactBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitContactBook()`. Since the `currentStatePointer` is not pointing at the end of the `contactBookStateList`, all contact book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire contact book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Primary users:
   * Breaking-news reporters in Singapore mainstream newsrooms who often handle a large number of contacts from different organisations and roles.

* Context:
   * They work under tight schedules, sometimes in the newsroom but often on-the-go, needing to recall and update information quickly while balancing multiple developing stories.

* Habits & skills:
   * Prefer desktop/laptop applications for speed.
   * Comfortable with command-line input and type quickly, preferring keyboard over mouse-driven interactions.
   * Used to handling structured yet scattered information (contacts, roles, organisations, interviews).

**Value proposition**: PressPal helps reporters manage scattered contacts with speed. It categorises relationships by role and organisation, tracks interviews and notes, and offers reminders for follow-ups. With timeline views, archiving contacts after each news, and smart command suggestions, it streamlines fast entry and retrieval, keeping reporters organised while chasing breaking stories.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …                              | I can …                                                                                     | So that I can…                                                            |
|-------|-------------------------------------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------|
| `* * *` | basic user                          | create new contacts with the add contact functions (name, phone, email, organisation, role) | retrieve the contact easily when I need them                              |
| `* * *` | basic user                          | add category(s) to a contact                                                                | search the contact with the category(s) for easy reference                |
| `* * *` | basic user                          | add an interview to a contact with details (date, time, location, header)                   | easily track and know my interview schedule with a specific person        |
| `* * *` | basic user                          | delete contacts from the contact book                                                       | keep my contact book organised                                            |
| `* * *` | basic user                          | delete category(s) from a contact                                                           | keep my contact book organised                                            |
| `* * *` | basic user                          | delete an interview from a contact                                                          | keep my schedule up to date and avoid confusion from outdated information |
| `* * *` | basic user                          | search contacts by their names                                                              | quickly find the specific person I’m looking for                          |
| `* * *` | basic user                          | see a list of all persons in the contact book                                               | check all my contacts in the contact book at once                         |
| `* * *` | basic user                          | see a list of all interviews of a contact                                                   | check all my interviews with a specific person                            |
| `* * *` | advanced user/ fast typer           | utilise fast-typing shortcuts during data entry                                             | enter information quickly and reduce wait times                           |
| `* *` | beginner, basic user, advanced user | work with an intuitive user interface                                                       | can work more efficiently                                                 |
| `* *` | basic user, beginner                | have error-prevention features built into the data entry process                            | record the contact correctly the first time                               |
| `* *` | beginner                            | view the user guide easily                                                                  | learn more about the product                                              |
| `* *` | basic user                          | see my next upcoming interview across all scheduled interviews                              | quickly know when and with whome my next interview is happening           |
| `* *` | basic user                          | edits an existing person in the contact book (name, phone, email, organisation, role)       | keep my contacts updated in the contact book                              |
| `* *` | basic user                          | search contacts by their organisation                                                       | find a person from a specific organisation                                |
| `* *` | basic user                          | search contacts by their roles                                                              | find a person with related role                                           |
| `* *` | basic user                          | search contacts by their categories                                                         | find a person with related categories                                     |
| `*`   | long-time user                      | archive contacts once a story wraps up                                                      | keep my dashboard uncluttered                                             |

## **Use cases**

The use cases below are not exhaustive. (For all use cases below, the **System** is the `PressPal` and the **Actor** is the `Reporter`, unless specified otherwise)

### UC1 – Add a contact

**MSS**

1. Reporter requests to add a contact with name, at least one mode of contact (phone or email), organisation and role.
2. PressPal validates all provided fields.
3. PressPal creates the contact and confirms.

   Use case ends.

**Extensions**

* 1a. Reporter omits one or more required fields (name, phone OR email, organisation, role).
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid field (e.g., name/phone/email format).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 2b. PressPal detects that the contact name already exists.
  * 2b1. PressPal shows a duplicate contact error message.  
    Use case resumes at step 1.

* 2c. PressPal detects a repeated parameter (e.g., two phone fields).
  * 2c1. PressPal shows a repeated parameter error message.  
    Use case resumes at step 1.

### UC2 – Add category to contact

**MSS**

1. Reporter requests to add one or more categories to a contact, specifying the contact’s index.
2. PressPal validates the contact index and each category name.
3. PressPal checks if each category already exists for that contact.
4. PressPal adds all valid, non-duplicate categories to the contact and confirms.

   Use case ends.

**Extensions**

* 1a. Reporter omits the contact index or category name(s).
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid contact index (e.g., out of range or non-integer).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 2b. PressPal detects an invalid category name (e.g., empty, non-alphabetic, or exceeds 20 characters).
  * 2b1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 3a. One or more categories already exist for the contact.
  * 3a1. PressPal informs the Reporter that duplicate categories cannot be added.
    Use case ends.

* 3b. All specified categories already exist.
  * 3b1. PressPal informs the Reporter that duplicate categories cannot be added.
  * 3b2. No new categories were added.  
    Use case ends.

### UC3 – Add interview to contact

**MSS**

1. Reporter requests to add an interview to a contact by specifying the contact index, interview header, date, time, and location.
2. PressPal validates all input fields (index, header, date, time, and location).
3. PressPal checks if the contact already has an interview at the same date and time.
4. PressPal adds the interview and confirms the addition.  

   Use case ends.

**Extensions**

* 1a. Reporter omits one or more required fields (index, header, date, time, or location).
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid field format (e.g., invalid date/time format, out of range or non-integer index).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 3a. The contact already has an interview scheduled at the same date and time.
  * 3a1. PressPal shows an error message that an interview conflict exists. 
    Use case resumes at step 1.

* 3b. The specified contact index does not exist.
  * 3b1. PressPal informs the Reporter that the index is invalid.  
    Use case resumes at step 1.

### UC4 – Delete a contact

**MSS**

1. Reporter requests to delete a person from the contact book by specifying the contact index.
2. PressPal validates the contact index.
3. PressPal deletes the corresponding person from the contact book and confirms the deletion.

   Use case ends.

**Extensions**

* 1a. Reporter omits the index.
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid index (e.g., non-integer or out of range).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 3a. Storage error occurs during deletion.
  * 3a1. PressPal shows an error message indicating that the deletion failed and the contact is retained.  
    Use case ends.

### UC5 – Delete category from contact

**MSS**

1. Reporter requests to delete one or more categories from a person by specifying the person’s index and category name(s).
2. PressPal validates the contact index and each category name.
3. PressPal checks whether the specified categories exist for that person.
4. PressPal deletes all existing categories and confirms the deletion.

   Use case ends.

**Extensions**

* 1a. Reporter omits the index or category name(s).
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid index (e.g., non-integer or out of range).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 2b. PressPal detects an invalid category name (e.g., non-alphabetic, or exceeds 20 characters).
  * 2b1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 3a. One or more specified categories do not exist for that person.
  * 3a1. PressPal informs the Reporter that the category(s) was not found.
  * 3a2. PressPal failed to delete the category(s).  
    Use case ends.

* 3b. None of the specified categories exist.
  * 3b1. PressPal informs the Reporter that no matching categories were found.  
    Use case ends.

### UC6 – Delete interview from contact

**MSS**

1. Reporter requests to delete an interview from a contact by specifying both the person index and the interview index.
2. PressPal validates the person index and interview index.
3. PressPal deletes the specified interview and confirms the deletion.

   Use case ends.

**Extensions**

* 1a. Reporter omits the person index or interview index.
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid person index (e.g., non-integer or out of range).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 2b. PressPal detects an invalid interview index (e.g., non-integer or out of range).
  * 2b1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 3a. The specified contact has no interviews or the specified interview does not exist.
  * 3a1. PressPal informs the Reporter that the interview cannot be found.  
    Use case ends.

### UC7 – Find Persons by Name, Organisation, Role, or Categories

**MSS**

1. Reporter requests to find persons by providing one or more keywords.
2. PressPal searches the contact book for matches in the name, organisation, role, or categories fields.
3. PressPal displays a list of all persons that match at least one keyword (case-insensitive).

   Use case ends.

**Extensions**

* 1a. Reporter omits the keyword(s).
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. No persons match the provided keyword(s).
  * 2a1. PressPal informs the Reporter that no matching results were found.  
    Use case ends.

### UC8 – List all interviews of a contact

**MSS**

1. Reporter requests to view all interviews of a contact by specifying the contact index.
2. PressPal validates the contact index.
3. PressPal retrieves and displays all interviews associated with the contact.  
   
   Use case ends.

**Extensions**

* 1a. Reporter omits the contact index.
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid index (e.g., non-integer or out of range).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 3a. The specified contact has no interviews.
  * 3a1. PressPal displays a message indicating that there are no interviews scheduled.  
    Use case ends.

### UC9 – List all persons

**MSS**

1. Reporter requests to view all persons in the contact book.
2. PressPal retrieves and displays the full list of persons stored in the contact book.  
 
   Use case ends.

**Extensions**

* 1a. The contact book is empty.
  * 1a1. PressPal informs the Reporter that there are no persons in the contact book.  
    Use case ends.

### UC10 – Edit a person

**MSS**

1. Reporter requests to edit an existing person by specifying the contact index and one or more fields to update.
2. PressPal validates the contact index and the provided field values.
3. PressPal updates the person’s details with the new information.
4. PressPal confirms that the person has been successfully updated.

   Use case ends.

**Extensions**

* 1a. Reporter omits the contact index.
  * 1a1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 1b. Reporter omits all optional fields.
  * 1b1. PressPal shows an invalid command error message.  
    Use case resumes at step 1.

* 2a. PressPal detects an invalid index (e.g., non-integer or out of range).
  * 2a1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

* 2b. PressPal detects invalid field formats (e.g., invalid email, phone number, or role/organisation exceeding character limits).
  * 2b1. PressPal shows an invalid parameters error message.  
    Use case resumes at step 1.

### UC11 – Display the upcoming interview

**MSS**

1. Reporter requests to view the next upcoming interview.
2. PressPal retrieves all scheduled interviews and filters out those that have already occurred (i.e., past interviews).
3. PressPal identifies the interview that is scheduled to occur next (closest future date and time).
4. PressPal displays the details of the next upcoming interview to the Reporter.  
   Use case ends.

   Use case ends.

**Extensions**

* 1a. The contact book contains no interviews.
  * 1a1. PressPal informs the Reporter that there are no scheduled interviews.  
    Use case ends.

* 2a. All interviews in the contact book have already occurred.
  * 2a1. PressPal displays a message indicating that there are no upcoming interviews.  
    Use case ends.

* 3a. Multiple upcoming interviews are scheduled at the same date and time.
  * 3a1. PressPal displays any one of them as the next interview, following priority of the person index.  
    Use case ends.

## Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

## Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
  This glossary defines key terms used within **PressPal**, a platform designed for breaking news journalists to manage and organize their contact information efficiently.
* **Account**: A user profile in PressPal that stores a journalist’s credentials, preferences, and contact database.
* **Category**: A label or grouping used to organize contacts (e.g., “Politics,” “Health,” “Technology”).
* **Contact**: An individual or organization entry stored in PressPal, including name, role, and at least one mode of contact (e.g., phone or email).
* **Duplicate contact**: A contact that shares the same phone number or email as an existing contact in PressPal.
* **Mode of contact**: A way to reach a contact, such as a phone number or email organisation.
* **Reporter**: The primary user of PressPal who manages contacts, categories, and interview notes.
* **Role**: The position or job title of a contact within their organisation.
* **Organisation**: The company, agency, or institution a contact belongs to.
* **MSS (Main Success Scenario)**: The sequence of steps that lead to a successful execution of a use case.
* **JAR**:  A Java Archive file that contains the compiled Java classes and resources for the application.

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1a. Download the jar file and copy into an empty folder

   1b. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   2a. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2b. Re-launch the app by double-clicking the jar file.<br>
       
   * Expected: The most recent window size and location is retained.


### Adding a person

**Command**: `add`

1. Adding a person with <u>all valid fields</u>
    * **Test Case:** `add n/John Doe p/12345678 e/johnd@example.com o/NUS r/TA c/Student`
    * **Expected:** Person is added to the contact book. Details of the new person is shown in the status message.
      <br><br>
2. Adding a person with missing <u>mandatory fields</u>
    * **Test Case:** `add p/12345678 e/johnd@example.com o/NUS r/TA c/Student`
    * **Expected:** Person is not added. Error details shown in the status message.
      <br><br>
3. Adding a person with invalid <u>Organisation format</u>
    * **Test Case:** `add n/John Doe p/12345678 e/johnd@example.com o/NUS-C r/TA`
    * **Expected:** Person is not added. Error details shown in the status message.
      <br><br>

### Editing a person

**Command:** `edit`

1. Editing an <u>existing contact</u>
    * **Prerequisite:** Ensure at least one contact exists in the list. If not, add a contact using the `add` command. E.g `add n/John Doe p/98765432 e/johnd@gmail.com o/NUS r/Student c/friends`.
    * **Test case:** `edit i/1 n/Jason Doe p/88888888`
    * **Expected:** Index 1 contact's name is changed to "Jason Doe" and phone to "88888888". Details of the edited contact shown in the status message.
    <br><br>
2. Editing a contact when the contact book is <u>empty</u>
    * **Prerequisite:** Ensure the contact book is empty. Run `clear` to clear the contact book.
    * **Test case:** `edit i/1 n/John Doe`
    * **Expected:** No contact is edited. Error details shown in the status message.
    <br><br>
3. Editing a contact with <u>invalid email</u>
    * **Prerequisite:** Ensure at least one contact exists in the list. If not, add a contact using the `add` command. E.g `add n/John Doe p/98765432 e/johnd@gmail.com o/NUS r/Student c/friends`.
    * **Test case:** `edit i/1 n/John Doe e/johndoe.com`
    * **Expected:** No contact is edited. Error details shown in the status message.
    <br><br>
4. Editing a contact with <u>invalid index</u>
    * **Prerequisite:** Ensure at least one contact exists in the list. If not, add a contact using the `add` command. E.g `add n/John Doe p/98765432 e/johnd@gmail.com o/NUS r/Student c/friends`.
    * **Test case:** `edit i/0 n/John Doe`
    * **Expected:** No contact is edited. Error details shown in the status message.
    <br><br>
5. Editing a contact <u>without any field</u>
    * **Prerequisite:** Ensure at least one contact exists in the list. If not, add a contact using the `add` command. E.g `add n/John Doe p/98765432 e/johnd@gmail.com o/NUS r/Student c/friends`.
    * **Test case:** `edit i/1`
    * **Expected:** No contact is edited. Error details shown in the status message.
    <br><br>

### Deleting a person

**Command**: `delete`

**Prerequisites**: Ensure at least one person exists in the contact book.

1. Deleting a person with a <u>valid index</u>
    * **Test case:** `delete i/1 `
    * **Expected:** Confirmation message shown that person at index 1 is deleted from the list.
      <br><br>
2. Deleting a person with <u>out of bounds index</u>
    * **Test case:** `delete i/0`
    * **Expected:** Error message shown indicating invalid index.
      <br><br>
3. Deleting a person <u>without any index</u>
    * **Test case:** `delete`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>
4. Deleting a person with a valid index with <u>additional prefixes</u>
    * **Test case:** `delete i/1 n/John`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>

### Listing All Persons

**Command**: `list`

1. Listing all persons when there are <u>existing contacts</u>
   * **Prerequisite:** The contact book contains at least one person (e.g., `John Doe`, `Alex Yeoh`).
   * **Test Case:** `list`
   * **Expected:** The system displays a list of all persons currently stored in the contact book. Both `John Doe` and `Alex Yeoh` are displayed in the result panel.
     <br><br>
2. Listing all persons when the contact book is <u>empty</u>
   * **Prerequisite:** The contact book contains no persons.
   * **Test Case:** `list`
   * **Expected:** The system shows an empty list. `No contact available.` is displayed.
     <br><br>

### Locating Persons by Name, Organisation, Role, or Categories

**Command**: `find`

1. Finding persons by keyword <u>name</u>
    * **Prerequisite:** The contact book contains at least one person with the name `John Doe`.
    * **Test Case:** `find John`
    * **Expected:** The system lists all persons whose `name` contains the full word `John`. Example: `John Doe` is displayed. Status bar shows `1 person listed!`.
      <br><br>
2. Finding persons by <u>multiple keywords</u>
    * **Prerequisite:** The contact book contains persons `Bernice Yu` and `Charlotte Oliveira`.
    * **Test Case:** `find bernice charlotte`
    * **Expected:** The system lists both `Bernice Yu` and `Charlotte Oliveira`. Status bar shows `2 persons listed!`. The search behaves as an OR search.
      <br><br>
3. <u>Case-insensitive</u> search
    * **Prerequisite:** The contact book contains `Alex Yeoh`.
    * **Test Case:** `find AlEx`
    * **Expected:** The system lists `Alex Yeoh`. The search is case-insensitive.
      <br><br>
4. <u>Order</u> of keywords does not matter
    * **Prerequisite:** The contact book contains `Hans Gruber`.
    * **Test Case:** `find Gruber Hans`
    * **Expected:** The system lists `Hans Gruber`. The order of keywords does not affect search results.
      <br><br>
5. <u>Partial word</u> should not match
    * **Prerequisite:** The contact book contains `Hans Gruber` and `Han Solo`.
    * **Test Case:** `find Han`
    * **Expected:** `Hans Gruber` is **not** listed since `Han` is only part of the name. `Han Solo` is listed since `Han` is a full word in that name.
      <br><br>
6. Finding persons by keyword <u>organisation</u>
    * **Prerequisite:** The contact book contains `Alice Tan` with organisation `Google`.
    * **Test Case:** `find Google`
    * **Expected:** `Alice Tan` is listed because her organisation contains the keyword `Google`.
      <br><br>
7. Finding persons by keyword <u>role</u>
    * **Prerequisite:** The contact book contains `David Li` with the role `Manager`.
    * **Test Case:** `find manager`
    * **Expected:** `David Li` is listed since his role matches the keyword `manager`.
      <br><br>
8. Finding persons by keyword <u>category</u>
    * **Prerequisite:** The contact book contains `Alex Yeoh` with category `family`.
    * **Test Case:** `find family`
    * **Expected:** `Alex Yeoh` is listed because the category `family` matches the keyword.
      <br><br>
9. Searching with a <u>non-existent keyword</u>
    * **Prerequisite:** No person has any field matching `Kieron`.
    * **Test Case:** `find Kieron`
    * **Expected:** No results displayed. Status bar shows `0 persons listed!`.
      <br><br>
10. Handling <u>extra spaces</u> in input
    * **Prerequisite:** The contact book contains `David Li`.
    * **Test Case:** `find   david    `
    * **Expected:** `David Li` is listed normally. Leading or trailing spaces are ignored.
      <br><br>
11. <u>Empty</u> input
    * **Prerequisite:** Any state of the contact book.
    * **Test Case:** `find`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>

### Add interview(s) to a person

**Command**: `addInterview`

**Prerequisites**: Ensure at least one person exists in the contact book.
1. Adding a <u>valid interview</u> to a person
    * **Test case:** `addInterview i/1 h/Interview with ABC Corp d/2024-10-10 t/14:00 l/123, Business St, #02-25`
    * **Expected:** Confirmation message shown that the interview has been added to the person at index 1.
      <br><br>
2. Adding interview with <u>invalid index</u>
    * **Test case:** `addInterview i/-1 h/Interview with ABC Corp d/2024-10-10 t/14:00 l/123, Business St, #02-25`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>
3. Adding interview with <u>empty header</u> to a person
    * **Test case:** `addInterview i/1 h/ d/2024-10-10 t/14:00 l/123, Business St, #02-25`
    * **Expected:** Error message shown indicating header cannot be blank.
      <br><br>
4. Adding interview with <u>invalid date</u> to a person
    * **Test case:** `addInterview i/1 h/Interview with ABC Corp d/24-10-10 t/14:00 l/123, Business St, #02-25`
    * **Expected:** Error message shown indicating invalid date.
      <br><br>
5. Adding interview with <u>invalid time</u> to a person
    * **Test case:** `addInterview i/1 h/Interview with ABC Corp d/2024-10-10 t/1400 l/123, Business St, #02-25`
    * **Expected:** Error message shown indicating invalid time.
      <br><br>
6. Adding interview with <u>empty location</u> to a person
    * **Test case:** `addInterview i/1 h/Interview with ABC Corp d/2024-10-10 t/14:00 l/`
    * **Expected:** Error message shown indicating location cannot be blank.
      <br><br>
7. Adding interview with <u>missing parameters</u> to a person
    * **Test case:** `addInterview i/1 h/Interview with ABC Corp d/2024-10-10 t/14:00`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>
8. Adding interview with <u>additional parameters</u> to a person
    * **Test case:** `addInterview i/1 h/Interview with ABC Corp d/2024-10-10 t/14:00 l/123, Business St, #02-25 c/formal`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>

### Delete interview(s) to a person

**Command**: `deleteInterview`

**Prerequisites**: Ensure at least one person exists in the contact book.

1. Deleting a <u>valid interview</u> from a person
    * **Test case:** `deleteInterview i/1 v/2`
    * **Expected:** Confirmation message shown that the interview has been deleted from the person at 1.
      <br><br>
2. Deleting an interview with a <u>invalid person index</u>
    * **Test case:** `deleteInterview i/0 v/2`
    * **Expected:** Error message shown indicating invalid index.
      <br><br>
3. Deleting an interview with a <u>invalid interview index</u>
    * **Test case:** `deleteInterview i/1 v/-2`
    * **Expected:** Error message shown indicating invalid index.
      <br><br>
4. Deleting an interview with a <u>missing parameters</u>
    * **Test case:** `deleteInterview i/1`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>
5. Deleting an interview with a <u>additional parameters</u>
    * **Test case:** `deleteInterview i/1 v/1 h/Interview with ABC Corps`
    * **Expected:** Error message shown indicating invalid command format.
      <br><br>

### Display the list of interview(s) for a person

**Command**: `listInterview`

1. Displaying a person's interview(s)
    * **Prerequisites:** Ensure the person at index `1` exists in the contact book with at least one interview.
    * **Test Case:** `listInterview i/1`
    * **Expected:** Success message showing the person's list of interview(s).
      <br><br>
2. Displaying <u>an empty list of</u> interview(s) for a person
    * **Prerequisites:** Ensure that the person at index `1` has no interview(s).
    * **Test Case:** `listInterview i/1`
    * **Expected:** Error message shown indicating the person has no interview(s) scheduled.
      <br><br>

### Display the upcoming interview

**Command**: `nextInterview`

1. Displaying the next upcoming interview in the contact book
    * **Prerequisites:** Ensure at least one person exists in the contact book with an interview date of later than present time.
    * **Test Case:** `nextInterview`
    * **Expected:** Success message showing the next upcoming interview.
      <br><br>
2. Displaying the next upcoming interview for an empty contact book
    * **Prerequisites:** Ensure that the contact book is empty by using `clear` command.
    * **Test Case:** `nextInterview`
    * **Expected:** Error message shown indicating the contact book is empty.
      <br><br>

### Add category(s) to a person

**Command**: `addCat`

**Prerequisites**: Ensure at least one person exists in the contact book.

1. Adding a <u>valid category</u> to a person
   * **Test case:** `addCat i/1 c/friend`
   * **Expected:** Confirmation message shown that category 'friend' is added to the person at index 1.
     <br><br>
2. Adding <u>valid categories</u> to a person
   * **Test case:** `addCat i/1 c/friend c/work`
   * **Expected:** Confirmation message shown that categories 'friend' and 'work' are added to the person at index 1.
     <br><br>
3. Adding an <u>invalid category</u> to a person
    * **Test case:** `addCat i/1 c/!`
    * **Expected:** Error message shown indicating invalid category name.
      <br><br>
4. Adding an <u>empty category</u> to a person
   * **Test case:** `addCat i/1 c/`
   * **Expected:** Error message shown indicating invalid category name.
     <br><br>
5. Adding a category to a <u>non-existent person</u>
    * **Test case:** `addCat i/999 c/family`
    * **Expected:** Error message shown indicating person not found.
      <br><br>

### Delete category(s) from a person

**Command**: `deleteCat`

**Prerequisites**: Ensure at least one person exists in the contact book.

1. Deleting a <u>valid category</u> from a person
    * **Test case:** `deleteCat i/1 c/friend`
    * **Expected:** Success message shown that category 'friend' is deleted from the person at index 1.
      <br><br>
2. Deleting <u>valid categories</u> from a person
    * **Test case:** `deleteCat i/1 c/friend c/work`
    * **Expected:** Success message shown that categories 'friend' and 'work' are deleted from the person at index 1.
      <br><br>
3. Deleting an <u>invalid category</u> from a person
    * **Test case:** `deleteCat i/1 c/!`
    * **Expected:** Error message shown indicating invalid category name.
      <br><br>
4. Deleting an <u>empty category</u> from a person
    * **Test case:** `deleteCat i/1 c/`
    * **Expected:** Error message shown indicating invalid category name.
      <br><br>
5. Deleting a category from a <u>non-existent person</u>
    * **Test case:** `deleteCat i/999 c/family`
    * **Expected:** Error message shown indicating person not found.














