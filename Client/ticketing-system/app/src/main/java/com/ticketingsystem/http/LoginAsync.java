package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.ticketingsystem.R;
import com.ticketingsystem.models.UserLoginRequestModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


public class LoginAsync extends AsyncTask<Void, Void, String> {
    private LoginCommand loginCommand;
    private Context context;
    private URI uri;
    private UserLoginRequestModel userLoginRequestModel;

    public LoginAsync(Context context, URI uri, UserLoginRequestModel userLoginRequestModel, LoginCommand loginCommand) {
        this.loginCommand = loginCommand;
        this.context = context;
        this.uri = uri;
        this.userLoginRequestModel = userLoginRequestModel;
    }

    @Override
    protected String doInBackground(Void... params) {

        URL url = null;
        try {
            url = new URL(context.getResources().getString(R.string.login_url));
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

            OutputStream os = null;
            os = urlConnection.getOutputStream();
            os.write(
                    ("grant_type=" + userLoginRequestModel.grant_type +
                            "&username=" + userLoginRequestModel.username +
                            "&password=" + userLoginRequestModel.password)
                    .getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            String result = sb.toString();
            return result;
        } catch (IOException e) {
            System.out.println("++++++++++++++++ error: " + e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        this.loginCommand.execute(s);
        super.onPostExecute(s);
    }
}