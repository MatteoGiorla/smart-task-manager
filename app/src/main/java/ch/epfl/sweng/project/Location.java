package ch.epfl.sweng.project;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class representing a location
 */
public class Location {

    public enum LocationType { HOME, WORKPLACE, EVERYWHERE }

    private String name;
    private LocationType type;
    private double latitude;
    private double longitude;

    /**
     * Constructor of the class
     *
     * @param name Location name
     * @param typeString Location type in String format
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @throws IllegalArgumentException if the parameter is null
     */
    public Location(String name, String typeString,  double latitude, double longitude) {
        if(name == null)
            throw new IllegalArgumentException("Name passed to the Location's constructor is null");

        if(typeString == null)
            throw new IllegalArgumentException("typeString passed to the Location's constructor is null");

        if(latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude is out of range");

        if(longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude is out of range");

        this.name = name;
        this.type = LocationType.valueOf(typeString);
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Location(Location location) {
        this(location.getName(), location.getType().toString(), location.getLatitude(), location.getLongitude());
    }

    public Location() {
        this("Everywhere", LocationType.EVERYWHERE.toString(), 0, 0);
    }

    /**
     * Getter returning the name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * Getter returning the longitude of the location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter returning the latitude of the location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter returning the gps coordinates of the location
     */
    public LatLng getGPSCoordinates() {
        return new LatLng(latitude, longitude);
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

   public void setLatitude(double newLatitude) {
       if(newLatitude < -90 || newLatitude > 90)
           throw new IllegalArgumentException("New latitude passed to Location's setter invalid");
       latitude = newLatitude;
   }

    public void setLongitude(double newLongitude) {
        if(newLongitude < -180 || newLongitude > 180)
            throw new IllegalArgumentException("New longitude passed to Location's setter invalid");
        longitude = newLongitude;
    }

    public void setType(LocationType newType) {
        if(newType == null)
            throw new IllegalArgumentException("Location type passed to the setter invalid");
        type = newType;
    }
}