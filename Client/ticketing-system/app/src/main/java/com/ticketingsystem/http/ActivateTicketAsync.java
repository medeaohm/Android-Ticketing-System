package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.ticketingsystem.R;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActivateTicketAsync extends AsyncTask<Void, Void, Boolean> {

    private ActivateTicketCommand activateTicketCommand;
    private Context context;
    private String ticketId;
    private String authorizationToken;

    public ActivateTicketAsync(Context context, String authorizationToken, String ticketId, ActivateTicketCommand activateTicketCommand) {
        this.activateTicketCommand = activateTicketCommand;
        this.authorizationToken = authorizationToken;
        this.context = context;
        this.ticketId = ticketId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        String requestBody = "{id: " + "\"" + ticketId + "\"}";
        System.out.println("++++++++++++++++ requestBody : " + requestBody);

        URL url = null;
        try {
            url = new URL(context.getResources().getString(R.string.activate_ticket_url));
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

            if(urlConnection.getResponseCode() == 200 ) {
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
        this.activateTicketCommand.execute(result);
        super.onPostExecute(result);
    }
}