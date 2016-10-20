package ch.epfl.sweng.project;

public interface dataExchanger {

    /**
     * Checks if the remote storage device can be accessed at the
     * moment.
     *
     * @return true if the remote storage device can be accessed,
     *          false otherwise
     */
    public boolean hasAccess();
    /**
     * Take care of retrieving all user data if there is no
     * data locally stored on the app.
     *
     * @return true if all data have been correctly retrieved,
     *          false if for some reason the action could not
     *          be performed
     */
    public boolean retrieveAllData();

    /**
     *  Add a tasks to the remote storage device
     *
     * @param task the task to add
     */
    public void addNewTask(Task task);

    /**
     * Update the task that has seen some change locally
     *
     * @param original the task before update
     * @param updated the updated task
     */
    public void updateTask(Task original, Task updated);

    /**
     * Deletes the task from the remote storage device
     *
     * @param task the task to be deleted
     */
    public void deleteTask(Task task);
}
