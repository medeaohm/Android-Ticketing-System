package com.ticketingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ticketingsystem.R;
import com.ticketingsystem.adapters.ListItemAdapter;
import com.ticketingsystem.fragments.BuyTicketFragment;
import com.ticketingsystem.fragments.ChargeAccountFragment;
import com.ticketingsystem.fragments.MyProfileFragment;
import com.ticketingsystem.fragments.MyTicketsFragment;
import com.ticketingsystem.http.IMyTicket;
import com.ticketingsystem.models.MyTicketsListItemModel;

import java.util.ArrayList;
import java.util.List;

import in.myinnos.customimagetablayout.ChangeColorTab;

public class HomeActivity extends AppCompatActivity implements IMyTicket {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public List<MyTicketsListItemModel> myTicketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ChangeColorTab changeColorTab = (ChangeColorTab) findViewById(R.id.tabChangeColorTab);
        changeColorTab.setViewpager(mViewPager);

        myTicketList = new ArrayList<>();

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutButton:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setMyTicketData(List<MyTicketsListItemModel> models) {

        if(myTicketList.size() == 0) {
            myTicketList.clear();
            myTicketList.addAll(models);
            return;
        }

        for(int i = 0; i < models.size(); i++) {
            MyTicketsListItemModel model = models.get(i);
            if(collectionContainsObject(myTicketList, model) == false) {
                myTicketList.add(model);
            }
        }

        //System.out.println("++++++++++++++++ myTicketList home: " + myTicketList.size());
    }

    private void logout() {
        updateSharedPreferences();
        Intent intent_login = new Intent(this, LoginActivity.class);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish(); // destroy current activity..
        startActivity(intent_login);
    }

    private void updateSharedPreferences(){
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isUserRegistered", true).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isUserLoggedIn", false).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .remove("username").commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .remove("password").commit();
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BuyTicketFragment();
                case 1:
                    return new MyTicketsFragment();
                case 2:
                    return new ChargeAccountFragment();
                case 3:
                    return new MyProfileFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Buy Ticket";
                case 1:
                    return "My Tickets";
                case 2:
                    return "Charge Account";
                case 3:
                    return "My Profile";
            }
            return null;
        }
    }

    private boolean collectionContainsObject(List<MyTicketsListItemModel> collection, MyTicketsListItemModel obj) {
        for(int i = 0; i < collection.size(); i++) {
            MyTicketsListItemModel model = collection.get(i);
            if((model.getDuration().equals(obj.getDuration()))) {
                return true;
            }
        }

        return false;
    }
}
