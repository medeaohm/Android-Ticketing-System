package com.ticketingsystem.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.ticketingsystem.activities.HomeActivity;
import com.ticketingsystem.adapters.ListItemAdapter;
import com.ticketingsystem.models.MyTicketsListItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoadMyTicketsAsync extends AsyncTask<String, Void, JSONArray> {
    private String myIssuesUrl;
    private Context context;
    private IMyTicket myTicket;
    private String authorizationToken;
    private ListItemAdapter adapter;

    public LoadMyTicketsAsync(Context context, IMyTicket myTicket, String authorizationToken,
                             String myIssuesUrl, ListItemAdapter adapter) {
        this.myIssuesUrl = myIssuesUrl;
        this.context = context;
        this.myTicket = myTicket;
        this.authorizationToken = authorizationToken;
        this.adapter = adapter;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray listOfTickets = null;
        HttpURLConnection urlConnection = null;

        try{
            URL url = new URL(myIssuesUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.authorizationToken);
            urlConnection.connect();

        }  catch (Exception e){
            System.out.println("++++++++++++++++ e1 : " + e);
        }
        StringBuilder response = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 512);
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException ex) {
            System.out.println("++++++++++++++++ e2 : " + ex);
        }

        try {
            listOfTickets = new JSONArray(response.toString());
        } catch (JSONException e) {
            System.out.println("++++++++++++++++ e3 : " + e);
        }
        System.out.println("++++++++++++++++ listOfTickets : " + listOfTickets);
        return listOfTickets;
    }


    @Override
    protected void onPostExecute(JSONArray listOfTickets) {
        List<MyTicketsListItemModel> ticketReadyForListing = new ArrayList<MyTicketsListItemModel>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for(int i = 0; i < listOfTickets.length(); i++) {
            try {
                JSONObject ticket = listOfTickets.getJSONObject(i);
                byte[] byteArray =  Base64.decode(ticket.getString("QRCode"), Base64.DEFAULT) ;
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                Date boughtAt = sdf.parse(ticket.getString("BoughtAt"));
                Date dateActivated = ticket.getString("DateActivated") == "null" ? null : sdf.parse(ticket.getString("DateActivated"));
                Calendar expiresOn = ticket.getString("ExpiresOn") == "null" ? null : toCalendar(sdf.parse(ticket.getString("ExpiresOn")));

                MyTicketsListItemModel ticketForListing =
                        new MyTicketsListItemModel(
                                ticket.getString("Id"),
                                boughtAt,
                                ticket.getDouble("Cost"),
                                ticket.getBoolean("Expired"),
                                ticket.getBoolean("Activated"),
                                dateActivated,
                                expiresOn,
                                ticket.getInt("Duration"),
                                bmp
                        );

                ticketReadyForListing.add(ticketForListing);
                System.out.println("++++++++++++++++ ticketReadyForListing : " + ticketReadyForListing.size());
            } catch (JSONException ex) {
                System.out.println("++++++++++++++++ e4 : " + ex);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        this.myTicket.setMyTicketData(ticketReadyForListing);
        System.out.println("++++++++++++++++ myTicket : " + this.myTicket.toString());
        this.adapter.notifyDataSetChanged();
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
