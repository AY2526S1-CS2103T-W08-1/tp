package presspal.contact.testutil;

import java.util.HashSet;
import java.util.Set;

import presspal.contact.model.category.Category;
import presspal.contact.model.person.Email;
import presspal.contact.model.person.Name;
import presspal.contact.model.person.Organisation;
import presspal.contact.model.person.Person;
import presspal.contact.model.person.Phone;
import presspal.contact.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ORGANISATION = "NUS";

    private Name name;
    private Phone phone;
    private Email email;
    private Organisation organisation;
    private Set<Category> categories;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        organisation = new Organisation(DEFAULT_ORGANISATION);
        categories = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        organisation = personToCopy.getOrganisation();
        categories = new HashSet<>(personToCopy.getCategories());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withCategories(String ... categories) {
        this.categories = SampleDataUtil.getCategorySet(categories);
        return this;
    }

    /**
     * Sets the {@code Organisation} of the {@code Person} that we are building.
     */
    public PersonBuilder withOrganisation(String organisation) {
        this.organisation = new Organisation(organisation);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, organisation, categories);
    }

}
