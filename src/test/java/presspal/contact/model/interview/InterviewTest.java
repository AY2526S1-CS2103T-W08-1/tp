package presspal.contact.model.interview;


import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link Interview}.
 */
/**
 * Unit tests for the {@link Interview} class.
 */
class InterviewTest {

    private Interview interview;
    private Header header;
    private Location location;
    private LocalDateTime dateTime;

    @BeforeEach
    void setUp() {
        // Create Header and Location
        header = new Header("Interview with Alice");
        location = new Location("NUS Enterprise");
        dateTime = LocalDateTime.of(2025, 10, 15, 14, 30);

        // Construct Interview
        interview = new Interview(header, location, dateTime);
    }

    @Test
    @DisplayName("Constructor should correctly initialize all fields")
    void testConstructorInitializesFields() {
        assertEquals(header, interview.getHeader(),
                "Constructor should correctly set the header");
        assertEquals(location, interview.getLocation(),
                "Constructor should correctly set the location");
        assertEquals(dateTime, interview.getDateTime(),
                "Constructor should correctly set the dateTime");
    }

    @Test
    @DisplayName("Getter should return the header value")
    void testGetHeader() {
        assertEquals(header, interview.getHeader(),
                "getHeader() should return the same Header passed to the constructor");
    }

    @Test
    @DisplayName("Getter should return the location value")
    void testGetLocation() {
        assertEquals(location, interview.getLocation(),
                "getLocation() should return the same Location passed to the constructor");
    }

    @Test
    @DisplayName("Getter should return the date and time value")
    void testGetDateTime() {
        assertEquals(dateTime, interview.getDateTime(),
                "getDateTime() should return the same LocalDateTime passed to the constructor");
    }

    @Test
    @DisplayName("toString() should include all key fields")
    void testToString() {
        String result = interview.toString();
        // Basic presence checks to avoid overly brittle tests
        assertTrue(result.contains("Interview{"), "toString() should start with 'Interview{'");
        assertTrue(result.contains("dateTime="), "toString() should include dateTime field");
        assertTrue(result.contains("header="), "toString() should include header field");
        assertTrue(result.contains("location="), "toString() should include location field");
    }

    @Test
    @DisplayName("Constructor should allow null header or location")
    void testConstructorAllowsNulls() {
        Interview interviewWithNulls = new Interview(null, null, dateTime);
        assertNull(interviewWithNulls.getHeader(),
                "Header should be allowed to be null");
        assertNull(interviewWithNulls.getLocation(),
                "Location should be allowed to be null");
        assertEquals(dateTime, interviewWithNulls.getDateTime(),
                "DateTime should still be correctly set");
    }

}