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
    public void goToInternalActivity() {

    }

    private void logout() {
        Intent intent_login = new Intent(this, LoginActivity.class);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish(); // destroy current activity..
        startActivity(intent_login);
    }
}