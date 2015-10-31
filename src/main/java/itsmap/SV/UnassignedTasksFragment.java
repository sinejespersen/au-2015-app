package itsmap.SV;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

public class UnassignedTasksFragment extends Fragment {


    ArrayList<ATask> taskList;

    public UnassignedTasksFragment() {
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

        taskList.add(new ATask("11:00", "Mona, Sine","Need one person more to set up lunch"));
        taskList.add(new ATask("14:00", "Sandra","Need one person more to hold the microphone"));

        TaskAdapter taskadapter = new TaskAdapter(this.getContext(),taskList);
        listView.setAdapter(taskadapter);
    }
}