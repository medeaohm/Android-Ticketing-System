package com.ticketingsystem.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.http.HttpClient;
import com.ticketingsystem.http.LoginAsync;
import com.ticketingsystem.http.LoginCommand;
import com.ticketingsystem.models.UserLoginRequestModel;
import com.ticketingsystem.navigation.NavigationService;

import java.net.URI;
import java.net.URISyntaxException;

public class LoginActivity extends Activity implements View.OnClickListener, NavigationService {

    private Boolean exit = false;

    private EditText username;
    private EditText password;
    private String grant_type = "password";
    private ProgressDialog connectionProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Logging in ...");

        this.username = (EditText) findViewById(R.id.login_username);
        this.password = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.login_register).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login : {
                this.onLogin();
                break;
            }
            case R.id.login_register :  {
                Intent intent_register = new Intent(this, RegisterActivity.class);
                intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                finish(); // destroy current activity..
                startActivity(intent_register);
                break;
            }
        }
    }

    public static void showAlertMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(R.string.app_name);
        alertDialogBuilder.setPositiveButton("OK", listener);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }

    public void onLogin() {
        URI uri = null;
        try {
            uri = new URI("http://ticket-system-rest.apphb.com/token");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        UserLoginRequestModel user = new UserLoginRequestModel();
        user.username = username.getText().toString();
        user.password = password.getText().toString();
        user.grant_type = grant_type;

        LoginAsync loginAsyncTask = new LoginAsync(LoginActivity.this, uri, user, new LoginCommand() {
            @Override
            public void execute(String token) {
                connectionProgressDialog.dismiss();
                goToInternalActivity();
                /*
                if (token != null) {
                    SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.TOKEN_SHARED_PREFERENCE_KEY, token);

                    editor.commit();

                    AlertDialogFactory.createInformationAlertDialog(LoginActivity.this, "Login successful.", "Success", new OkCommand() {
                        @Override
                        public void execute() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }).show();
                } else {
                    AlertDialogFactory.createInformationAlertDialog(LoginActivity.this, "Login failed.", "Error", null).show();
                }
                */
            }
        });

        connectionProgressDialog = new ProgressDialog(LoginActivity.this);
        connectionProgressDialog.setIndeterminate(true);
        connectionProgressDialog.setMessage("Logging in...");
        connectionProgressDialog.show();
        loginAsyncTask.execute();
    }

    @Override
    public void goToInternalActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}