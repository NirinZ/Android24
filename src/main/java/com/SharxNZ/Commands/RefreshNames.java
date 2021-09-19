package com.SharxNZ.Commands;

import com.SharxNZ.Android24;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RefreshNames extends Command {

    private static PreparedStatement getNames;
    private static PreparedStatement setNames;
    private static PreparedStatement getSNames;
    private static PreparedStatement setSNames;

    public RefreshNames() throws SQLException {
        super.name = "Refresh name";
        super.aliases = new String[]{"rn", "RN"};
        super.arguments = "[n\\s]";
        super.help = "Refresh all the names in the DataBase. (n - users names, s - servers names)";
        getNames = Android24.getConnection().prepareStatement("SELECT UserID FROM `android24`.users_data limit ?, 10;");
        setNames = Android24.getConnection().prepareStatement("UPDATE `android24`.`users_data` SET `UserName` = ? WHERE `UserID` = ?;");
        getSNames = Android24.getConnection().prepareStatement("SELECT GuildID FROM guilds.guilds_data limit ?, 10;");
        setSNames = Android24.getConnection().prepareStatement("UPDATE guilds.`guilds_data` SET `GuildName` = ? WHERE `GuildID` = ?;");


    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            if(commandEvent.getArgs().isEmpty() || commandEvent.getArgs().equalsIgnoreCase("n")) {
                int limit = 0;
                int check;
                do {
                    check = 10;
                    getNames.setInt(1, limit);
                    ResultSet result = getNames.executeQuery();
                    if (result.next()) {
                        for (int i = 0; i < 10; i++) {
                            long id = result.getLong(1);
                            Android24.jda.retrieveUserById(id).queue(user -> {
                                try {
                                    setNames.setString(1, user.getAsTag());
                                    setNames.setLong(2, id);
                                    setNames.executeUpdate();
                                } catch (SQLException throwables) {
                                    System.out.println("Inner try");
                                    Android24.logError(throwables);
                                    throwables.printStackTrace();
                                }
                            });
                            check--;
                            System.out.println("Changing name to: " + id);
                        }
                        limit += 10;
                        result.close();
                    } else {
                        break;
                    }
                } while (check == 0);
            }
            else if(commandEvent.getArgs().equalsIgnoreCase("s")){
                int limit = 0;
                int check;
                do {
                    check = 10;
                    getSNames.setInt(1, limit);
                    ResultSet result = getSNames.executeQuery();
                    for (int i = 0; i < 10; i++) {
                        if (result.next()) {
                            long id = result.getLong(1);
                            try {
                                setSNames.setString(1, Android24.jda.getGuildById(id).getName());
                                setSNames.setLong(2, id);
                                setSNames.executeUpdate();
                                check--;
                            } catch (SQLException throwables) {
                                System.out.println("Inner try "+Android24.jda.getGuildById(id).getName());
                                Android24.logError(throwables);
                                throwables.printStackTrace();
                            }
                            System.out.println("Changing name to: " + id);
                        } else {
                            break;
                        }
                    }
                    limit += 10;
                    result.close();
                } while (check == 0);
            }
        } catch (SQLException throwables) {
            System.out.println("Outer try");
            Android24.logError(throwables);
            throwables.printStackTrace();
        }

    }
}
