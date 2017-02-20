package com.ticketingsystem.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ticketingsystem.R;
import com.ticketingsystem.navigation.NavigationService;

import java.util.ArrayList;
import java.util.List;

public class InternalActivity extends AppCompatActivity implements NavigationService {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal);


        List<Fragment> fragments = new ArrayList<Fragment>();

        bundle = new Bundle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.internalToolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }


    @Override
    public void goToInternalActivity() {

    }
}