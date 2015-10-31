package itsmap.SV;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

public class MyTasksFragment extends Fragment  {

    ArrayList<ATask> taskList;

    public MyTasksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View r = inflater.inflate(R.layout.layout_fragments, container, false);
        ListView listView = (ListView)r.findViewById(R.id.Listview);
        displayTasks(listView);
        return r;
    }

    private void displayTasks(ListView listView) {
        taskList = new ArrayList<ATask>();
        taskList.add(new ATask("09:00", "Mona, Rasmus, Thomas","Fix tea and coffee for the 10:00-break"));
        taskList.add(new ATask("12:00", "Anders, Rasmus","Guide people to Incuba for lunch"));
        taskList.add(new ATask("14:00", "Mona, Rasmus, Thomas","Fix tea and coffee for the 15:00-break"));

        TaskAdapter taskadapter = new TaskAdapter(this.getContext(),taskList);
        listView.setAdapter(taskadapter);
    }
}