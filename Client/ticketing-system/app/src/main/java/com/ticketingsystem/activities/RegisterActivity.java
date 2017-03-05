package com.ticketingsystem.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.http.RegisterAsync;
import com.ticketingsystem.http.RegisterCommand;
import com.ticketingsystem.models.UserRegisterRequestModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

public class RegisterActivity  extends Activity implements View.OnClickListener {

    private Boolean exit = false;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button registerButton;

    private ProgressDialog progressDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = this;

        setContentView(R.layout.activity_register);

        MyTextWatcher textWatcher = new MyTextWatcher();

        this.firstName = (EditText) findViewById(R.id.register_first_name);
        this.firstName.addTextChangedListener(textWatcher);

        this.lastName = (EditText) findViewById(R.id.register_last_name);
        this.lastName.addTextChangedListener(textWatcher);

        this.email = (EditText) findViewById(R.id.register_email);
        this.email.addTextChangedListener(textWatcher);

        this.username = (EditText) findViewById(R.id.register_username);
        this.username.addTextChangedListener(textWatcher);

        this.password = (EditText) findViewById(R.id.register_password);
        this.password.addTextChangedListener(textWatcher);

        this.registerButton = (Button) findViewById(R.id.btn_register);
        this.registerButton.setOnClickListener(this);

        findViewById(R.id.register_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register : {
                this.onRegisterClick();
                break;
            }
            case R.id.register_login : {
                goToLoginActivity();
                break;
            }
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

    private void onRegisterClick() {

        UserRegisterRequestModel user = new UserRegisterRequestModel();
        user.firstName =  firstName.getText().toString();
        user.lastName =  lastName.getText().toString();
        user.email =  email.getText().toString();
        user.userName = username.getText().toString();
        user.password = password.getText().toString();
        user.confirmPassword =  password.getText().toString();

        RegisterAsync registerAsyncTask = new RegisterAsync(activity, user, new RegisterCommand() {
            @Override
            public void execute(Boolean result) {
                progressDialog.dismiss();

                if (result) {
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putBoolean("isUserRegistered", true).commit();

                    AlertFactory.createInformationAlertDialog(activity, getResources().getString(R.string.register_success), "Success", new OkCommand() {
                        @Override
                        public void execute() {
                            goToLoginActivity();
                        }
                    }).show();
                } else {
                    AlertFactory.createInformationAlertDialog(activity, getResources().getString(R.string.register_error), "Error", null).show();
                }
            }
        });

        progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.register_waiting));
        progressDialog.show();
        registerAsyncTask.execute();
    }

    private void updateRegisterButton() {
        if (this.email.getText().length() > 0 && this.password.getText().length() > 0) {
            this.registerButton.setEnabled(true);
        } else {
            this.registerButton.setEnabled(false);
        }
    }

    private void goToLoginActivity () {
        Intent intent_login = new Intent(this, LoginActivity.class);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish(); // destroy current activity..
        startActivity(intent_login);
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateRegisterButton();
        }
    }
}
