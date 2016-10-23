package ch.epfl.sweng.project;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

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
    private LatLng gpsCoordinates;
    private Location.LocationType type;

    @Rule
    public final ExpectedException thrownException = ExpectedException.none();

    @Before
    public void initValidLocation() {
        name = "name";
        type = Location.LocationType.HOME;
        gpsCoordinates = new LatLng(41, 38);
        location = new Location(name, type, gpsCoordinates);
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
        assertEquals(gpsCoordinates, location.getGPSCoordinates());
    }

    /**
     * Test that the setters modify correctly the location
     */
    @Test
    public void testLocationSetters() {
        String newName = "EPFL";
        LatLng newGPSCoordinates = new LatLng(20, 45);

        location.setName(newName);
        location.setGpsCoordinates(newGPSCoordinates);

        assertEquals(newName, location.getName());
        assertEquals(newGPSCoordinates, location.getGPSCoordinates());
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

    /**
     * Test that the public constructor throws an IllegalArgumentException
     * when its arguments are null
     */
    @Test
    public void testConstructorException() {
        thrownException.expect(IllegalArgumentException.class);
        new Location(null, null, null);
    }
}
