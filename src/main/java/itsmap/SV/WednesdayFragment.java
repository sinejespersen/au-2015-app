package itsmap.SV;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

/**
 * Created by sinejespersen on 29/09/15.
 */

public class WednesdayFragment extends Fragment {

    public WednesdayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ArrayList<AnEvent> eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View r = inflater.inflate(R.layout.layout_fragments, container, false);
        ListView listView = (ListView)r.findViewById(R.id.Listview);
        displayLists(listView);
        return r;

    }

    private void displayLists(ListView listView) {
        eventList = new ArrayList<AnEvent>();
        eventList.add(new AnEvent("Registration", "08:00", "Nygaard"));
        eventList.add(new AnEvent("Opening Keynote: Telekommunisten The Political Economy of Communication Networks", "09:00-10:30", "PBA"));
        eventList.add(new AnEvent("Coffee and tea break", "10:30-11:00", "Nygaard"));
        eventList.add(new AnEvent("Papers: Participatory Trajectories", "11:00-12:30", "PBA"));
        eventList.add(new AnEvent("Lunch", "12:30-14:00", "Incuba"));
        eventList.add(new AnEvent("Papers: Keeping Secrets", "14:00-15:00", "PBA"));
        eventList.add(new AnEvent("Coffee and tea break", "15:00-15:30", "Nygaard"));
        eventList.add(new AnEvent("Critical Conversation: A conversation with Frieder Nake about conversations with computers", "15:30-17:00", "PBA"));
        eventList.add(new AnEvent("Demos, visits, performance, food", "17:00", "Nygaard"));


        EventAdapter eventAdapter = new EventAdapter(this.getContext(),eventList);
        listView.setAdapter(eventAdapter);
    }
}