package ch.epfl.sweng.project;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import static android.app.Activity.RESULT_OK;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_IS_DELETED;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_IS_MODIFIED;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_STATUS_KEY;
import static ch.epfl.sweng.project.EditTaskActivity.TASK_TO_BE_DELETED_INDEX;

public abstract class TaskFragment extends Fragment {
    public static final String INDEX_TASK_TO_BE_EDITED_KEY = "ch.epfl.sweng.TaskFragment.INDEX_TASK_TO_BE_EDITED";
    public static final String TASKS_LIST_KEY = "ch.epfl.sweng.TaskFragment.TASKS_LIST";
    public static final int EDIT_TASK_REQUEST_CODE = 3;

    private RecyclerView recyclerView;
    private Bundle bundle;
    private final Paint p = new Paint();

    protected User currentUser;

    abstract int getIconSwipe();

    abstract void createSnackBar(final int position, final Boolean isDone,
                                 final RecyclerView recyclerView);

    abstract void setOnActivityCreated(SwipeRefreshLayout swipeRefreshLayout);

    abstract void setOnCreateView(RecyclerView recyclerView);

    abstract void onEditTaskActivityResult(Intent data);

    abstract void setOnSwipe(RecyclerView recyclerView, int position, int direction);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = this.getArguments();
        if(bundle == null) {
            throw new NullPointerException("Bundle passed to the fragment is null");
        }

        currentUser = getBundle().getParcelable(MainActivity.USER_KEY);
        if (currentUser == null) {
            throw new IllegalArgumentException("User passed with the intend is null");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.swipe_refresh);
        setOnActivityCreated(swipeLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_tasks);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        setOnCreateView(recyclerView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case when we returned from the EditTaskActivity
        if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
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
                    createSnackBar(taskIndex, false, recyclerView);
                    break;
            }
        }
    }

    void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @RequiresApi(Build.VERSION_CODES.M)
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        setOnSwipe(recyclerView, position, direction);
                    }

                    @RequiresApi(Build.VERSION_CODES.M)
                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                            int actionState, boolean isCurrentlyActive) {

                        Bitmap icon;
                        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;

                            if(dX > 0){
                                p.setColor(getResources().getColor(R.color.green_swipe,null));
                                RectF background = new RectF((float) itemView.getLeft(),
                                        (float) itemView.getTop(), dX,(float) itemView.getBottom());

                                c.drawRect(background,p);
                                icon = BitmapFactory.decodeResource(getResources(), getIconSwipe());

                                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,
                                        (float) itemView.getTop() + width,
                                        (float) itemView.getLeft()+ 2*width,
                                        (float)itemView.getBottom() - width);

                                c.drawBitmap(icon,null,icon_dest,p);
                            } else {
                                p.setColor(getResources().getColor(R.color.colorPrimary,null));

                                RectF background = new RectF((float) itemView.getRight() + dX,
                                        (float) itemView.getTop(),
                                        (float) itemView.getRight(),
                                        (float) itemView.getBottom());

                                c.drawRect(background,p);
                                icon = BitmapFactory.decodeResource(getResources(), R.drawable.trash_36dp);

                                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,
                                        (float) itemView.getTop() + width,
                                        (float) itemView.getRight() - width,
                                        (float)itemView.getBottom() - width);

                                c.drawBitmap(icon,null,icon_dest,p);
                            }
                        }
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                                actionState, isCurrentlyActive);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    Bundle getBundle() {
        return new Bundle(bundle);
    }
}