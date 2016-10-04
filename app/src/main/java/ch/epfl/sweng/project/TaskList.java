package ch.epfl.sweng.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlesparzy on 04/10/2016.
 */

/**
 * TaskList is the class representing the list of tasks.
 */
public class TaskList {
    List<Task> listOfTasks;

    /**
     * Constructor of the TaskList class
     */
    public TaskList() {
        listOfTasks = new ArrayList<Task>();
    }

    /**
     * Getter returning the size of the list, i.e. the number of tasks
     */
    public int getNumberOfTasks() {
        return listOfTasks.size();
    }

    /**
     * Getter returning a unmodifiable copy of the list of tasks
     */
    public List<Task> getListOfTasks() {
        return new ArrayList<Task>(listOfTasks);
    }

    /**
     * Method that allows to add task in the list
     *
     * @throws IllegalArgumentException if the task
     * to be added is null
     * @param task the task to be added
     */
    public void addTask(Task task) {
        if(task == null) {
            throw new IllegalArgumentException();
        } else {
            listOfTasks.add(task);
        }
    }

    /**
     * Method that allows to remove a task from the list
     *
     * @throws IllegalArgumentException if the task
     * to be removed is not in the list
     * @param task the task to be removed
     */
    public void removeTask(Task task) {
        int indexToBeRemoved = listOfTasks.indexOf(task);
        if (indexToBeRemoved == -1) {
            throw new IllegalArgumentException();
        } else {
            listOfTasks.remove(indexToBeRemoved);
        }
    }
}
