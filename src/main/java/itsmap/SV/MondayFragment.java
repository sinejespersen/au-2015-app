package itsmap.SV;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp11.SV.R;

public class MondayFragment extends Fragment  {

    ArrayList<AnEvent> eventList;

    public MondayFragment() {
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
        eventList.add(new AnEvent("W3: Shifting Borderlands of Technoscience: Tracing Trajectories of Critical Praxis", "09:00-17:00", "Nygaard 5335-091"));
        eventList.add(new AnEvent("W4: Unfolding Participation. What do we mean by participation conceptually and in practice", "09:00-17:00", "Nygaard 5335-192"));
        eventList.add(new AnEvent("W5: Charting the Next Decade for Value Sensitive Design", "09:00-17:00", "Nygaard 5335-184"));
        eventList.add(new AnEvent("W6: Critical and participatory development of people centered smart learning ecosystems and territories", "09:00-17:00", "Nygaard 5335-297"));
        eventList.add(new AnEvent("W7: Making \"World Machines\": Discourse, Design and Global Technologies for Greater-than-self Issues", "09:00-17:00", "Nygaard 5335-295"));
        eventList.add(new AnEvent("W8: Residents’ Democratic engagement in public housing and urban areas", "09:00-17:00", "Nygaard 5335-395"));
        eventList.add(new AnEvent("W14: Embodying Embodied Design Research Techniques", "09:00-17:00", "Nygaard 5335-327"));

        EventAdapter eventAdapter = new EventAdapter(this.getContext(),eventList);
        listView.setAdapter(eventAdapter);
    }
}