package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ticketingsystem.models.UserLoginRequestModel;
import com.ticketingsystem.models.UserProfileModel;
import com.ticketingsystem.models.UserRegisterResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class GetProfileAsync extends AsyncTask<Void, Void, UserProfileModel> {
    private GetProfileCommand getProfileCommand;
    private Context context;
    private String authorizationToken;
    private URI uri;
    private UserProfileModel userProfileModel;

    public GetProfileAsync(Context context, String authorizationToken, URI uri, GetProfileCommand getProfileCommand) {
        this.getProfileCommand = getProfileCommand;
        this.context = context;
        this.authorizationToken = authorizationToken;
        this.uri = uri;
        this.userProfileModel = userProfileModel;
    }

    @Override
    protected UserProfileModel doInBackground(Void... params) {
        Gson gson = new Gson();
        URL url = null;
        try {
            url = new URL("http://ticket-system-rest.apphb.com/api/users/info");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        System.out.println("++++++++++++++++ auth: " + authorizationToken);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            System.out.println("++++++++++++++++ 1");
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("GET");
            System.out.println("++++++++++++++++ 2");
            //urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            System.out.println("++++++++++++++++ 3");
            urlConnection.setRequestProperty("Accept", "application/json");
            System.out.println("++++++++++++++++ 4");
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.authorizationToken);
            System.out.println("++++++++++++++++ 5");

            StringBuilder sb = new StringBuilder();
            System.out.println("++++++++++++++++ url: " + urlConnection.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            System.out.println("++++++++++++++++ br: " + br);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            String resultString = sb.toString();
            System.out.println("++++++++++++++++ sb: " + sb);
            UserProfileModel result = gson.fromJson(resultString, UserProfileModel.class);
            System.out.println("++++++++++++++++ result: " + result);

        return result;

    } catch (IOException e) {
            System.out.println("++++++++++++++++ e: " + e);
        e.printStackTrace();
    }

    return null;
    }

    @Override
    protected void onPostExecute(UserProfileModel userProfileModel) {
        this.getProfileCommand.execute(userProfileModel);
        super.onPostExecute(userProfileModel);
        System.out.println("++++++++++++++++ post ex: " + userProfileModel.UserName);
    }
}