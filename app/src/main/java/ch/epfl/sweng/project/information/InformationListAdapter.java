package ch.epfl.sweng.project.information;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.project.R;

class InformationListAdapter extends ArrayAdapter<InformationItem> {

    InformationListAdapter(Context context, int resource, List<InformationItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;

        //There is no recycled view, we need to create a new one
        if (resultView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            resultView = inflater.inflate(R.layout.list_item_information, parent, false);
        }

        InformationItem itemToBeDisplayed = getItem(position);
        if(itemToBeDisplayed != null) {
            TextView titleView = (TextView) resultView.findViewById(R.id.title_field_information);
            TextView bodyView = (TextView) resultView.findViewById(R.id.body_field_information);

            if (titleView != null) {
                titleView.setText(itemToBeDisplayed.getTitle());
            }
            if (bodyView != null) {
                bodyView.setText(itemToBeDisplayed.getBody());
            }
        }
        return resultView;
    }
}
