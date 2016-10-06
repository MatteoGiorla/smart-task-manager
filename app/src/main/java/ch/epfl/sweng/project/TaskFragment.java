package ch.epfl.sweng.project;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Class that represents the inflated fragment located in the activity_main
 */
public class TaskFragment extends Fragment {
    private TaskListAdapter mTaskAdapter;
    private ArrayList<Task> taskList;

    /**
     * Method that adds a task in the taskList
     *
     * @param task The task to be added
     * @throws IllegalArgumentException If the task to be added is null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException();
        }
        taskList.add(task);
        mTaskAdapter.notifyDataSetChanged();
    }

    /**
     * Override the onCreate method
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = new ArrayList<>();
    }

    /**
     * Override the onCreateView method
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment
     * @param container          Parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here
     * @return the view of the list to be displayed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTaskAdapter = new TaskListAdapter(
                getActivity(),
                R.layout.list_item_task,
                taskList
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_tasks);
        listView.setAdapter(mTaskAdapter);

        return rootView;
    }
}
