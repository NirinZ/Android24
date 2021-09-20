package com.SharxNZ.Utilities;

import com.SharxNZ.Android24;

import javax.management.relation.Role;
import javax.naming.NameNotFoundException;
import java.lang.annotation.Annotation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.SharxNZ.Android24.jda;

public class Server {
    private long guildID;
    private long commandsCh;
    private long welcomeCh;
    private long loggingCh;
    private long transRole;
    private boolean allowTransGif;


    public Server(long guildID) {
        try {
            PreparedStatement create = Android24.getConnection().prepareStatement(
                    "SELECT CommandsCh, WelcomeCh, LoggingCh,TransRole, AllowTransGif FROM guilds.guilds_data WHERE GuildID = ?;");

            create.setLong(1, guildID);
            ResultSet resultSet = create.executeQuery();
            if (!resultSet.next()) {
                create.close();
                throw new NameNotFoundException("The guild name has not found");
            }
            this.guildID = guildID;
            commandsCh = resultSet.getLong(1);
            welcomeCh = resultSet.getLong(2);
            loggingCh = resultSet.getLong(3);
            transRole = resultSet.getLong(4);
            allowTransGif = resultSet.getBoolean(5);
            create.close();
        } catch (SQLException | NameNotFoundException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public void setServer() {
        try {
            PreparedStatement set = Android24.getConnection().prepareStatement(
                    "UPDATE guilds.guilds_data SET CommandsCh = ?, WelcomeCh = ?, LoggingCh = ?,TransRole = ?, AllowTransGif = ? WHERE GuildID = ?;");
            set.setLong(1, commandsCh);
            set.setLong(2, welcomeCh);
            set.setLong(3, loggingCh);
            set.setLong(4, transRole);
            set.setBoolean(5, allowTransGif);
            set.setLong(6, guildID);
            set.executeUpdate();
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public long getGuildID() {
        return guildID;
    }

    public long getCommandsCh() {
        return commandsCh;
    }

    public long getWelcomeCh() {
        return welcomeCh;
    }

    public long getLoggingCh() {
        return loggingCh;
    }

    public long getTransRole() {
        return transRole;
    }

    public boolean isAllowTransGif() {
        return allowTransGif;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public void setCommandsCh(long commandsCh) {
        this.commandsCh = commandsCh;
    }

    public void setWelcomeCh(long welcomeCh) {
        this.welcomeCh = welcomeCh;
    }

    public void setLoggingCh(long loggingCh) {
        this.loggingCh = loggingCh;
    }

    public void setTransRole(long transRole) {
        this.transRole = transRole;
    }

    public void setAllowTransGif(boolean allowTransGif) {
        this.allowTransGif = allowTransGif;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Command Channel: ").append(commandsCh);
        return string.toString();
    }
}
