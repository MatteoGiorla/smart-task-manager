package ch.epfl.sweng.project.data;


import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.chat.Message;
import ch.epfl.sweng.project.chat.MessageAdapter;

public class FirebaseChatHelper {

    private Query mQuery;
    private ValueEventListener mListener;

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final MessageAdapter mAdapter;
    private final Context mContext;

    public FirebaseChatHelper(Context context, MessageAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    public void retrieveMessages(String mail, final Task task) {
        final Query mChat = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
        if(mChat != null) {
            mChat.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mQuery = mChat;
                    mListener = this;
                    retrieveListOfMessages(dataSnapshot, task);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void updateChat(Task task, Message newMessage) {
        task.addMessage(newMessage);
        if(Utils.hasContributors(task)){
            for (String mail : task.getListOfContributors()) {
                Task updateTask = Utils.sharedTaskPreProcessing(task, mail);
                DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(updateTask.getName()).getRef();
                taskRef.setValue(updateTask);
            }
        }else{
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(task.getListOfContributors().get(0))).child(task.getName()).getRef();
            taskRef.setValue(task);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void removeListener() {
        if(mQuery != null && mListener != null) {
            mQuery.removeEventListener(mListener);
        }
    }

    private void retrieveListOfMessages(DataSnapshot dataSnapshot, Task task) {
        if(dataSnapshot.getChildrenCount() == 0) {
            Toast.makeText(mContext, mContext.getString(R.string.no_messages), Toast.LENGTH_SHORT).show();
        }else {
            mAdapter.clear();
            GenericTypeIndicator<List<Message>> gen = new GenericTypeIndicator<List<Message>>() {};
            List<Message> newListOfMessages = dataSnapshot.child("listOfMessages").getValue(gen);
            if(newListOfMessages != null) {
                task.setListOfMessages(newListOfMessages);
                mAdapter.addAll(newListOfMessages);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
