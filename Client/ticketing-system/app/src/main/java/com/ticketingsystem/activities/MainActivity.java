package com.ticketingsystem.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.http.LoginAsync;
import com.ticketingsystem.http.LoginCommand;
import com.ticketingsystem.models.UserLoginRequestModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isUserRegistered = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserRegistered", false);
        Boolean isUserLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserLoggedIn", false);


        if (!isUserRegistered) {
            goToActivity(RegisterActivity.class);
        } else if (!isUserLoggedIn) {
            String sp_username = "";
            String sp_password = "";

            if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).contains("username") &&
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).contains("password")) {
                sp_username = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("username", "");
                sp_password = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("password", "");
            }

            if (sp_username != "" && sp_password != "") {
                automaticLogin(sp_username, sp_password);
                goToActivity(HomeActivity.class);
            } else {
                goToActivity(LoginActivity.class);
            }
        } else {
            goToActivity(HomeActivity.class);
        }
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

    private void goToActivity(final Class<? extends Activity> ActivityToOpen){
        Intent intent = new Intent(MainActivity.this, ActivityToOpen);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish(); // destroy current activity..
        startActivity(intent);
    }

    public void automaticLogin(String username, String password) {
        URI uri = null;
        try {
            uri = new URI("http://ticket-system-rest.apphb.com/token");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        UserLoginRequestModel user = new UserLoginRequestModel();
        user.username = username;
        user.password = password;
        user.grant_type = "password";

        LoginAsync loginAsyncTask = new LoginAsync(MainActivity.this, uri, user, new LoginCommand() {
            @Override
            public void execute(String token) {

            }
        });

        loginAsyncTask.execute();
    }
}