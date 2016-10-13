package ch.epfl.sweng.project.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the database.
 */
public class DatabaseContract {

    // The name of the database file
    public static final String DATABASE_NAME = "task.db";

    /**
     * Define the table containing the tasks in the database.
     */
    public static final class TaskEntry implements BaseColumns {

        // The name of the table in the database
        public static final String TABLE_NAME = "task";

        // The column containing the title of the tasks
        public static final String COLUMN_TASK_TITLE = "title";

        // The column containing the description of the tasks
        public static final String COLUMN_TASK_DESCRIPTION = "description";
    }
}
