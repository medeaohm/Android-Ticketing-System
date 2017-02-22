package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ticketingsystem.navigation.NavigationService;
import com.ticketingsystem.storage.TokensDbHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class LoginAsync extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private NavigationService navigationService;
    private String loginUrl;

    public LoginAsync(Context context, NavigationService navigationService, String loginUrl) {
        this.context = context;
        this.navigationService = navigationService;
        this.loginUrl = loginUrl;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            String urlParameters = String.format("Username=%s&Password=%s&grant_type=password", params[0], params[1]);
            byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
            int postDataLength = postData.length;

            URL url = new URL(this.loginUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));

            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.write(postData);
            outputStream.flush();
            outputStream.close();
            urlConnection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 512);
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("LOGIN ++++++++++++++++ ");
                JSONObject json = new JSONObject(response.toString());
                TokensDbHandler db = new TokensDbHandler(this.context, null);
                db.addToken(json.getString("access_token"), "login");
                return true;
            }

            return false;


        } catch (Exception e) {
            System.out.println("LOGIN ERROR ==================== " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result.booleanValue() == true) {
            Toast.makeText(this.context, "Successful login", Toast.LENGTH_SHORT).show();
            this.navigationService.goToInternalActivity();
        } else {
            Toast.makeText(this.context, "Failed login",  Toast.LENGTH_SHORT).show();
        }
    }
}
