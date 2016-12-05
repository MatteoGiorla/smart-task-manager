package ch.epfl.sweng.project.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.data.ChatHelper;
import ch.epfl.sweng.project.data.ChatProvider;

public class ChatActivity extends AppCompatActivity {
    public static final String TASK_CHAT_KEY = "ch.epfl.sweng.project.chat.TASK_CHAT_KEY";

    private Intent intent;
    private Task task;
    private ChatHelper chatHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /*Toolbar mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        initializeToolbar(mToolbar);

        mToolbar.setNavigationOnClickListener(new ReturnArrowListener());*/


        //Initialise the Task and check its validity
        getAndCheckIntent();

        //Initialise the MessageAdapter
        MessageAdapter mAdapter = new MessageAdapter(this,
                R.layout.list_item_chat,
                task.getListOfMessages());

        //Get the listView
        ListView mssgListView = (ListView) findViewById(R.id.list_of_messages);
        //Bind the adapter to the listView
        mssgListView.setAdapter(mAdapter);

        FloatingActionButton sendMssgButton = (FloatingActionButton) findViewById(R.id.send_message_button);
        sendMssgButton.setOnClickListener(new SendMessageOnClickListener());

        //Instantiation of the ChatHelper
        chatHelper = new ChatProvider(this, mAdapter).getChatProvider();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatHelper.removeListener();
    }

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

    /**
     * Start the toolbar and enable that back button on the toolbar.
     *
     * @param mToolbar the toolbar of the activity
     */
    private void initializeToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private class SendMessageOnClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            EditText editMssg = (EditText) findViewById(R.id.input);
            if(editMssg.getText() != null) {
                String mssgText = editMssg.getText().toString();

                if(!mssgText.isEmpty()) {
                    String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    long time = new Date().getTime();
                    Message newMessage = new Message(userName, mssgText, time);
                    task.addMessage(newMessage);
                    chatHelper.updateChat(task, newMessage);
                }
            }
        }
    }

    /**
     * Class that implements OnClickListener.
     * It represents a OnClickListener on the return arrow.
     */
    private class ReturnArrowListener implements View.OnClickListener {

        /**
         * Called when the return arrow has been clicked.
         *
         * @param v The view that was clicked, the return arrow.
         */
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
