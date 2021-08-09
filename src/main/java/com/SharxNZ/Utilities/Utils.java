package com.SharxNZ.Utilities;
import com.SharxNZ.Commands.GameCommands.PowerPoints;
import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Race;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Utils{

    public static MessageEmbed savedEmbed(){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.green);
        embedBuilder.addField("All changes has been saved!", "", true);
        embedBuilder.setImage("https://user-images.githubusercontent.com/11019190/45695978-d59faa00-bb62-11e8-8f37-7b447356d237.png");
        return embedBuilder.build();
    }

    public static short getLevel(String guildID, String userID) throws SQLException {
        Statement sqlLevel = Android24.getStatement();
        ResultSet resultLvl = sqlLevel.executeQuery(
                "SELECT Level FROM `" + guildID + "`.users_data " +
                        "where UserID=" + userID + ";");
        if (resultLvl.next())
            return resultLvl.getShort(1);
        else
            return 0;
    }

    public static Race getRace(String guildID, String userID) throws SQLException {
        Statement sqlLevel = Android24.getStatement();
        ResultSet resultLvl = sqlLevel.executeQuery(
                "SELECT Race FROM `" + guildID + "`.users_power " +
                        "where UserID=" + userID + ";");
        if (resultLvl.next())
            return Race.valueOf(resultLvl.getString(1));
        else
            return Race.Saiyan;
    }

    public static short getPowerPoints(String guildID, String userID){
        try {
            Statement sqlLevel = Android24.getStatement();
        ResultSet resultLvl = sqlLevel.executeQuery(
                "SELECT PowerPoints FROM `" + guildID + "`.users_power " +
                        "where UserID=" + userID + ";");
        if (resultLvl.next())
            return resultLvl.getShort(1);
        else
            return 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static <T> boolean inUse(T instance){
        switch (instance.getClass().getSimpleName()){
            case "Being":
                Being being = (Being) instance;
                return being.getInUse();
            case "PowerPoints":
                PowerPoints powerPoints = (PowerPoints) instance;
                return powerPoints.getInUse();
            default:
                return false;
        }

        // In Java's next version:
        /*switch (instance){
            case Being b:
        }*/
    }

    public static <T> void setInUse(T instance) {
        switch (instance.getClass().getSimpleName()) {
            case "Being":
                Being being = (Being) instance;
                being.setInUse(false);
                break;
            case "PowerPoints":
                PowerPoints powerPoints = (PowerPoints) instance;
                powerPoints.setInUse(false);
                break;
        }
    }

    public static <T> void garbageCollector(HashMap<String, T> hashMap){
        Lock lock = new ReentrantLock();
        Timer timer = new Timer();
        final int[] i = {0};
        HashMap<String, T> tempMap = (HashMap<String, T>) hashMap.clone();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for(String key : tempMap.keySet()){
                    lock.lock();
                    if(inUse(tempMap.get(key))) {
                        setInUse(tempMap.get(key));
                        System.out.println(" === Prolonged === ");
                    }
                    else {
                        tempMap.remove(key);
                        System.out.println(" === deleted === ");
                    }
                    lock.unlock();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 30000, 30000);
    }
}
