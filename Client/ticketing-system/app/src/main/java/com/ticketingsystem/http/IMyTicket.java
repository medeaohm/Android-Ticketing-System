package com.ticketingsystem.http;

import com.ticketingsystem.models.MyTicketsListItemModel;

import java.util.List;

public interface IMyTicket {
    void setMyTicketData(List<MyTicketsListItemModel> models);
}