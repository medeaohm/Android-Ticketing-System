package com.ticketingsystem.http;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.models.UserRegisterRequestModel;
import com.ticketingsystem.models.UserRegisterResponseModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class RegisterAsync extends AsyncTask<Void, Void, UserRegisterResponseModel> {

    private RegisterCommand registerCommand;
    private Context context;
    private UserRegisterRequestModel userRegisterRequestModel;

    public RegisterAsync(Context context, UserRegisterRequestModel userRegisterRequestModel, RegisterCommand registerCommand) {
        this.registerCommand = registerCommand;
        this.context = context;
        this.userRegisterRequestModel = userRegisterRequestModel;
    }

    @Override
    protected UserRegisterResponseModel doInBackground(Void... params) {

        Gson gson = new Gson();
        String requestBody = gson.toJson(this.userRegisterRequestModel);

        URL url = null;
        try {
            url = new URL("http://ticket-system-rest.apphb.com/api/account/register");
        } catch (MalformedURLException e) {
            System.out.println("++++++++++++++++ error: " + e);
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
                    (
                            "firstName=" + userRegisterRequestModel.firstName +
                            "&lastName=" + userRegisterRequestModel.lastName +
                            "&username=" + userRegisterRequestModel.userName +
                            "&email=" + userRegisterRequestModel.email +
                            "&password=" + userRegisterRequestModel.password +
                            "&confirmPassword=" + userRegisterRequestModel.confirmPassword
                    )
                    .getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            System.out.println("++++++++++++++++ os: " + os);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            System.out.println("++++++++++++++++ os: " + os);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }


            String resultString = sb.toString();

            UserRegisterResponseModel result = gson.fromJson(resultString, UserRegisterResponseModel.class);

            return result;
        } catch (IOException e) {
            System.out.println("++++++++++++++++ error: " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(UserRegisterResponseModel userRegisterResponseModel) {
        this.registerCommand.execute(userRegisterResponseModel);
        super.onPostExecute(userRegisterResponseModel);
    }
}