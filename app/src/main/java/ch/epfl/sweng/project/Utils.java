package ch.epfl.sweng.project;


import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    private Utils() {
    }

    /**
     * Encode a given mail to be compatible with keys in firebase
     *
     * @param mail The user email
     * @return The encoded email
     */
    public static String encodeMailAsFirebaseKey(String mail) {
        return mail.replace('.', ' ');
    }


    /**
     * Look at the fields of the task and determines if the task is not completely filled,
     * and thus need to finish in the inbox of unfinished tasks
     *
     * @param task    the task to Test
     * @param context the context which serves as getting the good String values.
     * @return a boolean wether the task in unfilled or not
     */
    public static boolean isUnfilled(Task task, Context context) {

        return isLocationUnfilled(task, context)
                || isDurationUnfilled(task) || isDueDateUnfilled(task);
    }

    public static boolean isLocationUnfilled(Task task, Context context) {
        return task.getLocationName().equals(context.getString(R.string.select_one));
    }

    public static boolean isDurationUnfilled(Task task) {
        return task.getDuration() == 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static boolean isDueDateUnfilled(Task task) {
        Calendar c = Calendar.getInstance();
        c.setTime(task.getDueDate());
        int year = c.get(Calendar.YEAR);
        return year == 1899;
    }
}
