package com.ticketingsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketingsystem.R;
import com.ticketingsystem.models.MyTicketsListItemModel;

import java.util.List;

public class ListItemAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    List<MyTicketsListItemModel> items;
    private MyTicketsListItemModel listItem;

    ImageView QRcode;
    TextView status;
    TextView expiresOn;
    TextView duration;

    public ListItemAdapter(Context context, List<MyTicketsListItemModel> items) {
        this.context = context;
        this.items = items;
        System.out.println("++++++++++++++++ ListItemAdapter : ");
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_view_item, null);

        }
        QRcode = (ImageView) convertView.findViewById(R.id.ticket_QR_code);
        status = (TextView) convertView.findViewById(R.id.ticket_status);
        expiresOn = (TextView) convertView.findViewById(R.id.ticket_expires_on);
        duration = (TextView) convertView.findViewById(R.id.ticket_duration);

        status.setOnClickListener(this);

        listItem = (MyTicketsListItemModel)getItem(position);

        QRcode.setImageBitmap(listItem.getQRCode());
        setTextStatus();
        setTextExpiresOn();
        duration.setText(listItem.getDuration().toString());

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_status : {
                this.activateTicket();
                break;
            }
        }
    }

    private void activateTicket() {
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
    }

    private void setTextStatus() {
        if (listItem.getIsActivated() && !listItem.getIsExpired()){
            status.setText("Active");
            status.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (listItem.getIsExpired()){
            status.setText("Expired");
            status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            status.setEnabled(false);
        }
        else {
            status.setText("Activate!");
            status.setTextColor(context.getResources().getColor(R.color.blue));
            status.setAllCaps(true);
            status.setEnabled(true);
        }
    }

    private void setTextExpiresOn() {
        if (listItem.getIsActivated() && !listItem.getIsExpired()){
            String expDate =
                    listItem.getExpiresOn().getDay() + "/" +
                            listItem.getExpiresOn().getMonth() + "/" +
                            listItem.getExpiresOn().getYear() + " - " +
                            listItem.getExpiresOn().getHours() + ":" + listItem.getExpiresOn().getMinutes();

            expiresOn.setText("expDate");

            expiresOn.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (listItem.getIsExpired()){
            expiresOn.setText("-");
            expiresOn.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else {
            expiresOn.setText("-");
            expiresOn.setTextColor(context.getResources().getColor(R.color.blue));
        }
    }

}