package com.ticketingsystem.models;

import android.graphics.Bitmap;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyTicketsListItemModel {

    private String Id;
    private Date BoughtAt;
    private Number Cost;
    private Boolean Expired;
    private Boolean Activated;
    private Date DateActivated;
    private Calendar ExpiresOn;
    private Number Duration;
    private Bitmap QRCode;
    /*
    * [{ Id: "GUID", BoughtAt: "Date", Cost: "Decimal", Expired: "Bool", "Activated": "Bool", "DateActivated": "Date/null", "ExpiresOn": "Date/null", Duration: "NumberHours", QRCode: "LongText", Owner: { "Owner": { Id: "GUID" UserName: "Text", FullName: "Text"} } ]
    * */
    public MyTicketsListItemModel(String Id, Date BoughtAt, Number Cost, Boolean Expired, Boolean Activated,Date DateActivated, Calendar ExpiresOn, Number Duration, Bitmap QRCode) {
        this.Id = Id;
        this.BoughtAt = BoughtAt;
        this.Cost = Cost;
        this.Expired = Expired;
        this.Activated = Activated;
        this.DateActivated = DateActivated;
        this.ExpiresOn = ExpiresOn;
        this.Duration = Duration;
        this.QRCode = QRCode;
    }

    public String getId() { return this.Id; }

    public Date getBoughtAt() { return this.BoughtAt; }

    public Number getCost() { return this.Cost; }

    public Boolean getIsExpired() { return this.Expired; }

    public Boolean getIsActivated() { return this.Activated; }

    public Date getDateActivated() { return this.DateActivated; }

    public Calendar getExpiresOn() { return this.ExpiresOn; }

    public Number getDuration() { return this.Duration; }

    public Bitmap getQRCode(){ return  this.QRCode; }
}