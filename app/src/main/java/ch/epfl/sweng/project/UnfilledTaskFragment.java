package ch.epfl.sweng.project;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_IS_DELETED;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_IS_MODIFIED;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_STATUS_KEY;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_TO_BE_DELETED_INDEX;
import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY;
import static ch.epfl.sweng.project.TaskFragment.TASKS_LIST_KEY;

/**
 * Class that represents the inflated fragment located in the unfilled_task_activity
 */
public class UnfilledTaskFragment extends Fragment {
    private final int editTaskRequestCode = 3;


    private TaskListAdapter mTaskAdapter;
    private ArrayList<Task> unfilledTaskList;
    private ArrayList<Task> filledTaskList;
    private RecyclerView recyclerView;
    private final Paint p = new Paint();

    /**
     * Override the onCreate method. It retrieves all the task of the user
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();

        filledTaskList = new ArrayList<>();
        unfilledTaskList = bundle.getParcelableArrayList(MainActivity.UNFILLED_TASKS);
        mTaskAdapter = new TaskListAdapter(getActivity(), unfilledTaskList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);

        swipeLayout.setEnabled(false);
    }

    /**
     * Override the onCreateView method to initialize the adapter of
     * the ListView.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment
     * @param container          Parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here
     * @return the view of the list to be displayed
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_tasks);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        recyclerView.setAdapter(mTaskAdapter);
        initSwipe();

        return rootView;
    }


    /**
     * Method called when an activity launch inside UnfilledTaskActivity,
     * is finished. This method is triggered only if we use
     * startActivityForResult.
     *
     * @param requestCode The integer request code supplied to startActivityForResult
     *                    used as an identifier.
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An intent which can return result data to the caller.
     * @throws IllegalArgumentException if the returned extras from EditTaskActivity are
     *                                  invalid
     * @throws SQLiteException          if more that one row was changed when editing a task.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case when we returned from the EditTaskActivity
        if (requestCode == editTaskRequestCode && resultCode == RESULT_OK) {
            int taskStatus = data.getIntExtra(TASK_STATUS_KEY, -1);
            if (taskStatus == -1)
                throw new IllegalArgumentException("Error with the intent form EditTaskActivity");

            switch (taskStatus) {
                case TASK_IS_MODIFIED:
                    onEditTaskActivityResult(data);
                    break;
                case TASK_IS_DELETED:
                    int taskIndex = data.getIntExtra(TASK_TO_BE_DELETED_INDEX, -1);
                    if (taskIndex == -1) {
                        throw new IllegalArgumentException("Error with the task to be deleted index");
                    }
                    unfilledTaskList.remove(taskIndex);
                    break;
            }
        }
        mTaskAdapter.notifyDataSetChanged();
    }

    /**
     * Method called when we return from editing a task inside EditTaskActivity.
     *
     * @param data The returned Intent of EditTaskActivity.
     */
    private void onEditTaskActivityResult(Intent data) {
        // Get result from the result intent.
        Task editedTask = data.getParcelableExtra(EditTaskActivity.RETURNED_EDITED_TASK);
        int indexEditedTask = data.getIntExtra(EditTaskActivity.RETURNED_INDEX_EDITED_TASK, -1);
        if (indexEditedTask == -1 || editedTask == null) {
            throw new IllegalArgumentException("Invalid extras returned from EditTaskActivity !");
        } else {
            //if the task has been fulfilled, we can put it on the temporary list of good tasks.
            if(Utils.isUnfilled(editedTask, this.getActivity().getApplicationContext())){
                unfilledTaskList.set(indexEditedTask, editedTask);
            }else{
                unfilledTaskList.remove(indexEditedTask);
                filledTaskList.add(editedTask);
            }
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Start the EditTaskActivity for result when the user press the edit button.
     * The task index and the taskList are passed as extras to the intent.
     *
     * @param position Position of the task in the list.
     */
    private void startEditTaskActivity(int position) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);

        intent.putExtra(INDEX_TASK_TO_BE_EDITED_KEY, position);
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, unfilledTaskList);

        startActivityForResult(intent, editTaskRequestCode);
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @RequiresApi(Build.VERSION_CODES.M)
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    createSnackBar(position);
                } else {
                    startEditTaskActivity(position);
                }
            }

            @RequiresApi(Build.VERSION_CODES.M)
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(getResources().getColor(R.color.green_swipe,null));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mode_edit_white_48dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(getResources().getColor(R.color.colorPrimary,null));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.trash_36dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void createSnackBar(final int position) {
        FrameLayout layout = (FrameLayout) getActivity().findViewById(R.id.unfilled_tasks_container);
        final Task mTask = unfilledTaskList.get(position);

        Snackbar snackbar = Snackbar
                .make(layout, mTask.getName() + getString(R.string.has_been_deleted), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTaskAdapter.add(mTask, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.orange_yellow, null));
        snackbar.show();
        mTaskAdapter.remove(position);
    }

    /**
     * Getter for the taskList of unfilled tasks
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getUnfilledTaskList() {
        if(unfilledTaskList != null){
            return new ArrayList<>(unfilledTaskList);
        }else{
            return null;
        }
    }

    /**
     * Getter for the taskList of task that have been filled
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getFilledTaskList() {
        if(unfilledTaskList != null){
            return new ArrayList<>(filledTaskList);
        }else{
            return null;
        }
    }
}
