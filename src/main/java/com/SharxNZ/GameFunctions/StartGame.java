package com.SharxNZ.GameFunctions;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Race;
import com.SharxNZ.Utilities.Utils;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class StartGame {

    public static void StartGame() {
        try {
            OptionData optionData = new OptionData(OptionType.STRING, "race", "choose the race you want to play").setRequired(true);
            ResultSet resultSet = Android24.getConnection().createStatement().executeQuery("SELECT RaceName FROM android24.races;");
            while (resultSet.next())
                optionData.addChoice(resultSet.getString(1), resultSet.getString(1));
            Android24.addCommands(new CommandData("start_game", "Let's you start the game and choose your race")
                    .addOptions(optionData));

            startGameButton();

        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }


    private static void startGameButton(){
        Android24.jda.getTextChannelById(869983208698163250L).sendMessage("Choose your race")
                .setActionRow(Button.primary("saiyan", "Saiyan"),
                        Button.danger("frieza", "Frieza")).queue();

    }

    public static String startGame(long userID, Race race){
        try {
            PreparedStatement setRace = Android24.getConnection().prepareStatement(
                    "UPDATE `android24`.`users_data` SET `Race` = ? WHERE (`UserID` = ?);");
            PreparedStatement insertUser = Android24.getConnection().prepareStatement(
                    "INSERT INTO `android24`.`users_data` (`UserID`, `UserName`, `Race`)" +
                            " VALUES (?, ?, ?);");
            // Setting it in th SQL
            if (Utils.checkInGame(userID))
                return "You're already in the game";
            //Adding the race to the user
            else if(Android24.getConnection().prepareStatement("SELECT `UserID` FROM `android24`.`users_data` where `UserID` = " + userID + ";").executeQuery().next()) {
                setRace.setString(1, race.toString());
                setRace.setLong(2, userID);
                setRace.executeUpdate();
                setRace.close();
            }
            //Creating a whole mew user
            else {
                insertUser.setLong(1, userID);
                insertUser.setString(3, race.toString());
                Android24.jda.retrieveUserById(userID).queue(user -> {
                    try {
                        insertUser.setString(2, user.getAsTag());
                        insertUser.executeUpdate();
                        insertUser.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        Android24.logError(throwables);
                    }
                });
            }
            return "You have been added successfully";
        } catch (IllegalArgumentException exception) {
            return "The role ID isn't correct";
        } catch (Exception throwables) {
            Android24.logError(throwables);
            return "Some error accrued";
        }
    }

}
