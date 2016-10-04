package ch.epfl.sweng.project;

/**
 * Created by charlesparzy on 04/10/2016.
 */

/**
 * Task is the class representing a task
 */
public class Task {
    String name;

    /**
     * Constructor of the class
     *
     * @param name task's name
     * @throws IllegalArgumentException if the parameter is null
     */
    public Task(String name) {
        if(name == null) {
            throw new IllegalArgumentException();
        } else {
            this.name = new String(name);
        }
    }

    /**
     * Setter to modify the task's name
     *
     * @param newName the new task's name
     * @throws IllegalArgumentException if newName is null
     */
    public void setName(String newName) {
        if(newName == null) {
            throw new IllegalArgumentException();
        } else {
            name = new String(newName);
        }
    }

    /**
     * Getter returning a copy of the task's name
     */
    public String getName() {
        return new String(name);
    }
}
