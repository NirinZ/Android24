package com.SharxNZ.GameFunctions;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Levels extends ListenerAdapter {

    // long[0] = xp, long[1] = timestamp
    public static final HashMap<String, long[]> xpMap = new HashMap<>();
    public static final Set<String> voiceSet = new HashSet<>();
    private final Random rand = new Random();

    public Levels() throws SQLException {
        updateDatabase();
        voiceXP();
    }

    private short giveXP(){
        return (short) rand.nextInt(21);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent guildMessage) {
        if (!guildMessage.getAuthor().isBot()) { //If not bot
            String guildID, userID;
            long[] xpAndTime = new long[2];
            guildID = guildMessage.getGuild().getId();
            userID = guildMessage.getAuthor().getId();
            String key = guildID + "#" + userID;
            if (xpMap.containsKey(key)) { // If exists in the database
                long newDate = new Date().getTime();
                if (newDate - xpMap.get(key)[1] > 20000) { // If 20 seconds passed //20000
                    xpAndTime[0] = xpMap.get(key)[0] + giveXP(); // Give random XP from 0 to 20
                    System.out.println();
                    xpAndTime[1] = newDate;
                    xpMap.replace(key, xpAndTime);
                    //guildMessage.getChannel().sendMessage("XP UP").queue();
                }
            } else {
                xpMap.put(key, new long[]{(long) giveXP(), new Date().getTime()}); // Adds him to the database with XP
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent guildVoiceJoin){
        voiceSet.add(guildVoiceJoin.getGuild().getId() + "#" +
                guildVoiceJoin.getMember().getId());
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent guildVoiceLeaveEvent) {
        voiceSet.remove(guildVoiceLeaveEvent.getGuild().getId() + "#" +
                guildVoiceLeaveEvent.getMember().getId());
    }

    private void voiceXP(){
        Timer timer = new Timer();
        long[] xpAndTime = new long[2];
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Set<String> tempSet = new HashSet<>(voiceSet);
                for (String key : tempSet) {
                    if (xpMap.containsKey(key)) {
                        xpAndTime[0] = xpMap.get(key)[0] + giveXP();
                        xpAndTime[1] = xpMap.get(key)[1];
                        xpMap.replace(key, xpAndTime);
                        //System.out.println(guildAndUserIds[1] + " got XP from voice");
                    } else {
                        xpMap.put(key, new long[]{(long) giveXP(), 0});
                    }
                }
            }
        };
        timer.scheduleAtFixedRate( timerTask, 0, 120000); // Do the check every 120 seconds //120000
    }

    private void updateDatabase() throws SQLException {
        final Statement sqlStatement = Android24.getConnection().createStatement();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ArrayList<String> sql = new ArrayList<>();
                long totalXP, zeni;
                int lvlShouldBe, currentLvl, powerPoints;
                String guildID, userID;
                HashMap<String, long[]> tempMap = (HashMap<String, long[]>) xpMap.clone();
                for (String key : tempMap.keySet()) {
                    guildID = key.split("#")[0];
                    userID = key.split("#")[1];
                    try {
                        ResultSet resultSet = sqlStatement.executeQuery(
                                "SELECT XP, Level FROM `" + guildID + "`.users_data " +
                                        "where UserID=" + userID + ";");

                        if (resultSet.next()) {
                            totalXP = tempMap.get(key)[0] + resultSet.getLong(1);
                            zeni = tempMap.get(key)[0];
                            currentLvl = resultSet.getShort(2);
                            lvlShouldBe = (int) Math.floor(Math.pow(totalXP, Android24.difficulty));
                            powerPoints = 4 * (lvlShouldBe - currentLvl);

                            Android24.jda.getTextChannelById(Android24.debugChannelID).sendMessage("Your XP: " + tempMap.get(key)[0]).queue();

                            sql.add("UPDATE `" + guildID + "`.`users_data` SET" +
                                    " `XP` = " + totalXP +
                                    ", `Zeni` = `Zeni` + " + zeni +
                                    ", `Level` = " + lvlShouldBe +
                                    " WHERE `UserID` = '" + userID + "';");

                            sql.add("UPDATE `" + guildID + "`.`users_power` SET" +
                                    " `PowerPoints` = `PowerPoints` + " + powerPoints +
                                    " WHERE `UserID` = '" + userID + "';");

                            resultSet.close();
                        } else {
                            sql.add("INSERT INTO `" + guildID + "`.`users_data` (`UserID`, `UserName`) VALUES (" + userID + ", '" +
                                    Android24.jda.retrieveUserById(userID).complete().getAsTag().replace("'", "''") + "');");
                            System.out.println(sql);
                        }
                        for (String query: sql) {
                            sqlStatement.executeUpdate(query);
                        }
                    } catch (SQLException throwables) {
                        for (String query: sql) {
                            System.out.println("The query:\n" + query);
                            Android24.jda.getTextChannelById(Android24.debugChannelID).sendMessage(query).queue();
                        }
                        Android24.logError(throwables);
                        throwables.printStackTrace();
                    }
                }
                xpMap.clear();
            }
        };
        timer.scheduleAtFixedRate( timerTask, 60000, 60000); // Do the check every 60 seconds //60000
    }
}


