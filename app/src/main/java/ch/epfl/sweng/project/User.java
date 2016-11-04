package ch.epfl.sweng.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a user
 */
public class User {
    public static final String DEFAULT_EMAIL = "trixyfinger@gmail.com";
    private String email;
    private List<Location> listLocations;

    /**
     * Constructor of the user class which
     * set listLocations to an empty list.
     * @param mail user email
     */
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
            if(mail == null) {
                this.email = DEFAULT_EMAIL;
            }
            else {
                this.email = mail;
            }
            this.listLocations = new ArrayList<>(listLocations);
        }
    }

    /**
     * Getter returning the email of the user
     *
     * @return email of the user.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter
     *
     * @return list of Locations of the user.
     */
    public List<Location> getListLocations() {
        return listLocations;
    }

    /**
     * Setter that allows to change the locations of the user
     *
     * @throws NullPointerException If the argument is null
     */
    public void setListLocations(List<Location> list) {
        if(list == null) {
            throw new IllegalArgumentException("Bad list of locationName given in the setter of user");
        }else{
            if(list.size() == 2) {
                this.listLocations = new ArrayList<>(list);
            }
        }
    }
    /**
     * Allow the user to update a specific locationName.
     * @param location The new Location
     */
    public void updateLocation(Location location) {
        if(location == null) {
            throw new IllegalArgumentException("Bad locationName update !");
        }

        for(int i=0; i < listLocations.size(); i++) {
            if(listLocations.get(i).getName().equals(location.getName())) {
                listLocations.set(i,location);
            }
        }
    }

    /**
     * Allow the user to add a new location
     * @param location The new Location
     */
    public void addLocation(Location location) {
        if(location == null) {
            throw new IllegalArgumentException("Bad locationName update !");
        }
        listLocations.add(location);
    }
}