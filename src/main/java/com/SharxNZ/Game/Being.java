package com.SharxNZ.Game;

import com.SharxNZ.Android24;
import com.SharxNZ.Utilities.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class Being {

    protected String guildID, userID;
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

    private static final HashMap<String, Being> beings = new HashMap<>();

    static{
        Utils.garbageCollector(beings);
    }

    protected Being(String guildID, String userID){
        try {
            Statement statement = Android24.getConnection().createStatement();
            String sql = "SELECT up.*, Level " +
                    "FROM `" + guildID + "`.users_power up " +
                    "JOIN `" + guildID + "`.users_data ud " +
                    "on ud.UserID = up.UserID " +
                    "where ud.UserID=" + userID + ";";

            ResultSet resultSet = statement.executeQuery(sql);

            this.guildID = guildID;
            this.userID = userID;
            if (resultSet.next()) {
                // resultSet.getMetaData().getColumnCount()
                this.race = Race.valueOf(resultSet.getString(2));
                this.powerPoints.set(resultSet.getInt(3));
                this.health.set(resultSet.getInt(4));
                this.ki.set(resultSet.getInt(5));
                this.strikeAttack.set(resultSet.getInt(6));
                this.kiAttack.set(resultSet.getInt(7));
                this.defence.set(resultSet.getInt(8));
                this.speed.set(resultSet.getInt(9));
                this.level.set(resultSet.getInt(10));
            }
            else {
                this.race = Race.Saiyan;
            }
            this.inUse = true;
            resultSet.close();
            statement.close();

        }catch (SQLException throwables) {
            Android24.jda.getTextChannelById(Android24.debugChannelID).sendMessage(throwables.toString()).queue();
            throwables.printStackTrace();
        }
    }

    public static Being getBeing(String guildID, String userID){
        String key = guildID+"#"+userID;
        Being being;
        if(beings.containsKey(key)){
            being = beings.get(key);
            being.inUse = true;
        }
        else{
            being = new Being(guildID, userID);
            beings.put(key, being);
        }
        return being;

    }

    public void save(){
        String sql = "UPDATE `" + getGuildID() + "`.`users_power` SET" +
                " `Health` = " + getHealth() +
                ", `Ki` = " + getKi() +
                ", `StrikeAttack` = " + getStrikeAttack() +
                ", `KiAttack` = " + getKiAttack() +
                ", `Defence` = " + getDefence() +
                ", `Speed` = " + getSpeed() +
                " WHERE `UserID` = " + getUserID() + ";";
        try {
            Statement statement = Android24.getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public String getGuildID(){return this.guildID;}

    public String getUserID(){return this.userID;}

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
