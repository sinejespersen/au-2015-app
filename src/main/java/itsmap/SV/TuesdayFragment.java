package itsmap.SV;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

public class TuesdayFragment extends Fragment {

    public TuesdayFragment() {
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
        eventList.add(new AnEvent("W9: Live Coding Alternatives", "09:00-17:00", "Nygaard 5335-297"));
        eventList.add(new AnEvent("W10: Researching for Change in a Globalising Asymmetric World", "09:00-17:00", "Nygaard 5335-091"));
        eventList.add(new AnEvent("W11: \"I’ve had it!\" Group therapy for interdisciplinary researchers", "09:00-17:00", "Nygaard 5335-184"));
        eventList.add(new AnEvent("W12: The Future of Making: Where Industrial and Personal Fabrication Meet", "09:00-17:00", "Nygaard 5335-192"));
        eventList.add(new AnEvent("W13: Inviting Participation through IoT: Experiments and Performances in Public Spaces", "09:00-17:00", "Nygaard 5335-295"));
        eventList.add(new AnEvent("W16: Multi-Lifespan Information System Design", "09:00-17:00", "Nygaard 5335-298"));
        eventList.add(new AnEvent("W17: Criticism for computational alternatives", "09:00-17:00", "Nygaard 5335-395"));

        EventAdapter eventAdapter = new EventAdapter(this.getContext(),eventList);
        listView.setAdapter(eventAdapter);
    }
}