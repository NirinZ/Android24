package com.SharxNZ.Utilities;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Embeds {

    private static PreparedStatement asStatement;
    private static PreparedStatement tsStatement;
    private static PreparedStatement daStatement;
    private static PreparedStatement dtStatement;
/*    private static String displaySql = "SELECT " +
            "$Name, $Abbreviated, Cost, MinimalLevel, AttackPowerUp, DefencePowerUp, SpeedPowerUp, KiConsumption, Description, ?, Gif" +
            " FROM android24.#s" +
            " JOIN" +
            " android24.Shop ON $Name = Name" +
            " WHERE $Name = '?' OR $Abbreviated = ?;";*/

    public static void Embeds(){
        try {
            asStatement = Android24.getConnection().prepareStatement("SELECT " +
                    "AttackName, Cost, MinimalLevel" +
                    " FROM android24.attacks" +
                    " JOIN" +
                    " android24.shop ON AttackName = Name" +
                    " WHERE ForcedRace is null or ForcedRace = ?;");
            tsStatement = Android24.getConnection().prepareStatement("SELECT " +
                    "TransformationName,  Cost, MinimalLevel" +
                    " FROM android24.transformations" +
                    " JOIN" +
                    " android24.shop ON TransformationName = Name" +
                    " WHERE ForcedRace is null or ForcedRace = ?;");
            daStatement = Android24.getConnection().prepareStatement("SELECT " +
                    "AttackName, AttackAbbreviated, " +
                    "Cost, MinimalLevel, AttackPowerUp, DefencePowerUp, SpeedPowerUp, KiConsumption, " +
                    "Counter, AttackType, Description, Gif" +
                    " FROM android24.attacks" +
                    " JOIN" +
                    " android24.shop ON AttackName = Name" +
                    " WHERE AttackName = ? OR AttackAbbreviated = ?;");
            dtStatement = Android24.getConnection().prepareStatement("SELECT " +
                    "TransformationName, TransformationAbbreviated, " +
                    "Cost, MinimalLevel, AttackPowerUp, DefencePowerUp, SpeedPowerUp, KiConsumption, " +
                    "Description, Gif" +
                    " FROM android24.transformations" +
                    " JOIN" +
                    " android24.shop ON TransformationName = Name" +
                    " WHERE TransformationName = ? OR TransformationAbbreviated = ?;");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }


    public static MessageEmbed savedEmbed(){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.green);
        embedBuilder.addField("All changes has been saved!", "", true);
        embedBuilder.setImage("https://user-images.githubusercontent.com/11019190/45695978-d59faa00-bb62-11e8-8f37-7b447356d237.png");
        return embedBuilder.build();
    }

    public static MessageEmbed discardEmbed(){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.addField("Your changes has been discarded!", "", true);
        embedBuilder.setImage("https://static.thenounproject.com/png/2025351-200.png");
        return embedBuilder.build();
    }

    public static MessageEmbed errorEmbed(){
        return new EmbedBuilder().setImage("https://www.computerhope.com/jargon/e/error.png").setColor(Color.red).build();
    }

    public static MessageEmbed errorTextEmbed(String text){
        return new EmbedBuilder().setTitle(text).setColor(Color.red).build();
    }

    public static MessageEmbed successTextEmbed(String text){
        return new EmbedBuilder().setTitle(text).setColor(Color.green).build();
    }

    public static MessageEmbed attacksShop(long userID){
        try {
            EmbedBuilder asEmbed = new EmbedBuilder();
            asEmbed.setTitle("Attacks Shop");
            asEmbed.setDescription("Here you can see the list of all the attacks that you can buy");
            asStatement.setString(1, Utils.checkRace(userID));
            ResultSet resultSet = asStatement.executeQuery();
            while (resultSet.next())
                asEmbed.addField(resultSet.getString(1), resultSet.getString(2) + "$ Lvl:" + resultSet.getString(3), true);
            return asEmbed.build();
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return errorEmbed();
        }
    }

    public static MessageEmbed transformationsShop(long userID){
        try {
            EmbedBuilder tsEmbed = new EmbedBuilder();
            tsEmbed.setTitle("Transformations Shop");
            tsEmbed.setDescription("Here you can see the list of all the transformations that you can buy");
            tsStatement.setString(1, Utils.checkRace(userID));
            ResultSet resultSet = tsStatement.executeQuery();
            while (resultSet.next())
                tsEmbed.addField(resultSet.getString(1), resultSet.getString(2) + "$ Lvl:" + resultSet.getString(3), true);
            return tsEmbed.build();
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return errorEmbed();
        }
    }

    public static MessageEmbed displayAttack(String itemName){
        try {
            EmbedBuilder diEmbed = new EmbedBuilder();
            daStatement.setString(1, itemName);
            daStatement.setString(2, itemName);
            ResultSet resultSet = daStatement.executeQuery();
            if(resultSet.next()){
                diEmbed.setTitle(resultSet.getString(1));
                diEmbed.setDescription("Short: `" + resultSet.getString(2)+"`");
                diEmbed.addField("Cost 💵", resultSet.getString(3) + "$", true);
                for (int i = 4; i <= resultSet.getMetaData().getColumnCount() - 4; i++) {
                    diEmbed.addField(resultSet.getMetaData().getColumnName(i), resultSet.getString(i), true);
                }
                diEmbed.addField("Counter Attack", Boolean.toString(resultSet.getBoolean(9)), true);
                diEmbed.addField("Attack Type", resultSet.getString(10), true);
                diEmbed.addField("Description", resultSet.getString(11), false);
                diEmbed.setImage(resultSet.getString(resultSet.getMetaData().getColumnCount()));
                return diEmbed.build();
            }else {
                return errorTextEmbed("The requested item doesn't exist...");
            }

        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return errorEmbed();
        }
    }

    public static MessageEmbed displayTransformation(String itemName){
        try {
            EmbedBuilder diEmbed = new EmbedBuilder();
            dtStatement.setString(1, itemName);
            dtStatement.setString(2, itemName);
            ResultSet resultSet = dtStatement.executeQuery();
            if(resultSet.next()){
                diEmbed.setTitle(resultSet.getString(1));
                diEmbed.setDescription("Short: `" + resultSet.getString(2)+"`");
                diEmbed.addField("Cost 💵", resultSet.getString(3) + "$", true);
                for (int i = 4; i <= resultSet.getMetaData().getColumnCount() - 2; i++) {
                    diEmbed.addField(resultSet.getMetaData().getColumnName(i), resultSet.getString(i), true);
                }
                diEmbed.addField("Description", resultSet.getString(9), false);
                diEmbed.setImage(resultSet.getString(resultSet.getMetaData().getColumnCount()));
                return diEmbed.build();
            }else {
                return errorTextEmbed("The requested item doesn't exist...");
            }

        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return errorEmbed();
        }
    }
}
