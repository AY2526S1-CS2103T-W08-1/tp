package presspal.contact.model.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Location} class.
 */
class LocationTest {

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location("NUS Enterprise");
    }

    @Test
    @DisplayName("Constructor should correctly initialize location")
    void testConstructorInitializesLocation() {
        assertEquals("NUS Enterprise", location.getLocation(),
                "Constructor should set the correct location value");
    }

    @Test
    @DisplayName("Getter should return the current location value")
    void testGetLocation() {
        assertEquals("NUS Enterprise", location.getLocation(),
                "getLocation() should return the stored location string");
    }

    @Test
    @DisplayName("Setter should update the location value")
    void testSetLocation() {
        location.setLocation("Zoom Meeting");
        assertEquals("Zoom Meeting", location.getLocation(),
                "setLocation() should update the location value correctly");
    }

    @Test
    @DisplayName("Setter should handle empty string as location")
    void testSetLocationEmptyString() {
        location.setLocation("");
        assertEquals("", location.getLocation(),
                "Location should support empty string as a valid value");
    }

    @Test
    @DisplayName("Setter should handle null as location")
    void testSetLocationNull() {
        location.setLocation(null);
        assertNull(location.getLocation(),
                "Location should allow null values without throwing exceptions");
    }

    @Test
    void testEqualsAndHashCode() {
        Location loc1 = new Location("Zoom");
        Location loc2 = new Location("Zoom");
        Location loc3 = new Location("Google Meet");

        // equals
        assertEquals(loc1, loc2);
        assertNotEquals(loc1, loc3);

        // hashCode
        assertEquals(loc1.hashCode(), loc2.hashCode());
        assertNotEquals(loc1.hashCode(), loc3.hashCode());
    }

    @Test
    @DisplayName("Equals with null and different type")
    void testEqualsWithNullAndDifferentType() {
        Location loc = new Location("Zoom");
        assertNotEquals(null, loc);
        assertNotEquals("Zoom String", loc);
    }

}
