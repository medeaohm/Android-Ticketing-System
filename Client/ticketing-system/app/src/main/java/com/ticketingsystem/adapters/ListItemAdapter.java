package com.ticketingsystem.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ticketingsystem.R;
import com.ticketingsystem.activities.MainActivity;
import com.ticketingsystem.http.ActivateTicketAsync;
import com.ticketingsystem.http.ActivateTicketCommand;
import com.ticketingsystem.http.OnTicketActivatedListener;
import com.ticketingsystem.models.MyTicketsListItemModel;
import com.ticketingsystem.models.TokenModel;
import com.ticketingsystem.utilities.AlertFactory;
import com.ticketingsystem.utilities.OkCommand;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ListItemAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    List<MyTicketsListItemModel> items;
    private MyTicketsListItemModel listItem;
    private MyTicketsListItemModel currentItem;
    private ProgressDialog progressDialog;
    private TokenModel token;

    private ImageView QRcode;
    private TextView status;
    private TextView expiresOn;
    private TextView duration;

    private String currentId;
    private Bitmap currentQRcode;
    private int currentPosition;

    public OnTicketActivatedListener mOnTicketActivatedListener;

    Gson gson = new Gson();

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
                    LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_view_item, null);

        }

        String authorizationToken = this.context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("token", "");
        token = gson.fromJson(authorizationToken, TokenModel.class);

        QRcode = (ImageView) convertView.findViewById(R.id.ticket_QR_code);
        status = (TextView) convertView.findViewById(R.id.ticket_status);
        expiresOn = (TextView) convertView.findViewById(R.id.ticket_expires_on);
        duration = (TextView) convertView.findViewById(R.id.ticket_duration);

        status.setOnClickListener(this);

        listItem = (MyTicketsListItemModel)getItem(position);
        currentId = listItem.getId();

        QRcode.setImageBitmap(listItem.getQRCode());
        QRcode.setOnClickListener(this);
        setTextStatus();
        setTextExpiresOn();
        setTextDuration();

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_status : {
                View parentRow = (View) v.getParent();
                /*
                ListView listView = (ListView) parentRow;
                final int position = listView.getPositionForView(parentRow); */
                this.activateTicket(currentId);
                break;
            }
            case R.id.ticket_QR_code : {
                loadPhoto(QRcode, currentItem.getQRCode());
                currentPosition = 0;
            }
        }
    }

    private void loadPhoto(ImageView imageView, Bitmap bitmap) {

        ImageView tempImageView = imageView;
        tempImageView.setImageBitmap(bitmap);


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.qr_code, null);
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Hide", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        imageDialog.create();
        imageDialog.show();
    }

    private void activateTicket(String currentId) {
/*
        listItem = (MyTicketsListItemModel)getItem(position);
        currentId = listItem.getId();
*/
        ActivateTicketAsync activateAsyncTask = new ActivateTicketAsync(this.context, token.access_token, currentId, new ActivateTicketCommand() {
            @Override
            public void execute(Boolean result) {
                progressDialog.dismiss();

                if (result) {
                    AlertFactory.createInformationAlertDialog(context, "Ticket Activated successfully.", "Success", new OkCommand() {
                        @Override
                        public void execute() {
                            goToHomeActivity();
                        }
                    }).show();
                } else {
                    AlertFactory.createInformationAlertDialog(context, "An error occurred.", "Error", null).show();
                }
            }
        });

        progressDialog = new ProgressDialog(this.context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        activateAsyncTask.execute();
    }

    private void setTextStatus() {
        if (listItem.getIsActivated() && !listItem.getIsExpired()){
            status.setText("Active");
            status.setTextColor(context.getResources().getColor(R.color.green));
            status.setAllCaps(false);
            status.setEnabled(false);
        }
        else if (listItem.getIsExpired()){
            status.setText("Expired");
            status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            status.setAllCaps(false);
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
                    String.format("%02d", listItem.getExpiresOn().get(Calendar.DAY_OF_MONTH)) + "/" +
                            String.format("%02d", listItem.getExpiresOn().get(Calendar.MONTH)) + "/" +
                            listItem.getExpiresOn().get(Calendar.YEAR) + System.getProperty("line.separator") +
                            String.format("%02d", listItem.getExpiresOn().get(Calendar.HOUR_OF_DAY) + 2) + ":" +
                            String.format("%02d", listItem.getExpiresOn().get(Calendar.MINUTE));

            expiresOn.setText(expDate);

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

    private void setTextDuration() {
        String durationStr = "";
        if ((int)(listItem.getDuration()) < 24){
            durationStr = listItem.getDuration().toString() + " " + ((int)(listItem.getDuration()) == 1 ? "hour" : "hours");
        }
        else if ((int)(listItem.getDuration()) < 168){
            durationStr = ((int)listItem.getDuration() / 24) + " " + ((int)listItem.getDuration()/24 == 1 ? "day" : "days");
        }
        else if ((int)(listItem.getDuration()) < 720){
            durationStr = ((int)listItem.getDuration() / (24 * 7)) + " " + ((int)listItem.getDuration()/(24*7) == 1 ? "week" : "weeks");
        }
        else if ((int)(listItem.getDuration()) == 720){
            durationStr = "1 month";
        }
        else if ((int)(listItem.getDuration()) == 2208){
            durationStr = "3 month";
        }
        else {
            durationStr = "-";
        }
        duration.setText(durationStr);
        duration.setTextColor(context.getResources().getColor(R.color.blue));
    }

    private void goToHomeActivity () {
        Intent intent_home = new Intent(context, MainActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        ((Activity)context).finish(); // destroy current activity..
        (context).startActivity(intent_home);
    }

    public void setCurrentPosition(int position){
        currentItem = (MyTicketsListItemModel)getItem(position);
        currentId = currentItem.getId();
    }
}