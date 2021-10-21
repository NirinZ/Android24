package com.SharxNZ.Commands.GameCommands;


import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Utilities.Stat;
import com.SharxNZ.Utilities.Graphics;
import com.SharxNZ.Utilities.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PowerPoints extends Being {

    private byte pointer;

    protected Stat<Integer> health = new Stat<>();
    protected Stat<Integer> ki = new Stat<>();
    protected Stat<Integer> strikeAttack = new Stat<>();
    protected Stat<Integer> kiAttack = new Stat<>();
    protected Stat<Integer> defence = new Stat<>();
    protected Stat<Integer> speed = new Stat<>();

    public String imageUrl = "https://www.pngjoy.com/pngm/135/2736064_warning-symbol-error-png-transparent-png.png";

    protected List<Stat<Integer>> powerStats = List.of(health, ki, strikeAttack,
            kiAttack, defence, speed);

    private static final HashMap<Long, PowerPoints> ppoints = new HashMap<>();

    static{
        Utils.garbageCollector(ppoints);
    }

    private PowerPoints(long userID){
        super(userID);
        for(int i = 0; i < 6; i++){
            this.powerStats.get(i).set(0);
        }
    }

    public static @NotNull PowerPoints getPowerPoints(long userID){
        PowerPoints powerPoints;
        if(ppoints.containsKey(userID)){
            powerPoints = ppoints.get(userID);
            powerPoints.setInUse(true);
        }
        else{
            powerPoints = new PowerPoints(userID);
            ppoints.put(userID, powerPoints);
        }
        return powerPoints;
    }

    public static @NotNull MessageEmbed getPowerPointsEmbed(PowerPoints powerPoints, boolean ephemeral, boolean image){
        AtomicReference<String> imageUrl = new AtomicReference<>();
        Thread thread = null;
        if (ephemeral && image) {
             thread = new Thread("thread") {
                public void run() {
                    Utils.getImageUrl(Graphics.statsImage(powerPoints), imageUrl);
                }
            };
            thread.start();
        }
        else if(ephemeral){
            imageUrl.set(powerPoints.imageUrl);
        }
        else{
            imageUrl.set("attachment://png.png");
        }

        char[] charPointer = new char[6];
        charPointer[powerPoints.getPointer()] = '↘';

        String[] changeHighlight = new String[6];
        for (int i = 0; i < changeHighlight.length; i++) {
            if (powerPoints.powerStats.get(i).get() > 0){
                changeHighlight[i] = "```yaml\n";
            }
            else{
                changeHighlight[i] = "```\n";
            }
        }

        String[] strings = new String[]{
                changeHighlight[0] + "Health: " + powerPoints.getHealth() + "```",
                changeHighlight[1] + "Ki: " + powerPoints.getKi() + "```",
                changeHighlight[2] + "Strike Attack: " + powerPoints.getStrikeAttack() + "```",
                changeHighlight[3] + "Ki Attack: " + powerPoints.getKiAttack() + "```",
                changeHighlight[4] + "Defence: " + powerPoints.getDefence() + "```",
                changeHighlight[5] + "Speed: " + powerPoints.getSpeed() + "```",
        };


        EmbedBuilder ppEmbed = new EmbedBuilder();
        ppEmbed.setTitle("Your power points:");
        ppEmbed.setDescription("here you can edit your power points");
        ppEmbed.addBlankField(true);
        ppEmbed.addField("Available Power Points: "+ powerPoints.getPowerPoints(), "", false);
        ppEmbed.addField(String.valueOf(charPointer[0]), strings[0], true);
        ppEmbed.addField(String.valueOf(charPointer[1]), strings[1], true);
        ppEmbed.addField(String.valueOf(charPointer[2]), strings[2], true);
        ppEmbed.addField(String.valueOf(charPointer[3]), strings[3], true);
        ppEmbed.addField(String.valueOf(charPointer[4]), strings[4], true);
        ppEmbed.addField(String.valueOf(charPointer[5]), strings[5], true);
        try {
            User user = Android24.jda.retrieveUserById(powerPoints.getUserID()).submit().get();
            ppEmbed.setFooter("The stats of: " + user.getName(), user.getAvatarUrl());
            if (ephemeral && image) {
                thread.join();
                powerPoints.imageUrl = imageUrl.get();
            }

        } catch (Exception e) {
            Android24.logError(e);
        }
        ppEmbed.setImage(imageUrl.get());
        return ppEmbed.build();
    }

    public static @NotNull MessageEmbed getPowerPointsEmbed(PowerPoints powerPoints, boolean ephemeral) {
        AtomicReference<String> imageUrl = new AtomicReference<>();
        Thread thread = null;
        if (ephemeral) {
            thread = new Thread("thread") {
                public void run() {
                    Utils.getImageUrl(Graphics.statsImage(powerPoints), imageUrl);
                }
            };
            thread.start();
        }
        else{
            imageUrl.set("attachment://png.png");
        }

        EmbedBuilder ppEmbed = new EmbedBuilder();
        ppEmbed.setColor(Color.red);
        ppEmbed.setTitle("Your power points:");
        ppEmbed.setDescription("here you can edit your power points");
        ppEmbed.addField("", """
                ```diff
                -You sure you want to save?
                -You will not be able to change the stats after that.
                -If you wish to cancel you can just ignore this message,
                -or call the function again

                (Press save again to save)```""", false);
        ppEmbed.addBlankField(true);
        ppEmbed.addField("Available Power Points: "+ powerPoints.getPowerPoints(), "", false);
        ppEmbed.addField("", "```Health: " + powerPoints.getHealth() + "```", true);
        ppEmbed.addField("", "```Ki: " + powerPoints.getKi() + "```", true);
        ppEmbed.addField("", "```Strike Attack: " + powerPoints.getStrikeAttack() + "```", true);
        ppEmbed.addField("", "```Ki Attack: " + powerPoints.getKiAttack() + "```", true);
        ppEmbed.addField("", "```Defence: " + powerPoints.getDefence() + "```", true);
        ppEmbed.addField("", "```Speed: " + powerPoints.getSpeed() + "```", true);
        try {
            User user = Android24.jda.retrieveUserById(powerPoints.getUserID()).submit().get();
            ppEmbed.setFooter("The stats of: " + user.getName(), user.getAvatarUrl());
            if (ephemeral)
                thread.join();

        } catch (Exception e) {
            Android24.logError(e);
        }

        ppEmbed.setImage(imageUrl.get());
        return ppEmbed.build();
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
        if(powerPoints > 0){
            powerPoints--;
            powerStats.get(pointer).set(powerStats.get(pointer).get() + 1);
        }
    }

    public void subtractValue(){
        if(this.powerStats.get(pointer).get() > 0){
            powerStats.get(pointer).set(powerStats.get(pointer).get() - 1);
            powerPoints++;
        }
    }

    public Being toBeing(){
        super.health = this.getHealth();
        super.ki = this.getKi();
        super.strikeAttack = this.getStrikeAttack();
        super.kiAttack = this.getKiAttack();
        super.defence = this.getDefence();
        super.speed = this.getSpeed();

        return this;
    }

    @Override
    public void save(){
        try (
                Connection con = Android24.getConnection();
                PreparedStatement saveBeingStatement = con.prepareStatement(saveBeingStatementSql)
        ){

            saveBeingStatement.setLong(1, getPowerPoints());
            saveBeingStatement.setLong(2, getHealth());
            saveBeingStatement.setLong(3, getKi());
            saveBeingStatement.setLong(4, getStrikeAttack());
            saveBeingStatement.setLong(5, getKiAttack());
            saveBeingStatement.setLong(6, getDefence());
            saveBeingStatement.setLong(7, getSpeed());
            saveBeingStatement.setLong(8, getUserID());

            saveBeingStatement.executeUpdate();

        } catch (SQLException throwables) {
            Android24.logError(throwables);
        }
    }

    public int getPointer(){
        return pointer;
    }

    @Override
    public long getHealth(){
        return this.health.get() + super.getHealth();
    }

    @Override
    public long getKi(){
        return this.ki.get() + super.getKi();
    }

    @Override
    public long getStrikeAttack(){
        return this.strikeAttack.get() + super.getStrikeAttack();
    }

    @Override
    public long getKiAttack(){
        return this.kiAttack.get() + super.getKiAttack();
    }

    @Override
    public long getDefence(){
        return this.defence.get() + super.getDefence();
    }

    @Override
    public long getSpeed(){
        return this.speed.get() + super.getSpeed();
    }

    public static HashMap<Long, PowerPoints> getPPoints(){return ppoints;}

}
