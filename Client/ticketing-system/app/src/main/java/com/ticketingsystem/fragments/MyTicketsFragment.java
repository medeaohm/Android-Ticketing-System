package com.ticketingsystem.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import com.ticketingsystem.R;
import com.ticketingsystem.activities.HomeActivity;
import com.ticketingsystem.adapters.ListItemAdapter;
import com.ticketingsystem.http.IMyTicket;
import com.ticketingsystem.http.LoadMyTicketsAsync;
import com.ticketingsystem.models.TokenModel;

import static android.content.Context.MODE_PRIVATE;

public class MyTicketsFragment extends ListFragment {
    private TokenModel token;

    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_tickets_list, container, false);

        String authorizationToken = this.getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("token", "");
        token = gson.fromJson(authorizationToken, TokenModel.class);

        return  view;
    }

    ListItemAdapter adapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ListItemAdapter(getActivity(), ((HomeActivity) getActivity()).myTicketList);
        setListAdapter(adapter);
        LoadMyTickets((IMyTicket) getActivity(), adapter, token.access_token);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Toast.makeText(getActivity(), "item clicked" + position, Toast.LENGTH_SHORT).show();
        adapter.setCurrentPosition(position);
    }


    public void LoadMyTickets(IMyTicket myTickets, ListItemAdapter adapter, String authorizationToken) {
        String loadMyTicketsUrl = getResources().getString(R.string.current_user_tickets_url);
        new LoadMyTicketsAsync(this.getContext(), myTickets , authorizationToken, loadMyTicketsUrl, adapter).execute();
    }
}
