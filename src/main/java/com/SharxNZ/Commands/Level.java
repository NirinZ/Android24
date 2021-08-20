package com.SharxNZ.Commands;

import com.SharxNZ.Android24;
import com.SharxNZ.Utilities.Graphics;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Level{

    private static PreparedStatement levelStatement;

    public static void Level(){
        try {
            levelStatement = Android24.getConnection().prepareStatement(
                        "SELECT XP FROM `" + Android24.schema + "`.users_data where UserID = ?;");
            Android24.commandListUpdateAction.addCommands(new CommandData("level", "Returns your level")
                    .addOptions(new OptionData(OptionType.BOOLEAN, "display", "display your level")));
        } catch (Exception throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    //super.category = new Category("XP");


    public static short calculateLevel(long xp){
        return (short) Math.floor(Math.pow(xp, Android24.difficulty));
    }


    public static byte[] returnLevel(long guildID, long userID, String userURL){
        try {
            levelStatement.setLong(1, userID);
            ResultSet resultSet = levelStatement.executeQuery();

            if(resultSet.next()) {
                String guildName = Android24.jda.getGuildById(guildID).getName();
                long xp = resultSet.getLong(1);
                resultSet.close();
                return Graphics.levelImage(userURL, guildName, xp);
            }
            else return null;
        } catch (Exception throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }

    public static MessageEmbed returnLevelEmbed(long guildID, long userID, String userURL){
        AtomicReference<String> imageUrl = new AtomicReference<>();
        Android24.getImageUrl(Level.returnLevel(guildID, userID, userURL), imageUrl);
        return new EmbedBuilder().setImage(imageUrl.get()).build();
    }
}
