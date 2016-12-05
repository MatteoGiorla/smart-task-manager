package ch.epfl.sweng.project.data;

import android.content.Context;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.chat.MessageAdapter;

public class LocalChatHelper implements ChatHelper {

    private final MessageAdapter mAdapter;
    private final Context mContext;

    public LocalChatHelper(Context context, MessageAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public void retrieveMessages(User user, Task task) {
        // Nothing to retrieve
    }

    @Override
    public void updateChat(Task task) {
        // Nothing to update
    }
}
