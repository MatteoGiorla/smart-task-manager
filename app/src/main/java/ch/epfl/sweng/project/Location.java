package ch.epfl.sweng.project;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class representing a location
 */
public class Location {

    public enum LocationType { HOME, WORKPLACE, EVERYWHERE }

    private String name;
    private LocationType type;
    private LatLng gpsCoordinates;

    /**
     * Constructor of the class
     *
     * @param name Location name
     * @param type Location type
     * @param gpsCoordinates the gps coordinates of the location
     * @throws IllegalArgumentException if the parameter is null
     */
    public Location(String name, LocationType type, LatLng gpsCoordinates) {
        if(name == null) {
            throw new IllegalArgumentException("Name passed to the Location's constructor is null");
        }
        if(type == null) {
            throw new IllegalArgumentException("Type passed to the Location's constructor is null");
        }
        if(gpsCoordinates == null) {
            throw new IllegalArgumentException("gpsCoordinates passed to the Location's constructor is are null");
        }
        this.name = name;
        this.type = type;
        this.gpsCoordinates = gpsCoordinates;
    }

    public Location(Location location) {
        this(location.getName(), location.getType(), location.getGPSCoordinates());
    }

    public Location() {
        this("Every where", LocationType.EVERYWHERE, new LatLng(0, 0));
    }

    /**
     * Getter returning the name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * Getter returning the gps coordinates of the location
     */
    public LatLng getGPSCoordinates() {
        return gpsCoordinates;
    }

    /**
     * Getter returning the type of the location
     */
    public LocationType getType() {
        return type;
    }

    /**
     * Setter to modify the location name
     *
     * @param newName The new location name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException("New name passed to the Location's setter is null");
        }
        this.name = newName;
    }

    /**
     * Setter to modify the location GPS coordinates
     *
     * @param newGpsCoordinates The new GPS coordinates of the location
     */
    public void setGpsCoordinates(LatLng newGpsCoordinates) {
        if (newGpsCoordinates == null) {
            throw new IllegalArgumentException("new gps coordinates passed to the Location's setter are null");
        }
        gpsCoordinates = newGpsCoordinates;
    }
}