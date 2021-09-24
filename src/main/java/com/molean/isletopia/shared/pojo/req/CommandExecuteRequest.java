package com.molean.isletopia.shared.pojo.req;

public class CommandExecuteRequest {
    private String command;

    public CommandExecuteRequest() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandExecuteRequest(String command) {
        this.command = command;
    }
}
