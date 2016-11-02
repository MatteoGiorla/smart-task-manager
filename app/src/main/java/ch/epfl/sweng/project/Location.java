package ch.epfl.sweng.project;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class representing a locationName
 */
public class Location {

    private String name;
    private LocationType type;
    private double latitude;
    private double longitude;
    /**
     * Constructor of the class
     *
     * @param name       Location name
     * @param typeString Location type in String format
     * @param latitude   Latitude of the locationName
     * @param longitude  Longitude of the locationName
     * @throws IllegalArgumentException if the parameter is null
     */
    public Location(String name, String typeString, double latitude, double longitude) {
        if (name == null)
            throw new IllegalArgumentException("Name passed to the Location's constructor is null");

        if (typeString == null)
            throw new IllegalArgumentException("typeString passed to the Location's constructor is null");

        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude is out of range");

        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude is out of range");

        this.name = name;
        this.type = LocationType.valueOf(typeString);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Default constructor initializing fields to default values
     */
    public Location() {
        this("Everywhere", LocationType.EVERYWHERE.toString(), 0, 0);
    }

    /**
     * Getter returning the name of the locationName
     */
    public String getName() {
        return name;
    }

    /**
     * Setter to modify the locationName name
     *
     * @param newName The new locationName name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException("New name passed to the Location's setter is null");
        }
        this.name = newName;
    }

    /**
     * Getter returning the longitude of the locationName
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter to modify the longitude
     *
     * @param newLongitude The new longitude
     * @throws IllegalArgumentException if the argument is not between -180 and 180
     */
    public void setLongitude(double newLongitude) {
        if (newLongitude < -180 || newLongitude > 180)
            throw new IllegalArgumentException("New longitude passed to Location's setter invalid");
        longitude = newLongitude;
    }

    /**
     * Getter returning the latitude of the location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter to modify the latitude
     *
     * @param newLatitude The new latitude
     * @throws IllegalArgumentException if the argument is not between -90 and 90
     */
    public void setLatitude(double newLatitude) {
        if (newLatitude < -90 || newLatitude > 90)
            throw new IllegalArgumentException("New latitude passed to Location's setter invalid");
        latitude = newLatitude;
    }

    /**
     * Getter returning the gps coordinates of the location
     */
    public LatLng getGPSCoordinates() {
        return new LatLng(latitude, longitude);
    }

    /**
     * Setter to modify the type of the locationName
     *
     * @param newType The new type of the locationName
     * @throws IllegalArgumentException if the argument is null
     */
    public void setType(LocationType newType) {
        if (newType == null)
            throw new IllegalArgumentException("Location type passed to the setter invalid");
        type = newType;
    }

    /**
     * Getter returning the type of the locationName
     */
    public LocationType getType() {
        return type;
    }

    public enum LocationType {HOME, WORKPLACE, EVERYWHERE}

}