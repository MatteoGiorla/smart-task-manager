package ch.epfl.sweng.project.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ch.epfl.sweng.project.Task;

//this class need to be expanded  and completed once the Firebase database will be set
public class FirebaseDataExchanger implements DataExchanger {

    private final Context context;

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
        //only work if wifi for now.
        if(isWifiAvailable()){
            throw new UnsupportedOperationException("Need Firebase database implementation first");
        }else{
            return false;
        }
    }

    @Override
    public boolean retrieveAllData()
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
     * checks if the user's phone has any wifi connection.
     * For now only return true if the connection used by the user is the wifi.
     *
     * @return true if the network is available, false otherwise.
     */
    private boolean isWifiAvailable() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
            return activeNetwork != null && activeNetwork.isConnected();
        }else{
            return false;
        }
    }
}
