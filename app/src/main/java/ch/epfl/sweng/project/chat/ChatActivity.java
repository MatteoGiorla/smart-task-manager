package ch.epfl.sweng.project.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;

public class ChatActivity extends AppCompatActivity {
    public static final String TASK_CHAT_KEY = "ch.epfl.sweng.project.chat.TASK_CHAT_KEY";

    private Intent intent;
    private Task task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Initialise the Task and check its validity
        getAndCheckIntent();

        setAdapter();

        Button sendMssgButton = (Button) findViewById(R.id.send_message_button);
        sendMssgButton.setOnClickListener(new SendMessageOnClickListener());
    }

    private void setAdapter() {
        //Initialise the MessageAdapter
        MessageAdapter mAdapter = new MessageAdapter(this,
                R.layout.list_item_chat,
                task.getListOfMessages());

        //Get the listView
        ListView mssgListView = (ListView) findViewById(R.id.list_of_messages);
        //Bind the adapter to the listView
        mssgListView.setAdapter(mAdapter);
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
                }
            }
        }
    }
}
