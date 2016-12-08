package ch.epfl.sweng.project.chat;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.project.R;


public class MessageAdapter extends ArrayAdapter<Message> {

    private final DateFormat dateFormat;
    private final String currentUserName;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    MessageAdapter(Context context, int resource, List<Message> objects,  String currentUserName) {
        super(context, resource, objects);
        this.currentUserName = currentUserName;
        dateFormat = DateFormat.getDateInstance();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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
                messageDate.setText(dateFormat.format(messageToDisplay.getTime()));
            }

            LinearLayout messageContainer = (LinearLayout) resultView.findViewById(R.id.message_container);
            FrameLayout.LayoutParams params = new FrameLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if(messageContainer != null) {
                if(messageToDisplay.getUserName().equals(currentUserName)) {
                    messageContainer.setGravity(Gravity.END);
                    params.gravity = Gravity.END;

                } else {
                    messageContainer.setGravity(Gravity.START);
                    params.gravity = Gravity.START;
                }

                messageContainer.setLayoutParams(params);
            }

        }

        return resultView;
    }
}
