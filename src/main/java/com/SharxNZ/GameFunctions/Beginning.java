package com.SharxNZ.GameFunctions;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.sql.Statement;

public class Beginning extends ListenerAdapter {

    private final Statement sqlStatement = Android24.getConnection().createStatement();
    private final String[] tableCreation = new String[]{
            "CREATE TABLE IF NOT EXISTS `users_data` (\n" +
                    "  `UserID` varchar(18) NOT NULL,\n" +
                    "  `UserName` tinytext NOT NULL,\n" +
                    "  `XP` bigint(1) unsigned zerofill DEFAULT '0',\n" +
                    "  `Zeni` bigint(1) unsigned zerofill DEFAULT '0',\n" +
                    "  `Level` tinyint(1) unsigned zerofill DEFAULT '0',\n" +
                    "  `Roles` enum('base','ss1','ss2') DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`UserID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Where we store most of the data of the users';\n",
            "CREATE TABLE IF NOT EXISTS `users_power` (\n" +
                    "  `UserID` VARCHAR(18) NOT NULL,\n" +
                    "  `Race` ENUM('Saiyan', 'Frieza') NOT NULL,\n" +
                    "  `PowerPoints` INT UNSIGNED NOT NULL DEFAULT 0,\n" +
                    "  `Health` INT UNSIGNED NOT NULL DEFAULT 0,\n" +
                    "  `Ki` int unsigned NOT NULL DEFAULT '0',\n" +
                    "  `StrikeAttack` INT UNSIGNED NOT NULL DEFAULT 0,\n" +
                    "  `KiAttack` INT UNSIGNED NOT NULL DEFAULT 0,\n" +
                    "  `Defence` INT UNSIGNED NOT NULL DEFAULT 0,\n" +
                    "  `Speed` INT UNSIGNED NOT NULL DEFAULT 0,\n" +
                    "  PRIMARY KEY (`UserID`));\n"
    };

    public Beginning() throws SQLException {
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent guildJoinEvent) {
        try {
            sqlStatement.execute("CREATE DATABASE IF NOT EXISTS`" + guildJoinEvent.getGuild().getId() + "` ;");
            sqlStatement.execute("USE `" + guildJoinEvent.getGuild().getId() + "` ;");
            for (String query : tableCreation) {
                sqlStatement.execute(query);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Joined to " + guildJoinEvent.getGuild().getId());
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent guildLeaveEvent) {
        try {
            sqlStatement.execute("DROP DATABASE `" + guildLeaveEvent.getGuild().getId() + "`;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
