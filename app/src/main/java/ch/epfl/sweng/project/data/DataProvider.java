package ch.epfl.sweng.project.data;

import android.content.Context;

import java.util.ArrayList;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;

public class DataProvider {
    public static final String FIREBASE_PROVIDER = "Firebase";
    public static final String TEST_PROVIDER = "Tests";

    private static String mProvider = FIREBASE_PROVIDER;
    private final TaskListAdapter mAdapter;
    private final ArrayList<Task> mTaskList;
    private final Context mContext;

    public DataProvider(Context context, TaskListAdapter adapter, ArrayList<Task> taskList) {
        mAdapter = adapter;
        mTaskList = taskList;
        mContext = context;
    }

    public DataExchanger getProvider() {
        switch (mProvider) {
            case FIREBASE_PROVIDER:
                return new FirebaseDataExchanger(mContext, mAdapter, mTaskList);
            case TEST_PROVIDER:
                return new LocalDataExchanger(mAdapter, mTaskList);
            default:
                throw new IllegalArgumentException("This provider does not exists !");
        }
    }

    public static void setProvider(String provider) {
        mProvider = provider;
    }
}
