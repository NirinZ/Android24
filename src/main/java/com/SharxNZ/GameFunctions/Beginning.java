package com.SharxNZ.GameFunctions;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.exception.ContextedException;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Beginning extends ListenerAdapter {

    private static String joiningMessage = """
            Thanks for inviting my to your server!
            There are some stuff that you should take care of before we start.
            1) You or one of your staff should use the command `/server_setup` to define the server settings.
            2) When the bot joined he created a role called `---transformations---`. This role will define where all the transformations roles will be positioned.
               Therefore, it is highly recommended to move this role's position up as much as you can so when someone is transformed, his color will change accordingly.
               BTW, you can choose other role to be set the transformations role positions, just use the `/server_setup` 😁
            """;

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent guildJoinEvent) {
        if (guildJoinEvent.getGuild().getRoles().stream().anyMatch(role -> role.getName().equals("---transformations---"))) {
            try {
                PreparedStatement join = Android24.getConnection().prepareStatement(
                        "INSERT INTO `guilds`.`guilds_data` (`GuildID`, `GuildName`, `TransRole`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `GuildName` = ?, `TransRole` = ?;");

                join.setLong(1, guildJoinEvent.getGuild().getIdLong());
                join.setString(2, guildJoinEvent.getGuild().getName());
                join.setLong(3, guildJoinEvent.getGuild().getRolesByName("---transformations---", true).get(0).getIdLong());
                join.setString(4, guildJoinEvent.getGuild().getName());
                join.setLong(5, guildJoinEvent.getGuild().getRolesByName("---transformations---", true).get(0).getIdLong());
                join.executeUpdate();

                guildJoinEvent.getGuild().retrieveOwner().queue(owner -> {
                    owner.getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(joiningMessage).queue(null, throwable -> {
                            Android24.nirin.openPrivateChannel().queue(nirinChannel -> {
                                nirinChannel.sendMessage("The owner of " + guildJoinEvent.getGuild().getName() + " have no private channel!" +
                                        "\nYou need to do things manually...").queue();
                            });
                        });
                    });
                });
            } catch (SQLException throwables) {
                Android24.logError(throwables);
                throwables.printStackTrace();
            }
        } else
            guildJoinEvent.getGuild().createRole().setName("---transformations---").queue(role -> {
                try {
                    PreparedStatement join = Android24.getConnection().prepareStatement(
                            "INSERT INTO `guilds`.`guilds_data` (`GuildID`, `GuildName`, `TransRole`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `GuildName` = ?, `TransRole` = ?;");

                    join.setLong(1, guildJoinEvent.getGuild().getIdLong());
                    join.setString(2, guildJoinEvent.getGuild().getName());
                    join.setLong(3, role.getIdLong());
                    join.setString(4, guildJoinEvent.getGuild().getName());
                    join.setLong(5, role.getIdLong());
                    join.executeUpdate();

                    guildJoinEvent.getGuild().retrieveOwner().queue(owner -> {
                        owner.getUser().openPrivateChannel().queue(privateChannel -> {
                            privateChannel.sendMessage(joiningMessage).queue(null, throwable -> {
                                Android24.nirin.openPrivateChannel().queue(nirinChannel -> {
                                    nirinChannel.sendMessage("The owner of " + guildJoinEvent.getGuild().getName() + "have no private channel!" +
                                            "\nYou need to do things manually...").queue();
                                });
                            });
                        });
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
