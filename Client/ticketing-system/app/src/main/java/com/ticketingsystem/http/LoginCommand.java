package com.ticketingsystem.http;

public interface LoginCommand {
    void execute(String token);
}
