package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Race;
import net.dv8tion.jda.api.interactions.components.Button;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;


public abstract class StartGame {

    public static void StartGame(){
        startGameButton();
    }

    private static void startGameButton(){
        Android24.jda.getTextChannelById(869983208698163250L).sendMessage("Test")
                .setActionRow(Button.primary("12", "Test 1"),
                        Button.danger("13", "Test 2")).queue();

    }

    public static String startGame(String guildID, String userID, Race race){
        try {
            // Setting it in th SQL
            Statement sqlStatement = Android24.getStatement();
            String query = "INSERT INTO `"+guildID+"`.`users_power` (`UserID`, `Race`) VALUES ('"+userID+"', '"+race+"');";
            sqlStatement.execute(query);
            sqlStatement.close();

            //Adding the role
            Android24.jda.getGuildById(guildID).addRoleToMember(userID,
                    Android24.jda.getRoleById(race.getID())).queue();

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
