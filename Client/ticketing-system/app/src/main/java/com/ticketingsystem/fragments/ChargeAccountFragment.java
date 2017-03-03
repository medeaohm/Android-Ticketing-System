package com.ticketingsystem.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.activities.HomeActivity;
import com.ticketingsystem.activities.LoginActivity;
import com.ticketingsystem.http.ChargeAsync;
import com.ticketingsystem.http.ChargeCommand;
import com.ticketingsystem.http.RegisterAsync;
import com.ticketingsystem.http.RegisterCommand;
import com.ticketingsystem.models.ChargeRequestModel;
import com.ticketingsystem.models.TokenModel;
import com.ticketingsystem.models.UserRegisterRequestModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

import static android.content.Context.MODE_PRIVATE;

public class ChargeAccountFragment extends Fragment implements View.OnClickListener{

    private Boolean exit = false;

    private EditText cardNumber;
    private EditText securityCode;
    private Spinner expireMonth;
    private Spinner expireYear;
    private EditText cardHolderNames;
    private EditText amount;
    private Button chargeButton;
    private TokenModel token;

    private ProgressDialog progressDialog;

    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_charge_account, container, false);

        String authorizationToken = this.getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("token", "");
        token = gson.fromJson(authorizationToken, TokenModel.class);

        this.cardNumber = (EditText) rootView.findViewById(R.id.charge_card_num);
        this.securityCode = (EditText) rootView.findViewById(R.id.charge_ccv);
        this.cardHolderNames = (EditText) rootView.findViewById(R.id.charge_name);
        this.amount = (EditText) rootView.findViewById(R.id.charge_amount);
        this.cardNumber = (EditText) rootView.findViewById(R.id.charge_card_num);

        this.expireMonth = (Spinner) rootView.findViewById(R.id.charge_expiration_month);
        String[] months = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, months);
        expireMonth.setAdapter(adapterMonths);

        this.expireYear = (Spinner) rootView.findViewById(R.id.charge_expiration_year);
        String[] years = new String[]{"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028"};
        ArrayAdapter<String> adapterYears = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, years);
        expireYear.setAdapter(adapterYears);

        chargeButton = (Button) rootView.findViewById(R.id.charge_btn);
        chargeButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.charge_btn : {
                this.charge();
                break;
            }
        }
    }

    private void charge() {

        ChargeRequestModel request = new ChargeRequestModel();
        request.CardNumber =  cardNumber.getText().toString();
        request.CardHolderNames =  cardHolderNames.getText().toString();
        request.CardType =  "Visa";
        request.SecurityCode = securityCode.getText().toString();
        request.ExpireMonth = Integer.parseInt(expireMonth.getSelectedItem().toString());
        request.ExpireYear = Integer.parseInt(expireYear.getSelectedItem().toString());
        request.Amount = Double.parseDouble(amount.getText().toString());


        ChargeAsync chargeAsyncTask = new ChargeAsync(getActivity(), token.access_token, request, new ChargeCommand() {
            @Override
            public void execute(Boolean result) {
                progressDialog.dismiss();

                if (result) {
                    AlertFactory.createInformationAlertDialog(getActivity(), "Account Charged successfully.", "Success", new OkCommand() {
                        @Override
                        public void execute() {
                            goToHomeActivity();
                        }
                    }).show();
                } else {
                    AlertFactory.createInformationAlertDialog(getActivity(), "An error occurred.", "Error", null).show();
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        chargeAsyncTask.execute();
    }

    private void updateChargeButton() {
        if (this.cardHolderNames.getText().length() > 0 && this.cardNumber.getText().length() > 0) {
            this.chargeButton.setEnabled(true);
        } else {
            this.chargeButton.setEnabled(false);
        }
    }

    private void goToHomeActivity () {
        Intent intent_home = new Intent(this.getContext(), HomeActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        getActivity().finish(); // destroy current activity..
        startActivity(intent_home);
    }
}
