package ch.epfl.sweng.project;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class LocationTest {
    private String name;
    private Location location;
    private Location defaultLocation;
    private double latitude;
    private double longitude;
    private Location.LocationType type;


    @Rule
    public final ExpectedException thrownException = ExpectedException.none();

    @Before
    public void initValidLocation() {
        name = "name";
        type = Location.LocationType.HOME;
        latitude = 41;
        longitude = 38;
        location = new Location(name, type.toString(), latitude, longitude);
    }

    @Before
    public void testDefaultConstructor() {
        defaultLocation = new Location();
    }

    @Test
    public void packageNameIsCorrect() {
        final Context context = getTargetContext();
        assertThat(context.getPackageName(), is("ch.epfl.sweng.project"));
    }

    /**
     * Test that the getters return the good value
     */
    @Test
    public void testLocationGetters() {
        assertEquals(name, location.getName());
        assertEquals(type, location.getType());
        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
    }

    /**
     * Test that the getters for "everywhere" location return the good value
     */
    @Test
    public void testDefaultLocationGetters() {
        String expectedName = "Everywhere";
        double expectedLat = 0;
        double expectedLong = 0;
        assertEquals(expectedName, defaultLocation.getName());
        assertEquals(Location.LocationType.EVERYWHERE, defaultLocation.getType());
        assertEquals(expectedLat, defaultLocation.getLatitude());
        assertEquals(expectedLong, defaultLocation.getLongitude());
    }

    /**
     * Test that the setters modify correctly the location
     */
    @Test
    public void testLocationSetters() {
        Location newLocation = new Location();

        String newName = "EPFL";
        double newLat = 20;
        double newLong = 45;
        Location.LocationType newLocType = Location.LocationType.HOME;

        newLocation.setName(newName);
        newLocation.setLatitude(newLat);
        newLocation.setLongitude(newLong);
        newLocation.setType(newLocType);

        assertEquals(newName, newLocation.getName());
        assertEquals(newLat, newLocation.getLatitude());
        assertEquals(newLong, newLocation.getLongitude());
        assertEquals(newLocType, newLocation.getType());
    }

    /**
     * Test that the setName setter throws an IllegalArgumentException
     * when its argument is null
     */
    @Test
    public void testLocationSetNameException() {
        thrownException.expect(IllegalArgumentException.class);
        location.setName(null);
    }

    @Test
    public void testSetTypeException() {
        thrownException.expect(IllegalArgumentException.class);
        location.setType(null);
    }

    @Test
    public void constructWithNullName() {
        thrownException.expect(IllegalArgumentException.class);
        new Location(null, Location.LocationType.EVERYWHERE.toString(), 0, 0);
    }

    @Test
    public void constructWithNullType() {
        thrownException.expect(IllegalArgumentException.class);
        new Location("A name", null, 0, 0);
    }

    @Test
    public void constructWithInvalidLatitude() {
        thrownException.expect(IllegalArgumentException.class);
        new Location(null, Location.LocationType.EVERYWHERE.toString(), -200, 0);
    }

    @Test
    public void constructWithInvalidLongitude() {
        thrownException.expect(IllegalArgumentException.class);
        new Location(null, Location.LocationType.EVERYWHERE.toString(), 0, 300);
    }
}
