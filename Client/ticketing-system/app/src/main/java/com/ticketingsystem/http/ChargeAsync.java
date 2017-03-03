package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ticketingsystem.models.ChargeRequestModel;
import com.ticketingsystem.models.UserRegisterRequestModel;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChargeAsync extends AsyncTask<Void, Void, Boolean> {

    private ChargeCommand chargeCommand;
    private Context context;
    private ChargeRequestModel chargeRequestModel;
    private String authorizationToken;

    public ChargeAsync(Context context, String authorizationToken, ChargeRequestModel chargeRequestModel, ChargeCommand chargeCommand) {
        this.chargeCommand = chargeCommand;
        this.authorizationToken = authorizationToken;
        this.context = context;
        this.chargeRequestModel = chargeRequestModel;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Gson gson = new Gson();
        String requestBody = gson.toJson(chargeRequestModel);

        URL url = null;
        try {
            url = new URL("http://ticket-system-rest.apphb.com/api/users/charge");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("PUT");
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

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
        this.chargeCommand.execute(result);
        super.onPostExecute(result);
    }
}