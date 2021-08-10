package com.SharxNZ.Commands.GameCommands;


import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Stat;
import com.SharxNZ.Utilities.Utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class PowerPoints extends Being {

    private short pointer;

    protected Stat<Integer> health = new Stat<>();
    protected Stat<Integer> ki = new Stat<>();
    protected Stat<Integer> strikeAttack = new Stat<>();
    protected Stat<Integer> kiAttack = new Stat<>();
    protected Stat<Integer> defence = new Stat<>();
    protected Stat<Integer> speed = new Stat<>();
    protected Stat<Integer> level = new Stat<>();

    public String imageUrl = "https://www.pngjoy.com/pngm/135/2736064_warning-symbol-error-png-transparent-png.png";

    protected List<Stat<Integer>> powerStats = List.of(health, ki, strikeAttack,
            kiAttack, defence, speed);

    private static final HashMap<String, PowerPoints> ppoints = new HashMap<>();

    static{
        Utils.garbageCollector(ppoints);
    }

    private PowerPoints(String guildID, String userID){
        super(guildID, userID);
        for(int i = 0; i < 6; i++){
            this.powerStats.get(i).set(0);
        }
    }

    public static PowerPoints getPowerPoints(String guildID, String userID){
        String key = guildID+"#"+userID;
        PowerPoints powerPoints;
        if(ppoints.containsKey(key)){
            powerPoints = ppoints.get(key);
            powerPoints.inUse = true;
        }
        else{
            powerPoints = new PowerPoints(guildID, userID);
            ppoints.put(key, powerPoints);
        }
        return powerPoints;
    }

    public void nextValue(){
        pointer++;
        if(pointer > 5){
            pointer = 0;
        }
    }

public void previousValue(){
        pointer--;
        if(pointer < 0){
            pointer = 5;
        }
    }

    public void addValue(){
        if(powerPoints.get() > 0){
            powerPoints.set(powerPoints.get() - 1);
            powerStats.get(pointer).set(powerStats.get(pointer).get() + 1);
        }
    }

    public void subtractValue(){
        if(this.powerStats.get(pointer).get() > 0){
            powerStats.get(pointer).set(powerStats.get(pointer).get() - 1);
            powerPoints.set(powerPoints.get() + 1);
        }
    }

    public Being toBeing(){
        super.health.set(this.getHealth());
        super.ki.set(this.getKi());
        super.strikeAttack.set(this.getStrikeAttack());
        super.strikeAttack.set(this.getKiAttack());
        super.defence.set(this.getDefence());
        super.speed.set(this.getSpeed());

        return this;
    }

    @Override
    public void save(){
        String sql = "UPDATE `" + getGuildID() + "`.`users_power` SET" +
                " " +
                "`PowerPoints` = " + getPowerPoints() +
                ", `Health` = " + getHealth() +
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
            Android24.jda.getTextChannelById(Android24.debugChannelID).sendMessage(sql).queue();
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public int getPointer(){
        return pointer;
    }

    @Override
    public int getHealth(){
        return this.health.get() + super.health.get();
    }

    @Override
    public int getKi(){
        return this.ki.get() + super.ki.get();
    }

    @Override
    public int getStrikeAttack(){
        return this.strikeAttack.get() + super.strikeAttack.get();
    }

    @Override
    public int getKiAttack(){
        return this.kiAttack.get() + super.kiAttack.get();
    }

    @Override
    public int getDefence(){
        return this.defence.get() + super.defence.get();
    }

    @Override
    public int getSpeed(){
        return this.speed.get() + super.speed.get();
    }

    public static HashMap<String, PowerPoints> getPPoints(){return ppoints;}

}
