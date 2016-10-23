package ch.epfl.sweng.project.data;

import ch.epfl.sweng.project.Task;

interface DataExchanger {

    /**
     * Checks if the remote storage device can be accessed at the
     * moment.
     *
     * @return true if the remote storage device can be accessed,
     *          false otherwise
     */
    boolean hasAccess();
    /**
     * Take care of retrieving all user data if there is no
     * data locally stored on the app.
     *
     * @return true if all data have been correctly retrieved,
     *          false if for some reason the action could not
     *          be performed
     */
    boolean retrieveAllData();

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
}
