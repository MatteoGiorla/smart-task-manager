package ch.epfl.sweng.project;

import android.content.res.Resources;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        /**
         * Create a new array of Task
         * @param size Size of the array
         * @return An array of Task
         */
        @Override
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
    private Long startDuration; //in minutes


    /**
     * Enum representing the values of energy needed.
     */
    public enum Energy {LOW, NORMAL, HIGH}


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
                long durationInMinutes, String energyNeeded, @NonNull List<String> listOfContributors, long startDuration) {
        this.name = name;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.listOfContributors = new ArrayList<>(listOfContributors);
        this.dueDate = dueDate;
        this.energyNeeded = Energy.valueOf(energyNeeded);
        this.locationName = locationName;
        dateFormat = DateFormat.getDateInstance();
        this.startDuration = startDuration;
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
                in.createStringArrayList(), in.readLong());
    }

    /**
     * Getter returning start duration
     */
    public long getStartDuration() {
        return startDuration;
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
     * Setter to modify start duration
     *
     * @param newStartDuration the new start duration
     */
    public void setStartDuration(Long newStartDuration) {
        startDuration = newStartDuration;
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
    void setDueDate(Date newDueDate) {
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
    void setDurationInMinutes(long newDurationInMinutes) {
        durationInMinutes = newDurationInMinutes;
    }

    /**
     * Setter to modify the task's energy need
     *
     * @param newEnergyNeeded The new task's energy need
     * @throws IllegalArgumentException if the argument is null
     */
    void setEnergyNeeded(Energy newEnergyNeeded) {
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
        dest.writeLong(startDuration);

    }

    /**
     * Method returning a static comparator on Task.
     *
     * @return Static Comparator
     */
    public static Comparator<Task> getStaticComparator() {
        return new StaticComparator();
    }

    /**
     * Method returning a dynamic comparator on Task.
     *
     * @param currentLocation The user's current location
     * @param currentTimeDisposal The user's current disposal time
     * @return Dynamic Comparator
     */
    static Comparator<Task> getDynamicComparator(String currentLocation,
                                                        int currentTimeDisposal, String everywhere_location, String select_one_location) {
        return new DynamicComparator(currentLocation, currentTimeDisposal, everywhere_location, select_one_location);
    }

    /**
     * Convert the energy to int. It's to compute the number of point of each task
     * used for sorting them.
     *
     * @return integer representing the energy needed
     */
    private int getEnergyToInt() {
        return energyNeeded.ordinal() + 1;
    }

    private double computeStaticSortValue() {
        Calendar c = Calendar.getInstance();
        int delay = Math.max(0, daysBetween(c.getTime(), dueDate));
        if(delay <= 0) {
            delay = 0;
        }
        return this.getDuration()/(2*delay+0.1);
    }

    public double getStaticSortValue(){
        return computeStaticSortValue();
    }

    /**
     * Method returning a calendar for the specified Date.
     *
     * @param date the date
     * @return Calendar
     */
    private Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    /**
     * Method returning the number of day between two days.
     *
      * @param startDate the first day
     * @param endDate the last day
     * @return number of days between startDate and endDate
     */
    public int daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        Long millisDifference = eDate.getTimeInMillis() - sDate.getTimeInMillis();
        Long daysDifference =  TimeUnit.MILLISECONDS.toDays(millisDifference);
        return daysDifference.intValue();
    }

    /**
     * Private static inner class representing the static comparator.
     */
    private static class StaticComparator implements Comparator<Task> {

        /**
         * compare method of the Comparator.
         *
         * @param o1 the first task to compare
         * @param o2 the second task to compare
         * @return 0 if o1 == o2,
         *           a value less than 0 if o1 < o2
         *           a value greater than 0 if o1 > o2
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(Task o1, Task o2) {
            return Double.compare(o2.computeStaticSortValue(), o1.computeStaticSortValue());
        }
    }

    /**
     * Private static inner class representing the dynamic comparator.
     */
    private static class DynamicComparator implements Comparator<Task> {
        private static final int SHORT_DELAY_COEFFICIENT = 100000;
        private static final int TIME_COEFFICIENT = 100000000;
        private static final int LOCATION_COEFFICIENT = 1000000000;
        private static final int TIME_LIMIT = 60;
        private String currentLocation;
        private int currentTimeDisposal;
        private String everywhere_location = "";
        private String select_one_location = "";

        /**
         * Private constructor of the class.
         * @param currentLocation User's current location
         * @param currentTimeDisposal User's current disposal time
         */
        private DynamicComparator(@NonNull String currentLocation, int currentTimeDisposal, String everywhere_location, String select_one_location) {
            this.currentLocation = currentLocation;
            this.currentTimeDisposal = currentTimeDisposal;
            this.everywhere_location = everywhere_location;
            this.select_one_location = select_one_location;
        }

        /**
         * compare method of the Comparator.
         *
         * @param o1 the first task to compare
         * @param o2 the second task to compare
         * @return 0 if o1 == o2,
         *           a value less than 0 if o1 < o2
         *           a value greater than 0 if o1 > o2
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(Task o1, Task o2) {
            return Double.compare(computeDynamicSortValue(o2), computeDynamicSortValue(o1));
        }

        /**
         * Compute the number of point of a task, used to sort it.
         *
         * @param task for which we compute the number of points
         * @return number of point of the task
         */
        private double computeDynamicSortValue(Task task) {
            double dynamicSortValue = task.computeStaticSortValue();

            Calendar c = Calendar.getInstance();
            int days = task.daysBetween(c.getTime(), task.getDueDate());

            if (days <= 2) {
                dynamicSortValue += SHORT_DELAY_COEFFICIENT;
            }
            if(task.getLocationName().equals(currentLocation) ||
                    task.getLocationName().equals(everywhere_location) ||
                    currentLocation.equals(select_one_location)) {
                dynamicSortValue += LOCATION_COEFFICIENT;
                if(currentTimeDisposal != TIME_LIMIT && task.getDurationInMinutes() <= currentTimeDisposal) {
                    dynamicSortValue += TIME_COEFFICIENT;
                }
            }
            return dynamicSortValue;
        }
    }
}
