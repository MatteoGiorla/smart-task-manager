package ch.epfl.sweng.project;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.data.TaskHelper;
import ch.epfl.sweng.project.data.TaskProvider;
import ch.epfl.sweng.project.information.TaskInformationActivity;
import ch.epfl.sweng.project.notification.TaskNotification;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_IS_DELETED;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_IS_MODIFIED;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_STATUS_KEY;

/**
 * Class that represents the inflated fragment located in the activity_main
 */
public class TaskFragment extends Fragment {
    public static final String INDEX_TASK_TO_BE_EDITED_KEY = "ch.epfl.sweng.TaskFragment.INDEX_TASK_TO_BE_EDITED";
    public static final String TASKS_LIST_KEY = "ch.epfl.sweng.TaskFragment.TASKS_LIST";
    public static final String INDEX_TASK_TO_BE_DISPLAYED = "ch.epfl.sweng.TaskFragment.INDEX_TASK_TO_BE_DISPLAYED";
    public static final int editTaskRequestCode = 2;
    public static final int displayTaskRequestCode = 3;


    private TaskListAdapter mTaskAdapter;
    private ArrayList<Task> taskList;
    private TaskHelper mDatabase;
    private User currentUser;
    private RecyclerView recyclerView;
    private Paint p = new Paint();

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

