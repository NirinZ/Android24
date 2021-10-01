package com.SharxNZ.Game;

import com.SharxNZ.Android24;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Utilities.DoublyCircularLinkedList;
import com.SharxNZ.Utilities.Utils;
import org.jetbrains.annotations.NotNull;

import javax.naming.NameNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Being { // לסדר את זה שלא יהיה סטט כי לא צריך.

    protected long userID;
    protected String name;
    protected String race;
    protected int zeni;
    protected int level;
    protected int powerPoints;
    protected int health;
    protected int ki;
    protected int strikeAttack;
    protected int kiAttack;
    protected int defence;
    protected int speed;
    protected Transformation transformation;

    protected boolean inUse;


    protected static final HashMap<Long, Being> beings = new HashMap<>();

    protected static String saveBeingStatementSql = """
            UPDATE `android24`.`users_data` SET `PowerPoints` = ?, `Health` = ?, `Ki` = ?, `StrikeAttack` = ?, `KiAttack` = ?,
            `Defence` = ?, `Speed` = ? WHERE `UserID` = ?
            """;

    protected static String getAttacksSql = """
            SELECT
                AttackName
            FROM
                android24.users_attacks
                    JOIN
                android24.attacks USING (AttackAbbreviated)
                Where UserID = ?;
            """;
    protected static String getTransformationsSql = """
            SELECT
                TransformationName
            FROM
                android24.users_transformations
                    JOIN
                android24.transformations USING (TransformationAbbreviated)
                Where UserID = ?;
            """;

    static {
        Utils.garbageCollector(beings);
    }

    protected Being(long userID) {
        try
                (Connection con = Android24.getConnection();
                 PreparedStatement getBeingStatement = con.prepareStatement("""
                              SELECT `Race`, `XP`, `Zeni`,`PowerPoints`, `Health`, `Ki`, `StrikeAttack`, `KiAttack`,
                              `Defence`, `Speed`, CurrentTransformation
                              FROM `android24`.users_data where `UserID` = ?;
                         """)) {

            getBeingStatement.setLong(1, userID);
            ResultSet resultSet = getBeingStatement.executeQuery();
            this.userID = userID;
            Android24.jda.retrieveUserById(userID).queue(user -> name = user.getName());
            if (!resultSet.next()) {
                return;
            }
            // resultSet.getMetaData().getColumnCount()
            this.race = resultSet.getString(1);
            this.level = Level.calculateLevel(resultSet.getInt(2));
            this.zeni = resultSet.getInt(3);
            this.powerPoints = resultSet.getInt(4);
            this.health = resultSet.getInt(5);
            this.ki = resultSet.getInt(6);
            this.strikeAttack = resultSet.getInt(7);
            this.kiAttack = resultSet.getInt(8);
            this.defence = resultSet.getInt(9);
            this.speed = resultSet.getInt(10);
            this.transformation = new Transformation(resultSet.getString(11));
            this.inUse = true;

            beings.put(userID, this);
        } catch (SQLException | NameNotFoundException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public static Being getBeing(long userID) {
        Being being;
        if (beings.containsKey(userID)) {
            being = beings.get(userID);
            being.inUse = true;
        } else {
            being = new Being(userID);
        }
        return being;
    }

    public void save() {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement saveBeingStatement = con.prepareStatement(saveBeingStatementSql)
        ) {

            saveBeingStatement.setInt(1, getPowerPoints());
            saveBeingStatement.setInt(2, getHealth());
            saveBeingStatement.setInt(3, getKi());
            saveBeingStatement.setInt(4, getStrikeAttack());
            saveBeingStatement.setInt(5, getKiAttack());
            saveBeingStatement.setInt(6, getDefence());
            saveBeingStatement.setInt(7, getSpeed());
            saveBeingStatement.setLong(8, userID);
            saveBeingStatement.executeUpdate();

        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public DoublyCircularLinkedList<DisplayAttack> getDisplayAttacks() throws SQLException, NameNotFoundException {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement getAttacks = con.prepareStatement(getAttacksSql)
        ) {
            DoublyCircularLinkedList<DisplayAttack> attacks = new DoublyCircularLinkedList<>();
            getAttacks.setLong(1, userID);
            ResultSet resultSet = getAttacks.executeQuery();
            while (resultSet.next())
                attacks.add(new DisplayAttack(resultSet.getString(1)));
            getAttacks.close();
            return attacks;
        }
    }

    public DoublyCircularLinkedList<DisplayTransformation> getDisplayTransformations() throws SQLException, NameNotFoundException {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement getTransformations = con.prepareStatement(getTransformationsSql)
        ) {
            DoublyCircularLinkedList<DisplayTransformation> transformations = new DoublyCircularLinkedList<>();
            getTransformations.setLong(1, userID);
            ResultSet resultSet = getTransformations.executeQuery();
            while (resultSet.next())
                transformations.add(new DisplayTransformation(resultSet.getString(1)));
            getTransformations.close();
            return transformations;
        }
    }

    public static DoublyCircularLinkedList<DisplayAttack> getDisplayAttacks(long userID) throws SQLException, NameNotFoundException {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement getAttacks = con.prepareStatement(getAttacksSql)
        ) {
            DoublyCircularLinkedList<DisplayAttack> attacks = new DoublyCircularLinkedList<>();
            getAttacks.setLong(1, userID);
            ResultSet resultSet = getAttacks.executeQuery();
            while (resultSet.next())
                attacks.add(new DisplayAttack(resultSet.getString(1)));
            getAttacks.close();
            return attacks;
        }
    }

    public static DoublyCircularLinkedList<DisplayTransformation> getDisplayTransformations(long userID) throws SQLException, NameNotFoundException {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement getTransformations = con.prepareStatement(getTransformationsSql)
        ) {
            DoublyCircularLinkedList<DisplayTransformation> transformations = new DoublyCircularLinkedList<>();
            getTransformations.setLong(1, userID);
            ResultSet resultSet = getTransformations.executeQuery();
            while (resultSet.next())
                transformations.add(new DisplayTransformation(resultSet.getString(1)));
            getTransformations.close();
            return transformations;
        }
    }

    public void setTransformation(@NotNull Transformation trans) {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement setTransformation = con.prepareStatement("UPDATE `android24`.`users_data` SET `CurrentTransformation` = ? WHERE (`UserID` = ?);")
        ) {

            setTransformation.setString(1, trans.getName());
            setTransformation.setLong(2, userID);
            setTransformation.executeUpdate();
            transformation = trans;
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public Stats getStats() {
        return new Stats(userID);
    }

    public long getUserID() {
        return this.userID;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return this.race;
    }

    public int getZeni() {
        return zeni;
    }

    public int getLevel() {
        return level;
    }

    public int getPowerPoints() {
        return powerPoints;
    }

    public int getHealth() {
        return health;
    }

    public int getKi() {
        return ki;
    }

    public int getStrikeAttack() {
        return strikeAttack;
    }

    public int getKiAttack() {
        return kiAttack;
    }

    public int getDefence() {
        return defence;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isInUse() {
        return inUse;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public boolean getInUse() {
        return this.inUse;
    }



    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }



}
