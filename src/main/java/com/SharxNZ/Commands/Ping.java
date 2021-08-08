package com.SharxNZ.Commands;

import com.SharxNZ.Daishinkan;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Ping extends ListenerAdapter {
    public Ping(){
        Daishinkan.commandListUpdateAction.addCommands(new CommandData("ping", "pong you"));
    }
    String message;

    //@Override //s
    public void onMessageReceived(MessageReceivedEvent m) {
        if (m.getMessage().getContentRaw().startsWith(Daishinkan.prefix)) {
            message = m.getMessage().getContentRaw().substring(1).toLowerCase();

            if (message.equals("ping")) {
                m.getChannel().sendTyping().queue();
                m.getChannel().sendMessage("pong"/* + m.getGuild().getDefaultChannel()*/).queue();
            }
        }
    }
}

