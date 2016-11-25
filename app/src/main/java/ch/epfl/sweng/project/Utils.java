package ch.epfl.sweng.project;


import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    private Utils() {}

    /**
     * Encode a given mail to be compatible with keys in firebase
     *
     * @param mail The user email
     * @return The encoded email
     */
    public static String encodeMailAsFirebaseKey(String mail) {
        return mail.replace('.', ' ');
    }


    public static void addUser(User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("users").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();
        userRef.setValue(user);
    }

    public static void updateUser(User user) {
        deleteUser(user);
        addUser(user);
    }


    /**
     * Look at the fields of the task and determines if the task is not completely filled,
     * and thus need to finish in the inbox of unfinished tasks
     *
     * @param task the task to Test
     * @param context the context which serves as getting the good String values.
     *
     * @return a boolean wether the task in unfilled or not
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static boolean isUnfilled(Task task, Context context){
        Calendar c = Calendar.getInstance();
        c.setTime(task.getDueDate());
        int year = c.get(Calendar.YEAR);

        return task.getLocationName().equals(context.getString(R.string.select_one))
                || task.getDuration() == 0 || year == 1899;
    }

    /**
     * Deleter a user in the database
     *
     * @param user The user to be deleted
     */
    private static void deleteUser(User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("users").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();
        userRef.removeValue();
    }
}
