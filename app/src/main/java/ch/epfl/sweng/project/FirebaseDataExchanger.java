package ch.epfl.sweng.project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.lang.UnsupportedOperationException;

import ch.epfl.sweng.project.authentication.User;

//this class need to be expanded  and completed once the Firebase database will be set
public class FirebaseDataExchanger implements dataExchanger {

    private Context context;

    /**
     * Only constructor of FirebaseDataExchanger for the moment
     *
     * @param context current context in which this class is used
     */
    public FirebaseDataExchanger(Context context){
        this.context = context;

    }

    @Override
    public boolean hasAccess()
            throws UnsupportedOperationException{
        if(isNetworkAvailable()){
            throw new UnsupportedOperationException("Need Firebase database implementation first");
        }else{
            return false;
        }
    }

    @Override
    public boolean retrieveAllData(User user)
            throws  UnsupportedOperationException{
        throw new UnsupportedOperationException("Need Firebase database implementation first");
    }

    @Override
    public void addNewTask(Task task)
            throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Need Firebase database implementation first");
    }

    @Override
    public void updateTask(Task original, Task updated)
            throws UnsupportedOperationException{
        //update only if the task has effectively been changed
        if(!(original.getName().equals(updated.getName())
                && original.getDescription().equals(updated.getDescription()))){
            throw new UnsupportedOperationException("Need Firebase database implementation first");
        }
    }

    @Override
    public void deleteTask(Task task)
            throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Need Firebase database implementation first");
    }

    /**
     * checks if the user's phone has any network connection.
     * source : http://stackoverflow.com/questions/8503553/check-if-android-phone-has-access-to-internet
     *
     * @return true if the network is available, false otherwise.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
