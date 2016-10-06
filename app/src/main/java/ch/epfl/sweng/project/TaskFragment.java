package ch.epfl.sweng.project;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class TaskFragment extends Fragment {
    private TaskListAdapter mTaskAdapter;
    private ArrayList<Task> list_tasks;

    public void addTask(Task task) {
        list_tasks.add(task);
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_tasks = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTaskAdapter = new TaskListAdapter(
                getActivity(),
                R.layout.list_item_task,
                list_tasks
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_tasks);
        listView.setAdapter(mTaskAdapter);

        return rootView;
    }
}
