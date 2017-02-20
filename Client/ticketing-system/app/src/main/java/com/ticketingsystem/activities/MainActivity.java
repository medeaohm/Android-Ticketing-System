package com.ticketingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.navigation.NavigationService;

public class MainActivity extends AppCompatActivity implements NavigationService{

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isUserRegistered = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserRegistered", true);
        Boolean isUserLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserLoggedIn", false);


        if (!isUserRegistered) {
            Intent intent_register = new Intent(MainActivity.this, RegisterActivity.class);
            intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            finish(); // destroy current activity..
            startActivity(intent_register);
            /*Toast.makeText(MainActivity.this, "User Not Registered", Toast.LENGTH_LONG)
                    .show();*/
        } else if (!isUserLoggedIn) {
            Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
            intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            finish(); // destroy current activity..
            startActivity(intent_login);
            Toast.makeText(MainActivity.this, "User Not Logged In", Toast.LENGTH_LONG).show();
        } else {
            Intent intent_issues = new Intent(MainActivity.this, HomeActivity.class);
            intent_issues.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent_issues.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            finish(); // destroy current activity..
            startActivity(intent_issues);
        }
    }

    @Override
    public void goToInternalActivity() {
        Intent intent = new Intent(this, InternalActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

}