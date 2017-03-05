package com.ticketingsystem.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.http.LoginAsync;
import com.ticketingsystem.http.LoginCommand;
import com.ticketingsystem.models.UserLoginRequestModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

import java.net.URI;
import java.net.URISyntaxException;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Boolean exit = false;

    private EditText username;
    private EditText password;
    private String grant_type = "password";
    private String token = "";
    private ProgressDialog connectionProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.username = (EditText) findViewById(R.id.login_username);
        this.password = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.login_register).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login : {
                this.onLogin();
                break;
            }
            case R.id.login_register :  {
                goToActivity(RegisterActivity.class);
                break;
            }
        }
    }

    public void onLogin() {
        URI uri = null;
        try {
            uri = new URI(getResources().getString(R.string.login_url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        UserLoginRequestModel user = new UserLoginRequestModel();
        user.username = username.getText().toString();
        user.password = password.getText().toString();
        user.grant_type = grant_type;

        LoginAsync loginAsyncTask = new LoginAsync(LoginActivity.this, uri, user, new LoginCommand() {
            @Override
            public void execute(String access_token) {
                token = access_token;
                System.out.println("++++++++++++++++ Token: " + access_token);
                connectionProgressDialog.dismiss();

                if (access_token != null) {
                    updateSharedPreferences();
                    AlertFactory.createInformationAlertDialog(LoginActivity.this, getResources().getString(R.string.login_success), "Success", new OkCommand() {
                        @Override
                        public void execute() {
                            goToActivity(MainActivity.class);
                        }
                    }).show();
                } else {
                    AlertFactory.createInformationAlertDialog(LoginActivity.this, getResources().getString(R.string.login_error), "Error", null).show();
                }
            }
        });

        connectionProgressDialog = new ProgressDialog(LoginActivity.this);
        connectionProgressDialog.setIndeterminate(true);
        connectionProgressDialog.setMessage(getResources().getString(R.string.login_waiting));
        connectionProgressDialog.show();
        loginAsyncTask.execute();
    }

    private void updateSharedPreferences(){
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isUserRegistered", true).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isUserLoggedIn", true).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putString("username", username.getText().toString()).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putString("password", password.getText().toString()).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putString("token", token).commit();
    }

    private void goToActivity(final Class<? extends Activity> ActivityToOpen){
        Intent intent = new Intent(LoginActivity.this, ActivityToOpen);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish(); // destroy current activity..
        startActivity(intent);
    }
}