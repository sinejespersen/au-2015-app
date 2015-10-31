package itsmap.SV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

/*
* From
* https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
*
* In Android development, any time we want to show a vertical list of scrollable items we will
* use a ListView which has data populated using an Adapter. The simplest adapter to use is
* called an ArrayAdapter because the adapter converts an ArrayList of objects into View items
* loaded into the ListView container.
* */

public class EventAdapter extends ArrayAdapter<AnEvent> {

    private Context context;
    private ArrayList<AnEvent> eventList;

    public EventAdapter(Context context, ArrayList<AnEvent> eventList) {
        super(context, -1, eventList);

        this.context = context;
        this.eventList = eventList;
    }

    private static class EventHolder {
        public TextView eventName;
        public TextView eventTime;
        public TextView eventLocation;
        public CheckBox chkBox;
    }

    @Override
    //The adapter inflates the layout for each row in its getView() method and assigns
    //the data to the individual views in the row -> as in, this is where it assigns the layout

    //need to define the adapter to describe the process of converting the Java object to a View (in the getView method).

    public View getView(final int position, View convertView, ViewGroup parent) {

        //inflates (renders) layout_for_listview_events
        // Check if an existing view is being reused, otherwise inflate the view


        //inflater for inflating xml, see below
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_for_listview_events, parent, false);


        final EventHolder holder = new EventHolder();



        holder.eventName = (TextView) rowView.findViewById(R.id.textview_name);
        holder.eventTime = (TextView) rowView.findViewById(R.id.textview_time);
        holder.eventLocation = (TextView) rowView.findViewById(R.id.textview_location);
        holder.chkBox = (CheckBox) rowView.findViewById(R.id.checkBox);

        holder.eventName.setText(eventList.get(position).getEventName());
        holder.eventTime.setText(eventList.get(position).getTime());
        holder.eventLocation.setText(eventList.get(position).getLocation());


        holder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    holder.chkBox.setChecked(true);
                    Toast.makeText(context, "This event has been saved to your events", Toast.LENGTH_SHORT).show();
                } else {
                    holder.chkBox.setChecked(false);
                    Toast.makeText(context, "This event has been removed from your events", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rowView;
    }
}