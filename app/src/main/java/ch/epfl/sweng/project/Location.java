package ch.epfl.sweng.project;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class representing a location
 */
public class Location {

    public enum LocationType { HOME, OFFICE, EVERYWHERE };

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
        if (type == LocationType.EVERYWHERE) {
            this.name = "Everywhere";
            this.type = LocationType.EVERYWHERE;
            this.gpsCoordinates = null;

        } else {
            this.name = name;
            this.type = type;
            // TODO : Test validity of values
            this.gpsCoordinates = gpsCoordinates;

        }
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
     * Setter to modify the location name
     *
     * @param newName The new location name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException();
        } else {
            this.name = newName;
        }
    }

    /**
     * Setter to modify the location GPS coordinates
     *
     * @param gpsCoordinates The new GPS coordinates of the location
     * @throws IllegalArgumentException if the GPS coordinates value is not valid
     */
    public void setGpsCoordinates(LatLng gpsCoordinates) {
        if (gpsCoordinates == null || false) { //TODO : test validity of argument
            throw new IllegalArgumentException();
        } else {
            this.gpsCoordinates = gpsCoordinates;
        }
    }
}
