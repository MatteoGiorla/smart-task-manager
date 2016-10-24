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
        if (name == null || type == null) {
            throw new IllegalArgumentException("Name or type of new location can't be null");
        }
        if(gpsCoordinates == null) {
            throw new IllegalArgumentException("gpsCoordinates passed to the constructor is are null");
        }
        this.name = name;
        this.type = type;
        //GPS coordinates are optional and can therefore be null
        this.gpsCoordinates = gpsCoordinates;
    }

    public Location(Location location) {
        new Location(location.getName(), location.getType(), location.getGPSCoordinates());
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
        if (this.gpsCoordinates == null){
            return null;
        } else {
            return new LatLng(this.gpsCoordinates.latitude, this.gpsCoordinates.longitude);
        }
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
        if (this.type == LocationType.EVERYWHERE) {
            this.name = "Everywhere";
        } else if (newName == null) {
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
        if (this.type == LocationType.EVERYWHERE) {
            this.gpsCoordinates = null;
        } else {
            //GPS coordinates are optional and can therefore be null
            this.gpsCoordinates = gpsCoordinates;
        }
    }
}