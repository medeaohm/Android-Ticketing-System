package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.ticketingsystem.R;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BuyTicketAsync extends AsyncTask<Void, Void, Boolean> {

    private BuyTicketCommand buyTicketCommand;
    private Context context;
    private Integer hours;
    private String authorizationToken;

    public BuyTicketAsync(Context context, String authorizationToken, Integer hours, BuyTicketCommand buyTicketCommand) {
        this.buyTicketCommand = buyTicketCommand;
        this.authorizationToken = authorizationToken;
        this.context = context;
        this.hours = hours;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        String requestBody = "{hours: " + hours.toString() + "}";

        URL url = null;
        try {
            url = new URL(context.getResources().getString(R.string.buy_ticket_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.authorizationToken);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.write(requestBody.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            urlConnection.connect();
            System.out.println("++++++++++++++++ code : " + urlConnection.getResponseCode());

            if(urlConnection.getResponseCode() == 201) {
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        this.buyTicketCommand.execute(result);
        super.onPostExecute(result);
    }
}