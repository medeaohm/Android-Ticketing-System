package com.ticketingsystem.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.activities.HomeActivity;
import com.ticketingsystem.http.BuyTicketAsync;
import com.ticketingsystem.http.BuyTicketCommand;
import com.ticketingsystem.models.TokenModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

import static android.content.Context.MODE_PRIVATE;

public class BuyTicketFragment extends Fragment implements View.OnClickListener{

    private Spinner duration;
    private Integer hours;
    private TextView price;
    private Button buyTicketButton;

    private TokenModel token;

    private String[] durations = new String[]{
            "1 hour",
            "2 hour",
            "3 hour",
            "4 hour",
            "5 hour",
            "1 day",
            "2 days",
            "3 days",
            "1 week",
            "2 weeks",
            "3 weeks",
            "1 month",
            "3 months",
    };

    private ProgressDialog progressDialog;

    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_buy_ticket, container, false);

        String authorizationToken = this.getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("token", "");
        token = gson.fromJson(authorizationToken, TokenModel.class);

        this.price = (TextView) rootView.findViewById(R.id.ticket_buy_price);

        this.duration = (Spinner) rootView.findViewById(R.id.ticket_buy_duration);

        ArrayAdapter<String> adapterDurations = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, durations);
        duration.setAdapter(adapterDurations);
        duration.setOnItemSelectedListener(onItemSelectedListener);

        buyTicketButton = (Button) rootView.findViewById(R.id.ticket_buy_btn);
        buyTicketButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_buy_btn : {
                this.buy();
                break;
            }
        }
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener =
            new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String calculated_price = "0.00 lv";
                    switch (position) {
                        case 0:
                            calculated_price = "1.60 lv";
                            hours = 1;
                            break;
                        case 1:
                            calculated_price = "1.70 lv";
                            hours = 2;
                            break;
                        case 2:
                            calculated_price = "1.80 lv";
                            hours = 3;
                            break;
                        case 3:
                            calculated_price = "1.90 lv";
                            hours = 4;
                            break;
                        case 4:
                            calculated_price = "2.00 lv";
                            hours = 5;
                            break;
                        case 5:
                            calculated_price = "3.90 lv";
                            hours = 24;
                            break;
                        case 6:
                            calculated_price = "6.30 lv";
                            hours = 48;
                            break;
                        case 7:
                            calculated_price = "8.7 lv";
                            hours = 72;
                            break;
                        case 8:
                            calculated_price = "18.30 lv";
                            hours = 168;
                            break;
                        case 9:
                            calculated_price = "24.60 lv";
                            hours = 336;
                            break;
                        case 10:
                            calculated_price = "36.30 lv";
                            hours = 504;
                            break;
                        case 11:
                            calculated_price = "51.45 lv";
                            hours = 720;
                            break;
                        case 12:
                            calculated_price = "155.60 lv";
                            hours = 2208;
                            break;
                    }
                    price.setText(calculated_price);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            };

    private void buy() {

        Integer request_hours = hours;

        BuyTicketAsync buyAsyncTask = new BuyTicketAsync(getActivity(), token.access_token, request_hours, new BuyTicketCommand() {
            @Override
            public void execute(Boolean result) {
                progressDialog.dismiss();

                if (result) {
                    AlertFactory.createInformationAlertDialog(getActivity(), getResources().getString(R.string.buy_ticket_success), "Success", new OkCommand() {
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
        buyAsyncTask.execute();
    }

    private void goToHomeActivity () {
        Intent intent_home = new Intent(this.getContext(), HomeActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        getActivity().finish(); // destroy current activity..
        startActivity(intent_home);
    }
}

