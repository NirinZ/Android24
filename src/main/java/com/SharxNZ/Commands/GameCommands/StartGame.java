package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Race;
import net.dv8tion.jda.api.interactions.components.Button;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public abstract class StartGame {

    private static PreparedStatement setRace;
    private static PreparedStatement insertUser;

    public static void StartGame() {
        try {
            startGameButton();
            setRace = Android24.getConnection().prepareStatement(
                    "UPDATE `android24`.`users_data` SET `Race` = ? WHERE (`UserID` = ?);");
            insertUser = Android24.getConnection().prepareStatement(
                    "INSERT INTO `android24`.`users_data` (`UserID`, `UserName`, `Race`)" +
                            " VALUES (?, ?, ?);"
            );
        } catch (Exception throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }


    private static void startGameButton(){
        Android24.jda.getTextChannelById(869983208698163250L).sendMessage("Test")
                .setActionRow(Button.primary("12", "Test 1"),
                        Button.danger("13", "Test 2")).queue();

    }

    public static String startGame(long userID, Race race){
        try {
            if(Android24.getConnection().prepareStatement("SELECT `UserID` FROM `android24`.`users_data` where `UserID` = " + userID + ";").executeQuery().next()) {
                // Setting it in th SQL
                setRace.setString(1, race.toString());
                setRace.setLong(2, userID);
                setRace.executeUpdate();
            }else {
                insertUser.setLong(1, userID);
                insertUser.setString(3, race.toString());
                Android24.jda.retrieveUserById(userID).queue(user -> {
                    try {
                        insertUser.setString(2, user.getAsTag());
                        insertUser.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        Android24.logError(throwables);
                    }
                });
            }
            return "You have been added successfully";
        } catch (SQLIntegrityConstraintViolationException exception) {
            return "You're already in the game";
        } catch (IllegalArgumentException exception) {
            return "The role ID isn't correct";
        } catch (Exception throwables) {
            Android24.logError(throwables);
            return "Some error accrued";
        }
    }

}
