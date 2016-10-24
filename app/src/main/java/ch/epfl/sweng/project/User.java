package ch.epfl.sweng.project;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a user
 */
public class User {
    private String email;
    private List listTasks;
    private Location currentLocation;
    private Task.Energy currentEnergy;
    private long currentTimeAtDisposal;
    private List<Location> listLocation;
    private static final String TAG = "User Class";

    /**
     * Constructor of the class. Implementation of the fields of the class with
     * default values.
     *
     * @throws FirebaseAuthInvalidUserException if the FirebaseUser user doesn't exist.
     */
    public User() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.email = user.getEmail();
        } else {
            Log.d(TAG, "Error the user doesn't exist.");
            throw new NullPointerException();
        }
        // Default values:
        this.listTasks = new ArrayList<Long>();
        this.currentLocation = new Location("", Location.LocationType.EVERYWHERE, null);
        this.currentEnergy = Task.Energy.NORMAL;
        this.currentTimeAtDisposal = 30;
        Location home = new Location("Home", Location.LocationType.HOME, null);
        Location office = new Location("Office", Location.LocationType.WORKPLACE, null);
        Location school = new Location("School", Location.LocationType.WORKPLACE, null);
        Location everywhere = new Location("", Location.LocationType.EVERYWHERE, null);
        this.listLocation = new ArrayList<Location>();
        this.listLocation.add(home);
        this.listLocation.add(office);
        this.listLocation.add(school);
        this.listLocation.add(everywhere);
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
    public List<Long> getListTasks() {
        return this.listTasks;
    }

    /**
     * Add a Task to the list of Tasks of the user.
     *
     * @param index
     * @param task
     */
    public void addListTasks(int index, Task task) {
        this.listTasks.add(index, task);
    }

    /**
     * Remove a task from the list of tasks of the user.
     *
     * @param task the task to add.
     * @return true if the task was inside the list and was well removed, otherwise false.
     */
    public boolean removeListTasks(Task task) {
            return this.listTasks.remove(task);
    }

    /**
     * Getter
     *
     * @return the current location of the user.
     */
    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    /**
     * Setter
     *
     * @param newLocation the new current location of the user.
     */
    public void setCurrentLocation(Location newLocation) {
        this.currentLocation = newLocation;
    }

    /**
     * Getter
     *
     * @return the current energy of the user.
     */
    public Task.Energy getCurrentEnergy() {
        return this.currentEnergy;
    }

    /**
     * Setter
     *
     * @param newEnergy the new current energy of the user.
     */
    public void setCurrentEnergy(Task.Energy newEnergy) {
        this.currentEnergy = newEnergy;
    }

    /**
     * Getter
     *
     * @return the current time at disposal of the user.
     */
    public long getCurrentTimeAtDisposal() {
        return this.currentTimeAtDisposal;
    }

    /**
     * Setter
     *
     * @param newTimeAtDisposal the new current time at disposal of the user.
     */
    public void setCurrentTimeAtDisposal(long newTimeAtDisposal) {
        this.currentTimeAtDisposal = newTimeAtDisposal;
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
            this.listLocation.add(newLocation);
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
            return this.listLocation.remove(toRemoveLocation);
        }
    }
}