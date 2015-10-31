package itsmap.SV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

public class TaskAdapter extends ArrayAdapter<ATask> {

    private Context context;
    private ArrayList<ATask> taskList;


    public TaskAdapter(Context context, ArrayList<ATask> eventList) {
        super(context, -1, eventList);
        this.context = context;
        this.taskList = eventList;
    }

    private static class TaskHolder {
        public TextView taskName;
        public TextView taskTime;
        public TextView taskCoworkers;
    }

    @Override
    //The adapter inflates the layout for each row in its getView() method and assigns
    // the data to the individual views in the row -> as in, this is where it assigns the layout

    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.layout_for_listview, parent, false);

        TaskHolder holder = new TaskHolder();
        holder.taskName = (TextView) rowView.findViewById(R.id.textview_description);
        holder.taskTime = (TextView) rowView.findViewById(R.id.textview_time);
        holder.taskCoworkers = (TextView) rowView.findViewById(R.id.textview_coworker);

        holder.taskName.setText(taskList.get(position).getEventDes());
        holder.taskTime.setText(taskList.get(position).getTime());
        holder.taskCoworkers.setText(taskList.get(position).getCoworker());

        return rowView;
    }
}