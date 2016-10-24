package ch.epfl.sweng.project;

/**
 * Class that represents the inflated activity_new_task
 */
public class NewTaskActivity extends TaskActivity {
    public static final String RETURNED_TASK = "ch.epfl.sweng.NewTaskActivity.NEW_TASK";

    /**
     * Check if the title written is unique or not.
     *
     * @param title The new title of the task
     * @return true if the title is already used or false otherwise.
     */
    @Override
    boolean titleIsNotUnique(String title) {
        boolean result = false;
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(title)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    void resultActivity() {
        Task newTask = new Task(title, description);
        intent.putExtra(RETURNED_TASK, newTask);
    }
}
