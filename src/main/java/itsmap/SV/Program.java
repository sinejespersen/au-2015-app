package itsmap.SV;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import dk.iha.itsmap.e15.grp11.SV.R;

public class Program extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        //Fragmentmanager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MondayFragment(), getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getString(R.string.friday));

        viewPager.setAdapter(adapter);
    }

}