        if (bundle != null) {
            currentUser = bundle.getParcelable(MainActivity.USER_KEY);
            if (currentUser == null) {
                throw new IllegalArgumentException("User passed with the intend is null");
            }
        } else {
            throw new NullPointerException("User was badly passed from MainActivity to TaskFragment !");
        }
        taskList = new ArrayList<>();
        mTaskAdapter = new TaskListAdapter(getActivity(), taskList);
    }

    /**
     * Override onActivityCreated method. It set the refresh swipe colors and listener.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mDatabase.refreshData(currentUser);
                                swipeLayout.setRefreshing(false);
                            }
                        }, 2500);
            }
        });

        swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light));
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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initSwipe();

        registerForContextMenu(recyclerView);

        TaskProvider provider = new TaskProvider(getActivity(), mTaskAdapter, taskList);
        mDatabase = provider.getTaskProvider();
        mDatabase.retrieveAllData(currentUser);

        return rootView;
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
                    createSnackBar(position, false);
                } else {
                    createSnackBar(position, true);
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
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_36dp);
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

    /**
     * Override the onCreateContextMenu method.
     * This method creates a floating context menu.
     *
     * @param menu     The context menu that is being built.
     * @param v        The view for which the context menu is being built.
     * @param menuInfo Extra information about the item
     *                 for which the context menu should be shown
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.floating_context_menu, menu);
    }

    /**
     * Override the onContextItemSelected.
     * This method decides what to do depending of the context menu's item
     * selected by the user.
     *
     * @param item The context menu item that was selected
     * @return Return false to allow normal context menu processing to proceed,
     * true to consume it here
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.floating_task_delete:
                removeTask(itemInfo, false);
                return true;
            case R.id.floating_task_edit:
                startEditTaskActivity(itemInfo.position);
                return true;
            case R.id.floating_task_done:
                removeTask(itemInfo, true);
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Method called when an activity launch inside MainActivity,
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
            onEditTaskActivityResult(data);
        } else if (requestCode == displayTaskRequestCode && resultCode == RESULT_OK) {
            int taskStatus = data.getIntExtra(TASK_STATUS_KEY, -1);
            if (taskStatus == -1)
                throw new IllegalArgumentException("Error with the intent form TaskInformationActivity");

            switch (taskStatus) {
                case TASK_IS_MODIFIED:
                    onEditTaskActivityResult(data);
                    break;
                case TASK_IS_DELETED:
                    int taskIndex = data.getIntExtra(TaskInformationActivity.TASK_TO_BE_DELETED_INDEX, -1);
                    if (taskIndex == -1) {
                        throw new IllegalArgumentException("Error with the task to be deleted index");
                    }
                    removeTaskAction(taskIndex, false);
            }
        }
        //sortTaskStatically();
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
            mDatabase.updateTask(taskList.get(indexEditedTask), editedTask);
            //taskList.set(indexEditedTask, editedTask);
            mTaskAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(),
                    editedTask.getName() + getString(R.string.info_updated),
                    Toast.LENGTH_SHORT).show();

            //Create a notification
            new TaskNotification(taskList, getActivity()).execute(taskList.size(), taskList.size());
        }
    }

    /**
     * Method that adds a task in the taskList and in the database.
     *
     * @param task The task to be added
     * @throws IllegalArgumentException If the task to be added is null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException();
        }
        mDatabase.addNewTask(task);
        sortTaskStatically();

        //Update notifications
        new TaskNotification(taskList, getActivity()).createUniqueNotification(taskList.size() - 1);
    }

    /**
     * Start the EditTaskActivity for result when the user press the edit button.
     * The task index and the taskList are passed as extras to the intent.
     *
     * @param position Position of the task in the list
     */
    private void startEditTaskActivity(int position) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);

        intent.putExtra(INDEX_TASK_TO_BE_EDITED_KEY, position);
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, taskList);

        startActivityForResult(intent, editTaskRequestCode);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void createSnackBar(final int position, final Boolean isDone) {
        FloatingActionButton add_button = (FloatingActionButton) getActivity().findViewById(R.id.add_task_button);

        final Task mTask = taskList.get(position);
        String title;
        if(isDone) {
            title = mTask.getName() + getString(R.string.has_been_done);
        }else{
            title = mTask.getName() + getString(R.string.has_been_deleted);
        }

        Snackbar snackbar = Snackbar
                .make(add_button, title, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskList.add(position, mTask);
                        mTaskAdapter.notifyItemInserted(position); //mBookAdapter is my Adapter class
                        recyclerView.scrollToPosition(position);
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != DISMISS_EVENT_ACTION) {
                            taskList.add(position, mTask);
                            removeTaskAction(position, isDone);
                        }
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.orange_yellow, null));
        snackbar.show();
        mTaskAdapter.remove(mTask);
    }

    /**
     * Remove a task from the database and the taskList.
     *
     * @param itemInfo Extra information about the item
     *                 for which the context menu should be shown
     * @param isDone   true if the task is done, otherwise false.
     * @throws SQLiteException if an error occurred
     */
    private void removeTask(AdapterView.AdapterContextMenuInfo itemInfo, Boolean isDone) {
        int position = itemInfo.position;
        removeTaskAction(position, isDone);
    }

    /**
     * Private method executing the actions needed to remove the task.
     * It removes the task from the database.
     *
     * @param position Position of the task to be removed.
     * @param isDone   Boolean indicating if the task is done.
     */
    private void removeTaskAction(int position, Boolean isDone) {
        Task taskToBeDeleted = taskList.get(position);

        String taskName = taskToBeDeleted.getName();

        mDatabase.deleteTask(taskToBeDeleted);

        Context context = getActivity().getApplicationContext();
        String TOAST_MESSAGE;
        if (isDone) {
            TOAST_MESSAGE = taskName + getString(R.string.info_done);
        } else {
            TOAST_MESSAGE = taskName + getString(R.string.info_deleted);
        }
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, TOAST_MESSAGE, duration).show();

        //Update notifications
        new TaskNotification(taskList, getActivity()).execute(taskList.size() + 1, taskList.size());
    }

    /**
     * Method that launch the dynamic sort on the tasks.
     *
     * @param currentLocation     User's current location
     * @param currentTimeDisposal User's current disposal time
     */
    public void sortTasksDynamically(String currentLocation, int currentTimeDisposal, String everywhere_location, String select_one_location) {
        mTaskAdapter.sort(Task.getDynamicComparator(currentLocation, currentTimeDisposal, everywhere_location, select_one_location));
    }

    /**
     * Method that launch the static sort on the tasks.
     */
    public void sortTaskStatically() {
        mTaskAdapter.sort(Task.getStaticComparator());
    }

    /**
     * Getter for the taskList
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getTaskList() {
        if(taskList != null){
            return new ArrayList<>(taskList);
        }else{
            return null;
        }
    }
}
