package com.ticketingsystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.http.LoginAsync;
import com.ticketingsystem.http.LoginCommand;
import com.ticketingsystem.models.UserLoginRequestModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Boolean exit = false;
    private String token_expiration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isUserRegistered = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserRegistered", false);
        Boolean isUserLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserLoggedIn", false);

        /*
        Boolean isExpired = false;
        try {
            isExpired = isTokenExpired();
        } catch (ParseException e) {
            System.out.println("++++++++++++++++ error 1: " + e);
            e.printStackTrace();
        }
        */

        if (!isUserRegistered) {
            goToActivity(RegisterActivity.class);
        }

        /* TODO - Method isExpired Still produce an exception*/
        else if (!isUserLoggedIn) {
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
        }
        else {
            goToActivity(HomeActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, getResources().getString(R.string.exit_message),
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

    /* This function should be used if token is expired */
    public void automaticLogin(String username, String password) {
        URI uri = null;
        try {
            uri = new URI(getResources().getString(R.string.login_url));
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

    private Boolean isTokenExpired() throws ParseException {
        String authorizationToken = this.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("token", "");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss GTM");

        try {
            token_expiration = new JSONObject(authorizationToken).getString(".expires");
        } catch (JSONException e) {
            System.out.println("++++++++++++++++ error 2: " + e);
            e.printStackTrace();
        }

        Calendar expires_on = token_expiration == "null" || token_expiration.isEmpty() ? null : toCalendar(sdf.parse(token_expiration));

        if (expires_on == null){
            return false;
        }
        else {
            return expires_on.before(Calendar.getInstance());
        }
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}