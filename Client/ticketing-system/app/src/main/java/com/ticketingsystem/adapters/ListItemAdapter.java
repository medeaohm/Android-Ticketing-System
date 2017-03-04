package com.ticketingsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;
import com.ticketingsystem.R;
import com.ticketingsystem.models.MyTicketsListItemModel;

import java.util.List;

public class ListItemAdapter extends BaseAdapter{
    Context context;
    List<MyTicketsListItemModel> items;

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
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.ticket_icon);
        TextView isActive = (TextView) convertView.findViewById(R.id.ticket_is_active);
        TextView isExpired = (TextView) convertView.findViewById(R.id.ticket_is_expired);
        TextView duration = (TextView) convertView.findViewById(R.id.ticket_duration);

        MyTicketsListItemModel listItem = (MyTicketsListItemModel)getItem(position);
/*
        Picasso picasso = PicassoBuilder.getInstance(this.context);

        picasso.load(listItem.getImage()).fit().into(imgIcon);*/
        imgIcon.setImageBitmap(listItem.getQRCode());
        isActive.setText(listItem.getIsActivated() ? "Yes" : "No");
        isExpired.setText(listItem.getIsExpired() ? "Yes" : "No");
        duration.setText(listItem.getDuration().toString());
        System.out.println("++++++++++++++++ isActive : " + isActive.getText());
        System.out.println("++++++++++++++++ ListItemAdapter view: ");

        return convertView;
    }

}