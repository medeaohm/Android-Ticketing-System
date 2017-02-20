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
import com.ticketingsystem.navigation.NavigationService;

public class LoginActivity extends Activity implements View.OnClickListener, NavigationService {

    private Boolean exit = false;

    private EditText email;
    private EditText password;
    private ProgressDialog connectionProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Logging in ...");

        this.email = (EditText) findViewById(R.id.login_email);
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
        connectionProgressDialog.show();
        HttpClient httpClient = new HttpClient(this, getResources().getString(R.string.server_url));
        httpClient.Login(this.email.getText().toString(), this.password.getText().toString(), (NavigationService) this);
    }

    @Override
    public void goToInternalActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}