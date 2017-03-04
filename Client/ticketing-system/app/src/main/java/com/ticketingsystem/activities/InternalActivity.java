package com.ticketingsystem.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;


import com.ticketingsystem.R;
import com.ticketingsystem.adapters.ListItemAdapter;
import com.ticketingsystem.http.IMyTicket;
import com.ticketingsystem.models.MyTicketsListItemModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class InternalActivity extends AppCompatActivity implements IMyTicket {
    ViewPager viewPager;
    List<MyTicketsListItemModel> myTicketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        myTicketList = new ArrayList<>();
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
    }

    ListItemAdapter searchResultAdapter = null;
}