package ch.epfl.sweng.project;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;


public class Utils extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
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
     * @return a boolean whether the task in unfilled or not
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

    /**
     * for a task shared with contributors, take care of separating
     * the suffix (@@{creator}@@{sharer}) from the title.
     *
     * @param title the title whom we want to separe the suffix
     * @return an array which element at zero is the title,
     *          and at index 1 is the suffix if it exists,
     *          otherwise the empty string.
     */
    public static String[] separateTitleAndSuffix(String title){
        int charIndex = 0;
        String separatorSequence = mContext.getResources().getString(R.string.contributors_separator);
        String[] stringAndSuffix = new String[2];
        if(title.contains(separatorSequence)){
            stringAndSuffix[0] = title.substring(0, title.indexOf(separatorSequence));
            stringAndSuffix[1] = title.substring(charIndex);
        }else{
            stringAndSuffix[0] = title;
            stringAndSuffix[1] = "";
        }

        return stringAndSuffix;
    }
}
