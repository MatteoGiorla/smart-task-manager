package ch.epfl.sweng.project.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.project.R;


public class MessageAdapter extends ArrayAdapter<Message> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            resultView = inflater.inflate(R.layout.list_item_chat, parent, false);
        }

        Message messageToDisplay = getItem(position);
        if(messageToDisplay != null) {
            TextView userName = (TextView) resultView.findViewById(R.id.message_user_name);
            TextView messageBody = (TextView) resultView.findViewById(R.id.message_text);
            TextView messageDate = (TextView) resultView.findViewById(R.id.message_date);

            if(userName != null) {
                userName.setText(messageToDisplay.getUserName());
            }

            if(messageBody != null) {
                messageBody.setText(messageToDisplay.getBody());
            }

            if(messageDate != null) {
                messageDate.setText(String.valueOf(messageToDisplay.getTime()));
            }
        }

        return resultView;
    }
}
