package com.ticketingsystem.models;

public class ChargeRequestModel {
    public String CardNumber;
    public String SecurityCode;
    public String CardType;
    public Number ExpireMonth;
    public Number ExpireYear;
    public String CardHolderNames;
    public Number Amount;
}

/*
{ CardNumer: "string", SecurityCode: "string", CardType: "string", ExpireMonth: "number (1-12)", ExpireYear: "number", CardHolderNames: "string", Amount: "number"}
*/