package ch.epfl.sweng.project;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Task is the class representing a task
 */
public class Task implements Parcelable {

    public enum Energy { LOW, NORMAL, HIGH }

    /**
     * Used to regenerate a Task, all parcelables must have a creator
     */
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        /**
         * Create a new instance of the Task with the data previously
         * written by writeToParcel().
         *
         * @param in The Parcel to read the object's data from
         * @return New instance of Task
         */
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        /**
         * Create a new array of Task
         * @param size Size of the array
         * @return An array of Task
         */
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    private String name;
    private String description;
    private Location location;
    private GregorianCalendar dueDate;
    private long durationInMinutes;
    private Energy energyNeeded;
    private long timeOfAFractionInMinutes; //to be added optionally later
    private String author;

    /**
     * Constructor of the class
     *
     * @param name Task's name
     * @throws IllegalArgumentException if the parameter is null
     */
    public Task(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        } else {
            this.name = name;
            this.description = "";
            this.location = null;
            this.dueDate = null;
            this.durationInMinutes = 0;
            this.energyNeeded = null;
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                this.author = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            } else {
                this.author = "";
            }
        }
    }

    /**
     * Private constructor used to recreate a Task when
     * it was put inside an Intent.
     *
     * @param in Container of a Task
     * @throws IllegalArgumentException if the parameter is null
     */
    private Task(Parcel in) {
        if (in == null) {
            throw new IllegalArgumentException();
        }
        this.name = in.readString();
        this.description = in.readString();
    }

    /**
     * Getter returning a copy of the task's name
     */
    public String getName() {
        return name;
    }


    /**
     * Getter returning a copy of the task's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter returning a copy of the task's location
     */
    public Location getLocation() {
        if (location == null) {
            return null;
        } else {
            return new Location(location.getName(), location.getType(), location.getGPSCoordinates());
        }
    }

    /**
     * Getter returning a copy of the task's due date
     */
    public GregorianCalendar getDueDate() {
        if (dueDate == null) {
            return null;
        } else {
            return new GregorianCalendar(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Getter returning the task's duration
     */
    public long getDuration() {
        return durationInMinutes;
    }

    /**
     * Getter returning the task's energy need
     */
    public Energy getEnergy() {
        return energyNeeded;
    }

    /**
     * Getter returning the task's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter to modify the task's name
     *
     * @param newName The new task's name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException();
        } else {
            name = newName;
        }
    }

    /**
     * Setter to modify the task's description
     *
     * @param newDescription The new task's description
     * @throws IllegalArgumentException if newName is null
     */
    public void setDescription(String newDescription) {
        if (newDescription == null) {
            throw new IllegalArgumentException();
        } else {
            description = newDescription;
        }
    }

    /**
     * Setter to modify the task's location
     *
     * @param newLocation The new task's location
     */
    public void setLocation(Location newLocation) {
        location = newLocation;
    }

    /**
     * Setter to modify the task's due date
     *
     * @param newDueDate The new task's due date
     */
    public void setDueDate(GregorianCalendar newDueDate) {
        dueDate = newDueDate;
    }

    /**
     * Setter to modify the task's duration
     *
     * @param newDurationInMinutes The new task's duration
     */
    public void setDurationInMinutes(long newDurationInMinutes) {
        durationInMinutes= newDurationInMinutes;
    }

    /**
     * Setter to modify the task's energy need
     *
     * @param newEnergyNeeded The new task's energy need
     */
    public void setEnergyNeeded(Energy newEnergyNeeded) {
        energyNeeded= newEnergyNeeded;
    }

    /**
     * Override the describeContents method from
     * Parcelable interface.
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten the Task in to a Parcel
     *
     * @param dest  The parcel in which the Task should be written
     * @param flags Flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }
}
