package ch.epfl.sweng.project;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskFragment extends Fragment {
    private ArrayAdapter<String> mTaskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] data = {
                "Task 1 Title",
                "Task 2 Title",
                "Task 3 Title",
                "Task 4 Title",
                "Task 5 Title",
                "Task 6 Title",
                "Task 7 Title",
                "Task 8 Title",
                "Task 9 Title",
                "Task 10 Title",
                "Task 11 Title",
                "Task 12 Title",
                "Task 13 Title",
                "Task 14 Title",
                "Task 15 Title",
        };

        List<String> taskList = new ArrayList<String>(Arrays.asList(data));

        mTaskAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_task,
                R.id.list_item_forecast_textview,
                taskList
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_tasks);
        listView.setAdapter(mTaskAdapter);

        return rootView;
    }
}
