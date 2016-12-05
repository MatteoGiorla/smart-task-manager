package ch.epfl.sweng.project.data;


import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.chat.Message;
import ch.epfl.sweng.project.chat.MessageAdapter;

public class FirebaseChatHelper implements ChatHelper {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final MessageAdapter mAdapter;
    private final Context mContext;

    public FirebaseChatHelper(Context context, MessageAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public void retrieveMessages(User user, final Task task) {
        Query mChat = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();
        if(mChat != null) {
            mChat.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    retrieveListOfMessages(dataSnapshot, task);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    @Override
    public void updateChat(Task task, Message newMessage) {
        task.addMessage(newMessage);
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.setValue(task);
        }
    }

    private void retrieveListOfMessages(DataSnapshot dataSnapshot, Task task) {
        if(dataSnapshot.getChildrenCount() == 0 || dataSnapshot == null) {
            Toast.makeText(mContext, mContext.getString(R.string.no_messages), Toast.LENGTH_SHORT).show();
        }
        for(DataSnapshot t : dataSnapshot.getChildren()) {
            List<Message> newListOfMessages = (List<Message>) t.child("listOfMessages").getValue(Message.class);
            task.setListOfMessages(newListOfMessages);
        }
        mAdapter.notifyDataSetChanged();
    }
}
