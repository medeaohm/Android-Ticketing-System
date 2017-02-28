package com.ticketingsystem.http;

import com.ticketingsystem.models.UserRegisterResponseModel;

public interface RegisterCommand {
    void execute(UserRegisterResponseModel user);
}