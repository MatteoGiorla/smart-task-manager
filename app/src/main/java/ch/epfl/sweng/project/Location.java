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
    //TODO later : add icon corresponding to the location

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
            if (name == null || type == null) {
                throw new IllegalArgumentException("Name or type of new location can't be null");
            } else {
                this.name = name;
                this.type = type;
            }
            //GPS coordinates are optional and can therefore be null
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
            throw new IllegalArgumentException();
        } else {
            this.name = newName;
        }
    }

    /**
     * Setter to modify the location GPS coordinates
     *
     * @param gpsCoordinates The new GPS coordinates of the location
     */
    public void setGpsCoordinates(LatLng gpsCoordinates) {
        //GPS coordinates are optional and can therefore be null
        this.gpsCoordinates = gpsCoordinates;
    }
}