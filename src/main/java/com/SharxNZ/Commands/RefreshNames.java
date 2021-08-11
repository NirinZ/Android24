package com.SharxNZ.Commands;

import com.SharxNZ.Android24;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RefreshNames extends Command {

    private static PreparedStatement getNames;
    private static PreparedStatement setNames;

    public RefreshNames() throws SQLException {
        super.name = "Refresh name";
        super.aliases = new String[]{"rn", "RN"};
        super.help = "Refresh all the names in the DataBase";
        getNames = Android24.getConnection().prepareStatement("SELECT UserID FROM `android24`.users_data limit ?, 10;");
        setNames = Android24.getConnection().prepareStatement("UPDATE `android24`.`users_data` SET `UserName` = ? WHERE `UserID` = ?;");

    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            int limit = 0;
            while (true) {
                getNames.setInt(1, limit);
                ResultSet result = getNames.executeQuery();
                if (result.next()) {
                    for (int i = 1; i < 11; i++) {
                        long id = result.getLong(i);
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
                        System.out.println("Changing name to: " + id);
                    }
                    limit += 10;
                    result.close();
                }
                else{
                    break;
                }

            }
        } catch (SQLException throwables) {
            System.out.println("Outer try");
            Android24.logError(throwables);
            throwables.printStackTrace();
        }

    }
}
