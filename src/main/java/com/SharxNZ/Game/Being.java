package com.SharxNZ.Game;

import com.SharxNZ.Android24;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Utilities.DoublyCircularLinkedList;
import com.SharxNZ.Utilities.Stat;
import com.SharxNZ.Utilities.Utils;

import javax.naming.NameNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class Being {

    protected long userID;
    protected String race;
    protected Stat<Integer> zeni = new Stat<>(0);
    protected Stat<Integer> level = new Stat<>(0);
    protected Stat<Integer> powerPoints = new Stat<>(0);
    protected Stat<Integer> health = new Stat<>(0);
    protected Stat<Integer> ki = new Stat<>(0);
    protected Stat<Integer> strikeAttack = new Stat<>(0);
    protected Stat<Integer> kiAttack = new Stat<>(0);
    protected Stat<Integer> defence = new Stat<>(0);
    protected Stat<Integer> speed = new Stat<>(0);
    protected boolean inUse;

    protected List<Stat<Integer>> powerStats = List.of(health, ki, strikeAttack,
            kiAttack, defence, speed);

    private static final HashMap<Long, Being> beings = new HashMap<>();
    protected static PreparedStatement getBeingStatement;
    protected static PreparedStatement saveBeingStatement;
    protected static PreparedStatement getAttacks;
    protected static PreparedStatement getTransformations;

    static {
        Utils.garbageCollector(beings);
        try {
            getBeingStatement = Android24.getConnection().prepareStatement(
                    "SELECT `Race`, `XP`, `Zeni`,`PowerPoints`, `Health`, `Ki`, `StrikeAttack`, `KiAttack`, `Defence`, `Speed`" +
                            " FROM `android24`.users_data where `UserID` = ?;");
            saveBeingStatement = Android24.getConnection().prepareStatement(
                    "UPDATE `android24`.`users_data` SET `PowerPoints` = ?, `Health` = ?, `Ki` = ?, `StrikeAttack` = ?, `KiAttack` = ?, `Defence` = ?, `Speed` =?" +
                            " WHERE `UserID` = ?;");
            getAttacks = Android24.getConnection().prepareStatement("""
                    SELECT
                        AttackName
                    FROM
                        android24.users_attacks
                            JOIN
                        android24.attacks USING (AttackAbbreviated)
                        Where UserID = ?;
                    """);
            getTransformations = Android24.getConnection().prepareStatement("""
                    SELECT
                        TransformationName
                    FROM
                        android24.users_transformations
                            JOIN
                        android24.transformations USING (TransformationAbbreviated)
                        Where UserID = ?;
                    """);
        } catch (Exception throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    protected Being(long userID) {
        try {
            getBeingStatement.setLong(1, userID);
            ResultSet resultSet = getBeingStatement.executeQuery();
            this.userID = userID;
            if (!resultSet.next())
                return;
            // resultSet.getMetaData().getColumnCount()
            this.race = resultSet.getString(1);
            this.level.set(Level.calculateLevel(resultSet.getInt(2)));
            this.zeni.set(resultSet.getInt(3));
            this.powerPoints.set(resultSet.getInt(4));
            this.health.set(resultSet.getInt(5));
            this.ki.set(resultSet.getInt(6));
            this.strikeAttack.set(resultSet.getInt(7));
            this.kiAttack.set(resultSet.getInt(8));
            this.defence.set(resultSet.getInt(9));
            this.speed.set(resultSet.getInt(10));

            this.inUse = true;
            resultSet.close();
        } catch (SQLException throwables) {
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
            beings.put(userID, being);
        }
        return being;
    }

    public void save() {
        try {
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
        DoublyCircularLinkedList<DisplayAttack> attacks = new DoublyCircularLinkedList<>();
        getAttacks.setLong(1, userID);
        ResultSet resultSet = getAttacks.executeQuery();
        while (resultSet.next())
            attacks.add(new DisplayAttack(resultSet.getString(1)));
        resultSet.close();
        return attacks;
    }

    public DoublyCircularLinkedList<DisplayTransformation> getDisplayTransformations() throws SQLException, NameNotFoundException {
        DoublyCircularLinkedList<DisplayTransformation> transformations = new DoublyCircularLinkedList<>();
        getTransformations.setLong(1, userID);
        ResultSet resultSet = getTransformations.executeQuery();
        while (resultSet.next())
            transformations.add(new DisplayTransformation(resultSet.getString(1)));
        resultSet.close();
        return transformations;
    }

    public static DoublyCircularLinkedList<DisplayAttack> getDisplayAttacks(long userID) throws SQLException, NameNotFoundException {
        DoublyCircularLinkedList<DisplayAttack> attacks = new DoublyCircularLinkedList<>();
        getAttacks.setLong(1, userID);
        ResultSet resultSet = getAttacks.executeQuery();
        while (resultSet.next())
            attacks.add(new DisplayAttack(resultSet.getString(1)));
        resultSet.close();
        return attacks;
    }

    public static DoublyCircularLinkedList<DisplayTransformation> getDisplayTransformations(long userID) throws SQLException, NameNotFoundException {
        DoublyCircularLinkedList<DisplayTransformation> transformations = new DoublyCircularLinkedList<>();
        getTransformations.setLong(1, userID);
        ResultSet resultSet = getTransformations.executeQuery();
        while (resultSet.next())
            transformations.add(new DisplayTransformation(resultSet.getString(1)));
        resultSet.close();
        return transformations;
    }

    public Stats getStats() {
        return new Stats(userID);
    }

    public long getUserID() {
        return this.userID;
    }

    public String getRace() {
        return this.race;
    }

    public int getLevel() {
        return this.level.get();
    }

    public int getZeni() {
        return this.zeni.get();
    }

    public int getPowerPoints() {
        return this.powerPoints.get();
    }

    public int getHealth() {
        return this.health.get();
    }

    public int getKi() {
        return this.ki.get();
    }

    public int getStrikeAttack() {
        return this.strikeAttack.get();
    }

    public int getKiAttack() {
        return this.kiAttack.get();
    }

    public int getDefence() {
        return this.defence.get();
    }

    public int getSpeed() {
        return this.speed.get();
    }

    public boolean getInUse() {
        return this.inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }


    static void setBeing(String guidID, String userID, String type) throws SQLException {
        Statement sqlStatement = Android24.getConnection().createStatement();
        String query = "INSERT INTO `" + guidID + "`.`users_power` (`UserID`, `Type`) VALUES ('" + userID + "', '" + type + "');";
        sqlStatement.execute(query);
        sqlStatement.close();
    }

}
