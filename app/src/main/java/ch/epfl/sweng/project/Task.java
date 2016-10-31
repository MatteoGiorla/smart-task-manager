package ch.epfl.sweng.project;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Task is the class representing a task
 */
public class Task implements Parcelable {

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
    private Long durationInMinutes;
    private Energy energyNeeded;
    private Long timeOfAFractionInMinutes; //to be added optionally later
    private List<String> listOfContributors;
    private DateFormat dateFormat;

    /**
     * Constructor of the class.
     *
     * @param name Task's name
     * @param description Task's description
     * @param location Task's location
     * @param dueDate Task's due date
     * @param durationInMinutes Task's duration in minutes
     * @param energyNeeded Task's energy needed
     * @param listOfContributors Task's list of contributors
     * @throws IllegalArgumentException if one parameter is invalid (null)
     */
   public Task(String name, String description, Location location, Date dueDate,
                long durationInMinutes, String energyNeeded, List<String> listOfContributors) {

       //Control inputs
       if(location == null)
           throw new IllegalArgumentException("Location passed to the constructor is null");
       if(energyNeeded == null)
           throw new IllegalArgumentException("Energy passed to the constructor is null");
       if(name == null)
           throw new IllegalArgumentException("Name passed to the constructor is null");
       if(description == null)
           throw new IllegalArgumentException("Description passed to the constructor is null");
       if(listOfContributors == null || listOfContributors.size() == 0)
           throw new IllegalArgumentException("List of contributors passed to the constructor is invalid");

       this.name = name;
       this.description = description;
       this.durationInMinutes = durationInMinutes;
       this.listOfContributors = new ArrayList<>(listOfContributors);
       this.dueDate = dueDate;
       this.energyNeeded = Energy.valueOf(energyNeeded);
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
                new Date(0),
                30,
                Energy.NORMAL.toString(),
                Collections.singletonList(User.DEFAULT_EMAIL));
    }

    /**
     * Private constructor used to recreate a Task when
     * it was put inside an Intent.
     *
     * @param in Container of a Task
     */
    private Task(Parcel in) {
        if(in == null)
            throw new IllegalArgumentException("In is null");

        setName(in.readString());
        setDescription(in.readString());
        setLocation(new Location());
        setDueDate(new Date(0));
        setDurationInMinutes(30);
        setEnergyNeeded(Energy.NORMAL);
        listOfContributors = new ArrayList<>();
        // Temporary
        String mail;
        try{
            mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }catch (NullPointerException e) {
            mail = User.DEFAULT_EMAIL;
        }
        addContributor(mail);
        dateFormat = DateFormat.getDateInstance();
    }

    /**
     * Getter returning a copy of the task's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter to modify the task's name
     *
     * @param newName The new task's name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if (newName == null)
            throw new IllegalArgumentException("newName passed to the Task's setter is null");
        name = newName;
    }

    /**
     * Getter returning a copy of the task's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter to modify the task's description
     *
     * @param newDescription The new task's description
     * @throws IllegalArgumentException if newName is null
     */
    public void setDescription(String newDescription) {
        if (newDescription == null)
            throw new IllegalArgumentException("newDescription passed to the Task's setter is null");
        description = newDescription;
    }

    /**
     * Getter returning a copy of the task's location
     */
    public Location getLocation() {
        return new Location(location);
    }

    /**
     * Setter to modify the task's location
     *
     * @param newLocation The new task's location
     * @throws IllegalArgumentException if the argument is null
     */
    public void setLocation(Location newLocation) {
        if(newLocation == null)
            throw new IllegalArgumentException("newLocation passed to the Task's setter is null");
        location = newLocation;
    }

    /**
     * Getter returning a copy of the task's due date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Setter to modify the task's due date
     *
     * @param newDueDate The new task's due date
     * @throws IllegalArgumentException if the argument is null
     */
    public void setDueDate(Date newDueDate) {
        if(newDueDate == null)
            throw new IllegalArgumentException("newDueDate passed to the Task's setter is null");
        dueDate = newDueDate;
    }

    /**
     * Transform the date as a string
     * @return The formatted date
     */
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
     * Setter to modify the task's duration
     *
     * @param newDurationInMinutes The new task's duration
     */
    public void setDurationInMinutes(long newDurationInMinutes) {
        durationInMinutes = newDurationInMinutes;
    }

    /**
     * Setter to modify the task's energy need
     *
     * @param newEnergyNeeded The new task's energy need
     * @throws IllegalArgumentException if the argument is null
     */
    public void setEnergyNeeded(Energy newEnergyNeeded) {
        if (newEnergyNeeded == null)
            throw new IllegalArgumentException("newEnergyNeeded passed to the Task's setter is null");
        energyNeeded = newEnergyNeeded;
    }

    /**
     * Getter returning the list of contributors of the task
     *
     * @return list of contributors
     */
    public List<String> getListOfContributors() {
        return new ArrayList<>(listOfContributors);
    }

    /**
     * Getter returning the list of contributors of the task
     * as a string.
     *
     * @return list of contributors formatted as a string.
     */
    public String listOfContributorsToString() {
        if(listOfContributors.isEmpty())
            return "";

       StringBuilder contributorsToString = new StringBuilder();
        for(String contributor: listOfContributors) {
            contributorsToString.append(contributor).append(", ");
        }
        contributorsToString.delete(contributorsToString.length()-2, contributorsToString.length()); //remove the last ", "
        return contributorsToString.toString();
    }

    /**
     * Add a given contributor to the list of contributors
     *
     * @param contributor Email of the contributor
     */
    public void addContributor(String contributor) {
        if(contributor == null)
            listOfContributors.add(User.DEFAULT_EMAIL);
        else
        listOfContributors.add(contributor);
    }

    /**
     * Delete a given contributor of the list of contributors
     *
     * @param contributor Email of the contributor
     * @throws IllegalArgumentException if the argument is null
     */
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

    public enum Energy {LOW, NORMAL, HIGH}
}
