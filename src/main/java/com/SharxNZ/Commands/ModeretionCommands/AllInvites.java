package com.SharxNZ.Commands.ModeretionCommands;

import com.SharxNZ.Android24;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

public class AllInvites extends Command {

    public AllInvites() {
        super.name = "AllInvites";
        super.aliases = new String[]{"ali"};
//        super.arguments = "";
        super.help = "Get the invites to all the servers that the bot is in";
        super.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        super.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getAuthor().openPrivateChannel().queue(channel -> {
            for (Guild guild : Android24.jda.getGuilds()) {
                try {
                    guild.getDefaultChannel().createInvite().setTemporary(true).queue(invite ->
                            channel.sendMessage(invite.getUrl()).queue(null, throwable ->
                                    commandEvent.reply("You need to open your DMs to this bot to get the invites...")));
                } catch (Exception exception) {
                    Android24.logError(exception);
                }
            }
        });
    }
}
