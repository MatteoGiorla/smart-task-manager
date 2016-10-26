package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;

interface DataExchanger {

    /**
     * Recover the information from the user in the
     * database and return it.
     *
     * @return the User recovered from the database
     */
    User retrieveUserInformation();

    /**
     * Take care of retrieving all user data if there is no
     * data locally stored on the app.
     *
     * @param user The user we want to retrieve data from.
     * @return true if all data have been correctly retrieved,
     *          false if for some reason the action could not
     *          be performed
     */
    boolean retrieveAllData(User user);

    /**
     *  Add a tasks to the remote storage device
     *
     * @param task the task to add
     */
    void addNewTask(Task task);

    /**
     * Update the task that has seen some change locally
     *
     * @param original the task before update
     * @param updated the updated task
     */
    void updateTask(Task original, Task updated);

    /**
     * Deletes the task from the remote storage device
     *
     * @param task the task to be deleted
     */
    void deleteTask(Task task);

    /**
     *
     * @param user
     */
    void addUser(User user);

    void updateUser(User user);
}
