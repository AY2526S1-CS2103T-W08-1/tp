package presspal.contact.model.interview;

import java.time.LocalDateTime;
import presspal.contact.model.person.Person;

/**
 * Represents an interview session containing details about the header, location,
 * and scheduled date and time.
 *
 * <p>This class provides accessors to retrieve contact details (via the Header),
 * the interview's metadata, and its scheduled time.</p>
 */
public class Interview {
    /** The scheduled date and time of the interview. */
    private final LocalDateTime dateTime;

    /** The header information of the interview, including contact details. */
    private final Header header;

    /** The location details where the interview will be held. */
    private final Location location;

    /**
     * Constructs an {@code Interview} object with the given header, location, and date-time.
     *
     * @param header   the {@link Header} containing interview contact details and summary info
     * @param location the {@link Location} specifying where the interview will take place
     * @param dateTime the {@link LocalDateTime} representing when the interview is scheduled
     */
    public Interview(Header header, Location location, LocalDateTime dateTime) {
        this.header = header;
        this.location = location;
        this.dateTime = dateTime;
    }

    /**
     * Returns the header information of this interview.
     *
     * @return the {@link Header} of this interview
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Returns the scheduled date and time of the interview.
     *
     * @return the {@link LocalDateTime} when the interview is scheduled
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the location of this interview.
     *
     * @return the {@link Location} of the interview
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "dateTime=" + dateTime +
                ", header=" + header +
                ", location=" + location +
                '}';
    }
}


