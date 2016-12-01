package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;

/**
 * Interface that behave as a Proxy to a database
 */
public interface TaskHelper {

    /**
     * Take care of retrieving all user data if there is no
     * data locally stored on the app.
     *
     * @param user The user we want to retrieve data from.
     */
    void retrieveAllData(User user);

    /**
     * Add a tasks to the remote storage device
     *
     * @param task the task to add
     * @param position the position of the task in the list
     */
    void addNewTask(Task task, int position);

    /**
     * Update the task that has seen some change locally
     *
     * @param original the task before update
     * @param updated  the updated task
     * @param position the position of the task in the list
     */
    void updateTask(Task original, Task updated, int position);

    /**
     * Deletes the task from the remote storage device
     *
     * @param task the task to be deleted
     * @param position the position of the task in the list
     */
    void deleteTask(Task task, int position);

    /**
     * Take care of retrieving all user data when the
     * user force it.
     *
     * @param user The user we want to retrieve data from.
     */
    void refreshData(User user);
}
