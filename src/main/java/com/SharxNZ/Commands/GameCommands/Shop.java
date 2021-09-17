package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.DisplayAttack;
import com.SharxNZ.Game.DisplayTransformation;
import com.SharxNZ.Utilities.DoublyCircularLinkedList;
import com.SharxNZ.Utilities.Embeds;
import com.SharxNZ.Utilities.PreparedSql;
import com.SharxNZ.Utilities.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import javax.naming.NameNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Shop {

    private static PreparedStatement buyAttack;
    private static PreparedStatement buyTransformation;
    private static PreparedStatement checkType;
    public static PreparedStatement asStatement;
    public static PreparedStatement tsStatement;

    public static void Shop(){
        try {
            checkType = Android24.getConnection().prepareStatement("""
                    SELECT
                        IF(? IN (SELECT
                                    Name
                                FROM
                                    android24.attacks
                                        JOIN
                                    android24.shop ON Name = AttackName
                                    UNION
                                    SELECT
                                    AttackAbbreviated
                                FROM
                                    android24.attacks
                                        JOIN
                                    android24.shop ON Name = AttackName),
                            'Attack',
                            IF(? IN (SELECT
                                    Name
                                FROM
                                    android24.transformations
                                        JOIN
                                    android24.shop ON Name = TransformationName
                                    UNION
                                    SELECT
                                    TransformationAbbreviated
                                FROM
                                    android24.transformations
                                        JOIN
                                    android24.shop ON Name = TransformationName), 'Transformation', Null)) AS 'Type'
                    """);
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
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public static MessageEmbed shopView(String item) {
        try {
            checkType.setString(1, item);
            checkType.setString(2, item);
            ResultSet resultSet = checkType.executeQuery();
            if (!resultSet.next())
                return Embeds.errorEmbed();
            switch (resultSet.getString(1)) {
                case "Attack" -> {
                    return displayAttack(item);
                }
                case "Transformation" -> {
                    return displayTransformation(item);
                }
                default -> {
                    return Embeds.errorTextEmbed("The requested item doesn't exist...");
                }
            }
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return Embeds.errorEmbed();
        }
    }

    public static MessageEmbed tryToBuy(String item, long userID){
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

    public static MessageEmbed displayAttack(String name) {
        try {
            return new DisplayAttack(name).getEmbed().build();
        }catch (NameNotFoundException exception){
            return Embeds.errorTextEmbed("The requested item doesn't exist...");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return Embeds.errorEmbed();
        }
    }

    public static MessageEmbed displayTransformation(String name){
        try {
            return new DisplayTransformation(name).getEmbed().build();
        }catch (NameNotFoundException exception){
            return Embeds.errorTextEmbed("The requested item doesn't exist...");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return Embeds.errorEmbed();
        }
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
            return Embeds.errorEmbed();
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
            return Embeds.errorEmbed();
        }
    }

    public static DoublyCircularLinkedList<DisplayAttack> getAttacksShop(long userID){
        try {
            DoublyCircularLinkedList<DisplayAttack> attacks = new DoublyCircularLinkedList<>();
            asStatement.setString(1, Utils.checkRace(userID));
            ResultSet resultSet = asStatement.executeQuery();
            while (resultSet.next())
                attacks.add(new DisplayAttack(resultSet.getString(1)));
            return attacks;
        } catch (SQLException | NameNotFoundException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }

    public static DoublyCircularLinkedList<DisplayTransformation> getTransformationsShop(long userID){
        try {
            DoublyCircularLinkedList<DisplayTransformation> transformations = new DoublyCircularLinkedList<>();
            tsStatement.setString(1, Utils.checkRace(userID));
            ResultSet resultSet = tsStatement.executeQuery();
            while (resultSet.next())
                transformations.add(new DisplayTransformation(resultSet.getString(1)));
            return transformations;
        } catch (SQLException | NameNotFoundException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }
}

