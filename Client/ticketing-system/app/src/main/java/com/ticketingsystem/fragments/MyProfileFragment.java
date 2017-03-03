package com.ticketingsystem.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.http.GetProfileAsync;
import com.ticketingsystem.http.GetProfileCommand;
import com.ticketingsystem.models.TokenModel;
import com.ticketingsystem.models.UserProfileModel;

import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;

import static android.content.Context.MODE_PRIVATE;

public class MyProfileFragment extends Fragment{
    private TextView username;
    private TextView firstname;
    private TextView lastname;
    private TextView balance;
    private UserProfileModel currentUser;
    Gson gson = new Gson();

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        String authorizationToken = this.getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("token", "");
        TokenModel token = gson.fromJson(authorizationToken, TokenModel.class);

        System.out.println("++++++++++++++++ token: " + token.access_token);
        this.username = (TextView) rootView.findViewById(R.id.profile_username);
        this.firstname = (TextView) rootView.findViewById(R.id.profile_first_name);
        this.lastname = (TextView) rootView.findViewById(R.id.profile_last_name);
        this.balance = (TextView) rootView.findViewById(R.id.profile_balance);
        URI uri = null;
        try {
            uri = new URI("http://ticket-system-rest.apphb.com/api/users/info");
        } catch (URISyntaxException e) {
            System.out.println("++++++++++++++++ err: " + e);
            e.printStackTrace();
        }

        GetProfileAsync getProfileAsync = new GetProfileAsync(this.getActivity(), token.access_token, uri, new GetProfileCommand() {
            @Override
            public void execute(UserProfileModel user) {
                System.out.println("++++++++++++++++ user: " + user.Email);
                username.setText(user.UserName);
                firstname.setText(user.FirstName);
                lastname.setText(user.LastName);
                balance.setText(String.valueOf(user.Balance));
            }
        });
        getProfileAsync.execute();

        return rootView;
    }
}
