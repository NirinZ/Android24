package com.SharxNZ.Commands.ModeretionCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class setCmdCh extends Command {

    public setCmdCh(){
        super.name = "set cmd channel";
        super.aliases = new String[]{"scc", "setcmd"};
        super.arguments = "[channel id]";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

    }
}
