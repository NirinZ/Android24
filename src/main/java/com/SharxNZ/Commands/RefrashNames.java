package com.SharxNZ.Commands;

import com.SharxNZ.Android24;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RefrashNames extends Command {

    private final Statement sqlStatement = Android24.getStatement();

    public RefrashNames() throws SQLException {
        super.name = "Refresh name";
        super.aliases = new String[]{"rn", "RN"};
        super.help = "Refresh all the names in the DataBase";

    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            int limit = 0;
            while (true) {
                ResultSet result = sqlStatement.executeQuery("SELECT UserID FROM `"+ commandEvent.getGuild().getId() +"`.users_data limit "+limit+", 1;");
                if (result.next()) {
                    String id = result.getString(1);
                    Android24.jda.retrieveUserById(id).queue(user -> {
                        try {
                            String query = "UPDATE `24 gaming`.`users_data` SET `UserName` = '" +
                                    user.getAsTag().replace("'", "''") + "' WHERE `UserID` = '" + id + "';";
                            sqlStatement.executeUpdate(query);
                        } catch (SQLException throwables) {
                            System.out.println("Inner try");
                            throwables.printStackTrace();
                        }
                    });
                    System.out.println("Changing name to: " + id);
                    limit++;
                    result.close();
                }
                else{
                    break;
                }

            }
        } catch (SQLException throwables) {
            System.out.println("Outer try");
            throwables.printStackTrace();
        }

    }
}
