package ch.epfl.sweng.project.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;

public class ChatActivity extends AppCompatActivity {
    public static final String TASK_CHAT_KEY = "ch.epfl.sweng.project.chat.TASK_CHAT_KEY";

    private Intent intent;
    private Task task;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Initialise the Task and check its validity
        getAndCheckIntent();

        //Initialise the MessageAdapter
        mAdapter = new MessageAdapter(this, R.layout.list_item_chat, task.getListOfMessages());

    }


//    private void populate

    private void getAndCheckIntent() {
        intent = getIntent();
        if(intent == null) {
            throw new IllegalArgumentException("Intent passed to ChatActivity is null");
        }
        getAndCheckIntentExtra();
    }

    private void getAndCheckIntentExtra() {
        task = intent.getParcelableExtra(TASK_CHAT_KEY);
        if(task == null) {
            throw new IllegalArgumentException("Task passed with the intent to ChatActivity is null");
        }
    }
}
