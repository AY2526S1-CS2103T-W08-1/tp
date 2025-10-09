package presspal.contact.model.interview;

/**
 * Represents the location of interview.
 */
public class Location {
    /** The textual representation of the interview location. */
    private String location;

    /** Constructs a Location object */
    public Location(String location) {
        this.location = location;
    }

    /** Returns the interview location as a String */
    public String getLocation() {
        return this.location;
    }

    /** Updates the interview location to a new specified value */
    public void setLocation(String newLocation) {
        this.location = newLocation;
    }
}
