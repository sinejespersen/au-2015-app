package itsmap.SV;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

public class FridayFragment extends Fragment {

    ArrayList<AnEvent> eventList;


    public FridayFragment() {
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
        displayLists(listView);
        return r;
    }

    private void displayLists(ListView listView) {
        eventList = new ArrayList<AnEvent>();
        eventList.add(new AnEvent("Registration", "08:00", "Nygaard"));
        eventList.add(new AnEvent("Papers: Critical Subjects and Subjectives", "09:00-10:30", "PBA"));
        eventList.add(new AnEvent("Coffee and tea break", "10:30-11:00", "Nygaard"));
        eventList.add(new AnEvent("Panel: Reconceptualizing Critical Utopias", "11:00-12:30", "PBA"));
        eventList.add(new AnEvent("Lunch", "12:30-14:00", "Incuba"));
        eventList.add(new AnEvent("Papers: Interpreting Infrastructure", "14:00-15:00", "PBA"));
        eventList.add(new AnEvent("Coffee and tea break", "15:00-15:30", "Nygaard"));
        eventList.add(new AnEvent("Towards 2025", "15:30-17:00", "PBA"));

        EventAdapter eventAdapter = new EventAdapter(this.getContext(),eventList);
        listView.setAdapter(eventAdapter);
    }
}