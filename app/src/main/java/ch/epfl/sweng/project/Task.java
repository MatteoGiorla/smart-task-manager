package ch.epfl.sweng.project;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
     * Constructor of the class.
     *
     * @param name Task's name
     * @param description Task's description
     * @param location Task's location
     * @param dueDate Task's due date
     * @param durationInMinutes Task's duration in minutes
     * @param energyNeeded Task's energy needed
     * @param author Task's author
     * @throws IllegalArgumentException if one parameter is invalid (null)
     */
   public Task(String name, String description, Location location, GregorianCalendar dueDate,
                long durationInMinutes, Energy energyNeeded, String author) {

       if(location == null) {
           throw new IllegalArgumentException("Location passed to the constructor is null");
       }
       if(dueDate == null) {
           throw new IllegalArgumentException("Due date passed to the constructor is null");
       }
       if(energyNeeded == null) {
           throw new IllegalArgumentException("Energy passed to the constructor is null");
       }
       this.name = name;
       this.description = description;
       this.durationInMinutes = 0;
       this.author = author;
   }

    /**
     * Constructor of the class.
     * Take only name and description as parameters, and initialise the other attributes
     * with default values.
     *
     * @param name Task's name
     * @param description Task's description
     */
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        LatLng coordinates = new LatLng(0, 0);
        location = new Location("Every where", Location.LocationType.EVERYWHERE, coordinates);
        dueDate = new GregorianCalendar(TimeZone.getDefault(), Locale.FRANCE);
        author = "Me";
        energyNeeded = Energy.NORMAL;
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
        return new Location(location);
    }

    /**
     * Getter returning a copy of the task's due date
     */
    public GregorianCalendar getDueDate() {
        return dueDate;
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
        if(newLocation == null) {
            throw new IllegalArgumentException("newLocation passed to the setter is null");
        }
        location = newLocation;
    }

    /**
     * Setter to modify the task's due date
     *
     * @param newDueDate The new task's due date
     */
    public void setDueDate(GregorianCalendar newDueDate) {
        if(newDueDate == null) {
            throw new IllegalArgumentException("newDueDate passed to the setter is null");
        }
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
        if(newEnergyNeeded == null) {
            throw new IllegalArgumentException("newEnergyNeeded passed to the setter is null");
        }
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
