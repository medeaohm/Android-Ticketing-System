package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.models.UserRegisterRequestModel;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterAsync extends AsyncTask<Void, Void, Boolean> {

    private RegisterCommand registerCommand;
    private Context context;
    private UserRegisterRequestModel userRegisterRequestModel;

    public RegisterAsync(Context context, UserRegisterRequestModel userRegisterRequestModel, RegisterCommand registerCommand) {
        this.registerCommand = registerCommand;
        this.context = context;
        this.userRegisterRequestModel = userRegisterRequestModel;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Gson gson = new Gson();
        String requestBody = gson.toJson(userRegisterRequestModel);

        URL url = null;
        try {
            url = new URL(context.getResources().getString(R.string.register_url));
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
        this.registerCommand.execute(result);
        super.onPostExecute(result);
    }
}