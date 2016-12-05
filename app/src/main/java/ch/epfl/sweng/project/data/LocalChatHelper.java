package ch.epfl.sweng.project.data;

import android.content.Context;
import android.widget.Toast;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.chat.Message;
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
        Toast.makeText(mContext, mContext.getString(R.string.no_messages), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateChat(Task task, Message newMessage) {
        task.addMessage(newMessage);
    }
}
