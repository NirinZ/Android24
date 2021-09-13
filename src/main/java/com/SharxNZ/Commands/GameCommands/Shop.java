package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Utilities.Embeds;
import com.SharxNZ.Utilities.PreparedSql;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Shop {

    private static PreparedStatement buyAttack;
    private static PreparedStatement buyTransformation;

    public static void Shop(){
        Android24.addCommand(new CommandData("shop", "All the operations that you can do in the Shop")
                .addSubcommands(new SubcommandData("view", "View the items in the Shop")
                        .addOptions(new OptionData(OptionType.STRING, "type", "The type of the Shop you want to view")
                                .addChoice("Special Attacks", "Special Attacks")
                                .addChoice("Transformations", "Transformations")
                                //.addChoice("Others", "Others")
                                .setRequired(true))
                        .addOptions(new OptionData(OptionType.STRING, "item", "The name of the item you want to expend (Can be the abbreviated name)")))
                .addSubcommands(new SubcommandData("buy", "Buy items from the Shop")
                        .addOptions(new OptionData(OptionType.STRING, "item", "The name of the item you want to buy (Can be the abbreviated name)")
                                .setRequired(true))));
        try {
            buyAttack = Android24.getConnection().prepareStatement("SELECT \n" +
                    "    IF('Already owned' NOT IN (SELECT \n" +
                    "                'Already owned' AS 'Check if exist'\n" +
                    "            FROM\n" +
                    "                android24.users_attacks\n" +
                    "            WHERE\n" +
                    "                UserID = ?\n" +
                    "                    AND AttackAbbreviated = (SELECT \n" +
                    "                        AttackAbbreviated\n" +
                    "                    FROM\n" +
                    "                        android24.attacks\n" +
                    "                    WHERE\n" +
                    "                        AttackAbbreviated = ?\n" +
                    "                            OR AttackName = ?)),\n" +
                    "        IF(ForcedRace IS NULL OR Race = ForcedRace,\n" +
                    "            IF(POWER(XP, "+Android24.difficulty+") >= MinimalLevel,\n" +
                    "                IF(Zeni >= Cost, 'Ok', 'Under budget'),\n" +
                    "                'Under Level'),\n" +
                    "            'Race Limited'),\n" +
                    "        'Already owned') AS Result\n" +
                    "FROM\n" +
                    "    android24.users_data AS u,\n" +
                    "    android24.shop AS s\n" +
                    "        JOIN\n" +
                    "    android24.attacks AS a ON a.AttackName = s.Name\n" +
                    "WHERE\n" +
                    "    UserID = ?\n" +
                    "        AND (AttackName = ?\n" +
                    "        OR AttackAbbreviated = ?);");
            buyTransformation = Android24.getConnection().prepareStatement("SELECT \n" +
                    "    IF('Already owned' NOT IN (SELECT \n" +
                    "                'Already owned' AS 'Check if exist'\n" +
                    "            FROM\n" +
                    "                android24.users_transformations\n" +
                    "            WHERE\n" +
                    "                UserID = ?\n" +
                    "                    AND TransformationAbbreviated = (SELECT \n" +
                    "                        TransformationAbbreviated\n" +
                    "                    FROM\n" +
                    "                        android24.transformations\n" +
                    "                    WHERE\n" +
                    "                        TransformationAbbreviated = ?\n" +
                    "                            OR TransformationName = ?)),\n" +
                    "        IF(ForcedRace IS NULL OR Race = ForcedRace,\n" +
                    "            IF(POWER(XP, "+Android24.difficulty+") >= MinimalLevel,\n" +
                    "                IF(Zeni >= Cost, 'Ok', 'Under budget'),\n" +
                    "                'Under Level'),\n" +
                    "            'Race Limited'),\n" +
                    "        'Already owned') AS Result\n" +
                    "FROM\n" +
                    "    android24.users_data AS u,\n" +
                    "    android24.shop AS s\n" +
                    "        JOIN\n" +
                    "    android24.transformations AS a ON a.TransformationName = s.Name\n" +
                    "WHERE\n" +
                    "    UserID = ?\n" +
                    "        AND (TransformationName = ?\n" +
                    "        OR TransformationAbbreviated = ?);");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public static MessageEmbed shopView(String type, String item, long userID) {
        if (item == null) {
            switch (type) {
                case "Special Attacks" -> {
                    return Embeds.attacksShop(userID);
                }
                case "Transformations" -> {
                    return Embeds.transformationsShop(userID);
                }
            }
        } else {
            switch (type) {
                case "Special Attacks" -> {
                    return Embeds.displayAttack(item);
                }
                case "Transformations" -> {
                    return Embeds.displayTransformation(item);
                }
            }
        }
        return Embeds.errorEmbed();
    }

    public static MessageEmbed shopBuy(String item, long userID){
        try {
            //Check if attack
            buyAttack.setLong(1, userID);
            buyAttack.setString(2, item);
            buyAttack.setString(3, item);
            buyAttack.setLong(4, userID);
            buyAttack.setString(5, item);
            buyAttack.setString(6, item);
            ResultSet resultSet = buyAttack.executeQuery();
            if(resultSet.next()){
                if(resultSet.getString(1).equals("Ok")){
                    return purchase("Attack", item, userID);
                } else
                    return Embeds.errorTextEmbed(resultSet.getString(1));
            }else{
                //Check if transformations
                buyTransformation.setLong(1, userID);
                buyTransformation.setString(2, item);
                buyTransformation.setString(3, item);
                buyTransformation.setLong(4, userID);
                buyTransformation.setString(5, item);
                buyTransformation.setString(6, item);
                resultSet = buyTransformation.executeQuery();
                if(resultSet.next()){
                    if(resultSet.getString(1).equals("Ok") /* && Check structure */ )
                        return purchase("Transformation", item, userID);
                     else
                        return Embeds.errorTextEmbed(resultSet.getString(1));
                }
                else{
                    return Embeds.errorTextEmbed("The requested item doesn't exist...");
                }
            }


        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return Embeds.errorEmbed();
        }
    }

    private static MessageEmbed purchase(String type, String item, long userID){
        String purchaseSql = "INSERT INTO android24.users_#s (UserID, $Abbreviated) " +
                "VALUES (?, (select $Abbreviated from android24.#s where $Abbreviated = ? or $Name = ?));";
        PreparedSql purchaseP = new PreparedSql();
        purchaseP.setSql(purchaseSql);
        purchaseP.setFormatAll(type);
        purchaseP.stringChange(Long.toString(userID));
        purchaseP.stringChange(item);
        purchaseP.stringChange(item);

        PreparedSql subtractMoney = new PreparedSql();
        String subtractMoneySql = "update android24.users_data set Zeni = Zeni - (SELECT Cost FROM android24.shop " +
                "join android24.#s on $Name = Name " +
                "where $Abbreviated = ? or Name = ?) where UserID = ?;";
        subtractMoney.setSql(subtractMoneySql);
        subtractMoney.setFormatAll(type);
        subtractMoney.stringChange(item);
        subtractMoney.stringChange(item);
        subtractMoney.stringChange(Long.toString(userID));

        try {
            Android24.getConnection().createStatement().executeUpdate(subtractMoney.toString());
            Android24.getConnection().createStatement().executeUpdate(purchaseP.toString());
            return Embeds.successTextEmbed("The item has been purchased successfully!");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return Embeds.errorEmbed();
        }

        /*max Storey
        SELECT Storey FROM android24.users_transformations
join android24.transformations using(TransformationAbbreviated)
join android24.shop on TransformationName = Name
where UserID = 739532349280354404
order by Storey
;*/
    }
}

