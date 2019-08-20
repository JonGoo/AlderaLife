package com.fivem.alderalife.model;

public class Command {
    private String server;

    private String command;

    public Command(String server, String command) {
        this.server = server;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
