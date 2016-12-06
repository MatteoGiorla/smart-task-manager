package ch.epfl.sweng.project.chat;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ch.epfl.sweng.project.R;


public class MessageAdapter extends ArrayAdapter<Message> {

    private DateFormat dateFormat;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        dateFormat = DateFormat.getDateInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                messageDate.setText(dateFormat.format(messageToDisplay.getTime()));
            }

            /*if(messageToDisplay.getUserName().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                resultView.setBackground(getContext().getResources().getDrawable(R.drawable.right_chat_buble));
            } else {
                resultView.setBackground(getContext().getResources().getDrawable(R.drawable.left_chat_bubble));
            }*/
        }

        return resultView;
    }
}
