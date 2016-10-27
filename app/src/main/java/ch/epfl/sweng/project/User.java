package ch.epfl.sweng.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a user
 */
public class User {
    private String email;
    private List<Location> listLocations;
    private static final String TAG = "User Class";
    public static final String DEFAULT_EMAIL = "trixyfinger@gmail.com";

    public User(String mail) {
        if (mail == null) {
            this.email = DEFAULT_EMAIL;
        } else {
            this.email = mail;
        }
        // Default values:
        this.listLocations = Arrays.asList(new Location(), new Location());
    }
    /**
     * Constructor of the class. Implementation of the fields of the class with
     * default values.
     *
     * @throws NullPointerException if an argument is null
     */
    public User(String mail, List<Location> listLocations) {
        if (listLocations == null) {
            throw new NullPointerException();
        } else {
            if(email == null)
                this.email = DEFAULT_EMAIL;
            else
                this.email = mail;
            this.listLocations = new ArrayList<>(listLocations);
        }
    }

    /**
     * Getter
     *
     * @return email of the user.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter
     */
    public List<Location> getListLocations() {
        return listLocations;
    }

    /**
     * Setter
     */
    public void setListLocations(List<Location> list) {
        if(list == null) {
            throw new IllegalArgumentException("Bad list of location given in the setter of user");
        }else{
            if(list.size() == 2)
                this.listLocations = new ArrayList<>(list);
        }
    }
    /**
     * ListLocations is always of size 2.
     */
    public void updateLocation(Location location) {
        if(location == null || location.getType() == Location.LocationType.EVERYWHERE
                && (location.getType() != Location.LocationType.HOME || location.getType() != Location.LocationType.WORKPLACE)) {
            throw new IllegalArgumentException("Bad location update !");
        }
        if(location.getType() == Location.LocationType.HOME) {
            if(listLocations.get(0).getType() == Location.LocationType.HOME) {
                listLocations.set(0, location);
            }else{
                listLocations.set(1, location);
            }
        }else{
            if(listLocations.get(0).getType() == Location.LocationType.WORKPLACE) {
                listLocations.set(0, location);
            }else{
                listLocations.set(1, location);
            }
        }
    }
}