package ch.epfl.sweng.project;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Task is the class representing a task
 */
public class Task implements Parcelable {
    private String name;
    private String description;

    /**
     * Constructor of the class
     *
     * @param name        task's name
     * @param description task's description
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

    /* Order must be kept between the constructor and writeToParcel */
    private Task(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
    }

    /**
     * Setter to modify the task's name
     *
     * @param newName the new task's name
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
     * @param newDescription the new task's description
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
