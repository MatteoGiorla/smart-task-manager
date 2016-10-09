package ch.epfl.sweng.project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ch.epfl.sweng.project.Task;

/**
 * Manages a local database for task data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // If the database schema change, you MUST increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "task.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Overrid the method onCreate to generate the
     * SQL table that will contain the tasks.
     * @param db an SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table to hold the tasks.
        final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + DatabaseContract.TaskEntry.TABLE_NAME + " (" +
                DatabaseContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.TaskEntry.COLUMN_TASK_TITLE + " TEXT NOT NULL, " +
                DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION + " TEXT NOT NULL " +
                " )";

        db.execSQL(SQL_CREATE_TASK_TABLE);
    }


    /**
     * This method will be called when the database version is changed.
     * It delete and create the database from scratch with the new
     * schema.
     *
     * @param db an SQLiteDatabase
     * @param oldVersion old version of the database
     * @param newVersion new version of the dabase
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Note that this only fires if you change the version number for your database.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add the given task in the database.
     *
     * @param task Task that need to be added in the database
     * @return true if the task was added correctly otherwise false
     */
    public boolean addData(Task task) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE, task.getName());
        contentValues.put(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION, task.getDescription());

        long resultQuery = mDatabase.insert(DatabaseContract.TaskEntry.TABLE_NAME, null, contentValues);

        return resultQuery != -1;
    }

    /**
     * Provide all the content in the database.
     *
     * @return Cursor Content of the database
     */
    public Cursor getAllContents() {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        return mDatabase.rawQuery("SELECT * FROM " + DatabaseContract.TaskEntry.TABLE_NAME, null);
    }
}
