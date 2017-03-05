package com.ticketingsystem.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.activities.HomeActivity;
import com.ticketingsystem.http.ChargeAsync;
import com.ticketingsystem.http.ChargeCommand;
import com.ticketingsystem.models.ChargeRequestModel;
import com.ticketingsystem.models.TokenModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ChargeAccountFragment extends Fragment implements View.OnClickListener{

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
                if (!isNumberValid(cardNumber, 16)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_card_number), Toast.LENGTH_SHORT).show();
                }
                else if (!isCardHolderNameValid()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_card_holder), Toast.LENGTH_SHORT).show();
                }
                else if (!isExpireDateValid()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_expiration_date), Toast.LENGTH_SHORT).show();
                }
                else if (!isNumberValid(securityCode, 3)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_security_number), Toast.LENGTH_SHORT).show();
                }
                else if (!isAmountValid()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                }
                else {
                    this.charge();
                }
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
                    AlertFactory.createInformationAlertDialog(getActivity(),getResources().getString(R.string.charge_success), "Success", new OkCommand() {
                        @Override
                        public void execute() {
                            goToHomeActivity();
                        }
                    }).show();
                } else {
                    AlertFactory.createInformationAlertDialog(getActivity(), getResources().getString(R.string.default_error), "Error", null).show();
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.default_wait));
        progressDialog.show();
        chargeAsyncTask.execute();
    }

    private void goToHomeActivity () {
        Intent intent_home = new Intent(this.getContext(), HomeActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        getActivity().finish(); // destroy current activity..
        startActivity(intent_home);
    }

    private Boolean isNumberValid(EditText et, int len){
        int actualLen = et.getText().toString().length();
        Boolean containsOnlyNumbers = et.getText().toString().matches("[0-9]+");

        if(actualLen != len || !containsOnlyNumbers) {
            return false;
        }
        else {
            return  true;
        }
    }

    private Boolean isCardHolderNameValid(){
        String name = cardHolderNames.getText().toString();
        CharSequence numbers = "0123456789";

        if(name.length() == 0 || name.contains(numbers)) {
            return false;
        }
        else {
            return  true;
        }
    }

    private Boolean isExpireDateValid(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int expYear = Integer.parseInt(expireYear.getSelectedItem().toString());
        int expMonth = Integer.parseInt(expireMonth.getSelectedItem().toString());

        if(expYear < currentYear || ((expYear == currentYear) && (expMonth < currentMonth))) {
            return false;
        }
        else {
            return  true;
        }
    }

    private Boolean isAmountValid(){
        int amountLen = amount.getText().toString().length();
        Double amountNum = null;
        if (amountLen > 0) {
            amountNum = Double.parseDouble(amount.getText().toString());
        }

        if(amountLen == 0 || amountNum <= 0) {
            return false;
        }
        else {
            return  true;
        }
    }
}
