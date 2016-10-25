package ch.epfl.sweng.project;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private Date dueDate;
    private long dueDateAttribute;
    private long durationInMinutes;
    private Energy energyNeeded;
    private long timeOfAFractionInMinutes; //to be added optionally later
    private List<String> listOfContributors;
    private DateFormat dateFormat;


    /**
     * Constructor of the class.
     *
     * @param name Task's name
     * @param description Task's description
     * @param location Task's location
     * @param dueDateAttribute Task's due date attribute
     * @param durationInMinutes Task's duration in minutes
     * @param energyNeeded Task's energy needed
     * @param listOfContributors Task's list of contributors
     * @throws IllegalArgumentException if one parameter is invalid (null)
     */
   public Task(String name, String description, Location location, long dueDateAttribute,
                long durationInMinutes, Energy energyNeeded, List<String> listOfContributors) {

       if(location == null) {
           throw new IllegalArgumentException("Location passed to the constructor is null");
       }
       if(energyNeeded == null) {
           throw new IllegalArgumentException("Energy passed to the constructor is null");
       }
       if(name == null) {
           throw new IllegalArgumentException("Name passed to the constructor is null");
       }
       if(description == null) {
           throw new IllegalArgumentException("Description passed to the constructor is null");
       }
       if(listOfContributors == null || listOfContributors.size() == 0) {
           throw new IllegalArgumentException("List of contributors passed to the constructor is invalid");
       }
       this.name = name;
       this.description = description;
       this.durationInMinutes = durationInMinutes;
       this.listOfContributors = new ArrayList<>(listOfContributors);
       this.dueDateAttribute = dueDateAttribute;
       dueDate = new Date(this.dueDateAttribute);
       this.energyNeeded = energyNeeded;
       this.location = new Location(location);
       dateFormat = DateFormat.getDateInstance();
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
        this(name,
                description,
                new Location(),
                0,
                30,
                Energy.NORMAL,
                Arrays.asList("Me", "myself", "I"));
    }

    /**
     * Private constructor used to recreate a Task when
     * it was put inside an Intent.
     *
     * @param in Container of a Task
     */
    private Task(Parcel in) {
        if(in == null) {
            throw new IllegalArgumentException("In is null");
        }
        setName(in.readString());
        setDescription(in.readString());
        setLocation(new Location());
        setDueDate(new Date(0));
        setDurationInMinutes(30);
        setEnergyNeeded(Energy.NORMAL);
        listOfContributors = new ArrayList<>();
        addContributor("Me");
        addContributor("myself");
        addContributor("I");
        dateFormat = DateFormat.getDateInstance();
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
    public Date getDueDate() {
        return dueDate;
    }

    public String dueDateToString() {
        return dateFormat.format(dueDate.getTime());
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
     * Getter returning the task's list of contributors
     */
    public List<String> getAuthor() {
        return new ArrayList<>(listOfContributors);
    }

    /**
     * Setter to modify the task's name
     *
     * @param newName The new task's name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException("newName passed to the Task's setter is null");
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
            throw new IllegalArgumentException("newDescription passed to the Task's setter is null");
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
            throw new IllegalArgumentException("newLocation passed to the Task's setter is null");
        }
        location = newLocation;
    }

    /**
     * Setter to modify the task's due date
     *
     * @param newDueDate The new task's due date
     */
    public void setDueDate(Date newDueDate) {
        if(newDueDate == null) {
            throw new IllegalArgumentException("newDueDate passed to the Task's setter is null");
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
            throw new IllegalArgumentException("newEnergyNeeded passed to the Task's setter is null");
        }
        energyNeeded= newEnergyNeeded;
    }

    public List<String> getListOfContributors() {
        return new ArrayList<>(listOfContributors);
    }

    public String listOfContributorToString() {
        if(listOfContributors.isEmpty())
            return "";

       StringBuilder contributorsToString = new StringBuilder();
        for(String contributor: listOfContributors) {
            contributorsToString.append(contributor).append(", ");
        }
        contributorsToString.delete(contributorsToString.length()-2, contributorsToString.length()); //remove the last ", "
        return contributorsToString.toString();
    }

    public boolean addContributor(String contributor) {
        if(contributor == null)
            throw new IllegalArgumentException("Contributor to be added null");
        return listOfContributors.add(contributor);
    }

    public boolean deleteContributor(String contributor) {
        if(contributor == null || !listOfContributors.contains(contributor))
            throw new IllegalArgumentException("Contributor to be deleted invalid");
        return listOfContributors.remove(contributor);
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
