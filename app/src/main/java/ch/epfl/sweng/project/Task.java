package ch.epfl.sweng.project;

import android.os.Parcel;
import android.os.Parcelable;

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

    /**
     * Constructor of the class
     *
     * @param name        Task's name
     * @param description Task's description
     * @throws IllegalArgumentException if the parameter is null
     */
    public Task(String name, String description) {
        if (name == null || description == null) {
            throw new IllegalArgumentException();
        } else {
            this.name = name;
            this.description = description;
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
        if (newDescription == null) {
            throw new IllegalArgumentException();
        } else {
            description = newDescription;
        }
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
