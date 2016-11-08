package ch.epfl.sweng.project;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Task is the class representing a task
 */
public class Task implements Parcelable, Comparable<Task> {

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
    private String locationName;
    private Date dueDate;
    private Long durationInMinutes;
    private Energy energyNeeded;
    private final List<String> listOfContributors;
    private final DateFormat dateFormat;
    private final int fraction;
    private final int staticSortValue;

    /**
     * Constructor of the class
     *
     * @param name               Task's name
     * @param description        Task's description
     * @param locationName       Task's locationName
     * @param dueDate            Task's due date
     * @param durationInMinutes           Task's durationInMinutes in minutes
     * @param energyNeeded       Task's energy needed
     * @param listOfContributors Task's list of contributors
     * @throws IllegalArgumentException if one parameter is invalid (null)
     */
    public Task(@NonNull String name, @NonNull String description, @NonNull String locationName, @NonNull Date dueDate,
                long durationInMinutes, String energyNeeded, @NonNull List<String> listOfContributors) {
        this.name = name;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.listOfContributors = new ArrayList<>(listOfContributors);
        this.dueDate = dueDate;
        this.energyNeeded = Energy.valueOf(energyNeeded);
        this.locationName = locationName;
        dateFormat = DateFormat.getDateInstance();
        fraction = 1;
        staticSortValue = computeStaticSortValue();
    }

    /**
     * Private constructor used to recreate a Task when
     * it was put inside an Intent.
     *
     * @param in Container of a Task
     */
    private Task(@NonNull Parcel in) {
        this(in.readString(), in.readString(), in.readString(),
                new Date(in.readLong()), in.readLong(), in.readString(),
                in.createStringArrayList());
    }

    public int getStaticSortValue() {
        return staticSortValue;
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
     * Getter returning a copy of the task's locationName name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Getter returning the task's durationInMinutes
     */
    public long getDurationInMinutes() {
        return durationInMinutes;
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
     * Setter to modify the task's location
     *
     * @param newLocationName The new task's locationName
     * @throws IllegalArgumentException if the argument is null
     */
    public void setLocationName(String newLocationName) {
        if (newLocationName == null)
            throw new IllegalArgumentException("newLocation passed to the Task's setter is null");
        locationName = newLocationName;
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
        if (newDueDate == null)
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
     * @param newDurationInMinutes The new task's durationInMinutes
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
     * Add a given contributor to the list of contributors
     *
     * @param contributor Email of the contributor
     */
    void addContributor(String contributor) {
        if (contributor == null)
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
        if (contributor == null || !listOfContributors.contains(contributor))
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
        dest.writeString(locationName);
        dest.writeLong(dueDate.getTime());
        dest.writeLong(durationInMinutes);
        dest.writeString(energyNeeded.toString());
        dest.writeStringList(listOfContributors);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(Task o) {
        return Integer.compare(o.getStaticSortValue(), staticSortValue);
    }

    public enum Energy {LOW, NORMAL, HIGH}

    private int computeStaticSortValue() {
        Calendar c = Calendar.getInstance();
        int delay = (int)daysBetween(c.getTime(), dueDate);
        int staticSortValue = (120 * durationInMinutes.intValue() + 55 * (energyNeeded.ordinal() + 1)) / (75 * delay + 100 * fraction);
        return staticSortValue;
    }

    private Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
}
