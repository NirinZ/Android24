package com.SharxNZ.GameFunctions;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Beginning extends ListenerAdapter {

    private static PreparedStatement join;
    private static String joiningMessage = """
            Thanks for inviting my to your server!
            There are some stuff that you should take care of before we start.
            1) You or one of your staff should use the command `/server_setup` to define the server settings.
            2) When the bot joined he created a role called `---transformations---`. This role will define where all the transformations roles will be positioned.
               Therefore, it is highly recommended to move this role's position up as much as you can so when someone is transformed, his color will change accordingly.
               BTW, you can choose other role to be set the transformations role positions, just use the `/server_setup` 😁
            """;

    static {
        try {
            join = Android24.getConnection().prepareStatement(
                    "INSERT INTO `guilds`.`guilds_data` (`GuildID`, `GuildName`, `TransRole`) VALUES (?, ?, ?);");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent guildJoinEvent) {
        guildJoinEvent.getGuild().createRole().setName("---transformations---").queue(role -> {
            try {
                join.setLong(1, guildJoinEvent.getGuild().getIdLong());
                join.setString(2, guildJoinEvent.getGuild().getName());
                join.setLong(3, role.getIdLong());
                join.executeUpdate();

                if (guildJoinEvent.getGuild().getOwner().getUser().hasPrivateChannel())
                    guildJoinEvent.getGuild().getOwner().getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(joiningMessage).queue();
                    });
                else
                    Android24.jda.getUserById(Android24.nirin).openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage("The owner of " + guildJoinEvent.getGuild().getName() + "have no private channel!" +
                                "\nYou need to do things manually...").queue();
                    });
            } catch (SQLException throwables) {
                Android24.logError(throwables);
                throwables.printStackTrace();
            }
        });
        System.out.println("Joined to " + guildJoinEvent.getGuild().getId());
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent guildLeaveEvent) {
    }
}
