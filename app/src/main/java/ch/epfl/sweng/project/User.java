package ch.epfl.sweng.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a user
 */
public class User {
    private String email;
    private List listTasks;
    private List<Location> listLocations;
    private static final String TAG = "User Class";

    /**
     * Constructor of the class with only email. The list of task's id and location
     * are initialized with empty arrays.
     *
     * @param mail The email of the user
     */
    public User(String mail) {
        if (mail != null) {
            this.email = mail;
        } else {
            throw new NullPointerException("User email is invalid !");
        }
        this.listTasks = new ArrayList<String>();
        this.listLocations = new ArrayList<Location>();
    }
    /**
     * Constructor of the class.
     *
     * @throws NullPointerException if the parameters are invalid
     */
    public User(String mail, List<String> listTasks, List<Location> locations) {
        if (mail == null || listTasks == null || locations == null) {
            throw new NullPointerException("Invalid parameters when creating a new user");
        } else {
            this.email = mail;
            this.listTasks = new ArrayList<String>(listTasks);
            this.listLocations = new ArrayList<Location>(locations);
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
     *
     * @return the list of Tasks of the user.
     */
    public List<String> getListTasks() {
        return this.listTasks;
    }

    /**
     * Add the id of a task to the list of tasks' id of the user.
     *
     * @param idTask id of the task to add.
     */
    public void addListTasks(String idTask) {
        this.listTasks.add(idTask);
    }

    /**
     * Remove an id of a task from the list of tasks' id of the user.
     *
     * @param idTask the id of a task to remove.
     * @return true if the id of the task was inside the list and was well removed, otherwise false.
     */
    public boolean removeListTasks(long idTask) {
        return this.listTasks.remove(idTask);
    }


    /**
     * Add a new location to the list of the locations of the user.
     *
     * @param newLocation new location to add.
     * @throws IllegalArgumentException if the type of newLocation is EVERYWHERE.
     */
    public void addListLocation(Location newLocation) {
        // we can't add a location of type EVERYWHERE:
        if (newLocation.getType() == Location.LocationType.EVERYWHERE) {
            throw new IllegalArgumentException();
        } else {
            this.listLocations.add(newLocation);
        }
    }

    /**
     * Remove a location from the list of the locations of the user.
     *
     * @param toRemoveLocation location to remove.
     * @throws IllegalArgumentException if the type of newLocation is EVERYWHERE.
     * @return true if the location was inside the list and was well removed, otherwise false.
     */
    public boolean removeListLocation(Location toRemoveLocation) {
        // we can't remove a location of type EVERYWHERE:
        if (toRemoveLocation.getType() == Location.LocationType.EVERYWHERE) {
            throw new IllegalArgumentException();
        } else {
            return this.listLocations.remove(toRemoveLocation);
        }
    }
}