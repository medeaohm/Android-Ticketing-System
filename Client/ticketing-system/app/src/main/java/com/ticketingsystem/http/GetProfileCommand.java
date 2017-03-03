package com.ticketingsystem.http;

import com.ticketingsystem.models.UserProfileModel;

public interface GetProfileCommand {
    void execute(UserProfileModel user);
}
