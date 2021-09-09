package com.SharxNZ.Game;

import com.SharxNZ.Android24;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Utilities.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class Being {

    protected long userID;
    protected Race race;
    protected Stat<Integer> powerPoints = new Stat<>(0);
    protected Stat<Integer> health = new Stat<>(0);
    protected Stat<Integer> ki = new Stat<>(0);
    protected Stat<Integer> strikeAttack = new Stat<>(0);
    protected Stat<Integer> kiAttack = new Stat<>(0);
    protected Stat<Integer> defence = new Stat<>(0);
    protected Stat<Integer> speed = new Stat<>(0);
    protected Stat<Integer> level = new Stat<>(0);
    protected boolean inUse;

    protected List<Stat<Integer>> powerStats = List.of(health, ki, strikeAttack,
    kiAttack, defence, speed);

    private static final HashMap<Long, Being> beings = new HashMap<>();
    protected static PreparedStatement getBeingStatement;
    protected static PreparedStatement saveBeingStatement;

    static{
        Utils.garbageCollector(beings);
        try {
            getBeingStatement = Android24.getConnection().prepareStatement(
                    "SELECT `Race`, `XP`, `PowerPoints`, `Health`, `Ki`, `StrikeAttack`, `KiAttack`, `Defence`, `Speed`" +
                            " FROM `android24`.users_data where `UserID` = ?;");
            saveBeingStatement = Android24.getConnection().prepareStatement(
                    "UPDATE `android24`.`users_power` SET `PowerPoints` = ?, `Health` = ?, `Ki` = ?, `StrikeAttack` = ?, `KiAttack` = ?, `Defence` = ?, `Speed` =?" +
                            " WHERE `UserID` = ?;");
        } catch (Exception throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    protected Being(long userID){
        try {
            getBeingStatement.setLong(1, userID);
            ResultSet resultSet = getBeingStatement.executeQuery();
            this.userID = userID;
            if (resultSet.next()) {
                // resultSet.getMetaData().getColumnCount()
                this.race = Race.valueOf(resultSet.getString(1));
                this.level.set((int) Level.calculateLevel(resultSet.getInt(2)));
                this.powerPoints.set(resultSet.getInt(3));
                this.health.set(resultSet.getInt(4));
                this.ki.set(resultSet.getInt(5));
                this.strikeAttack.set(resultSet.getInt(6));
                this.kiAttack.set(resultSet.getInt(7));
                this.defence.set(resultSet.getInt(8));
                this.speed.set(resultSet.getInt(9));
            }
            else {
                this.race = Race.Saiyan;
            }
            this.inUse = true;
            resultSet.close();

        }catch (SQLException throwables) {
            Android24.jda.getTextChannelById(Android24.debugChannelID).sendMessage(throwables.toString()).queue();
            throwables.printStackTrace();
        }
    }

    public static Being getBeing(long userID){
        Being being;
        if(beings.containsKey(userID)){
            being = beings.get(userID);
            being.inUse = true;
        }
        else{
            being = new Being(userID);
            beings.put(userID, being);
        }
        return being;

    }

    public void save(){
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


    public long getUserID(){return this.userID;}

    public Race getRace(){return this.race;}

    public int getPowerPoints(){return this.powerPoints.get();}

    public int getHealth(){return this.health.get();}

    public int getKi(){return this.ki.get();}

    public int getStrikeAttack(){return this.strikeAttack.get();}

    public int getKiAttack(){return this.kiAttack.get();}

    public int getDefence(){return this.defence.get();}

    public int getSpeed(){return this.speed.get();}

    public int getLevel(){return this.level.get();}

    public boolean getInUse(){return this.inUse;}

    public void setInUse(boolean inUse){this.inUse = inUse;}


    static void setBeing(String guidID, String userID, String type) throws SQLException {
        Statement sqlStatement =  Android24.getConnection().createStatement();
        String query = "INSERT INTO `"+guidID+"`.`users_power` (`UserID`, `Type`) VALUES ('"+userID+"', '"+type+"');";
        sqlStatement.execute(query);
        sqlStatement.close();
    }

}
