package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.models.UserProfileModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    }

    @Override
    protected UserProfileModel doInBackground(Void... params) {
        Gson gson = new Gson();
        URL url = null;
        try {
            url = new URL(context.getResources().getString(R.string.current_user_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.authorizationToken);

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            String resultString = sb.toString();
            UserProfileModel result = gson.fromJson(resultString, UserProfileModel.class);

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
    }
}