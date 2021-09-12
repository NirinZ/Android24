package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Utilities.Graphics;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Stats {

    public static void Stats() {
        Android24.addCommand(new CommandData("get_power_points", "Display and let you edit your power points")
        .addOptions(new OptionData(OptionType.BOOLEAN, "display", "display your stats"))
        .addOptions(new OptionData(OptionType.BOOLEAN, "refresh",
                "Refreshes the image for every change. (Might by slow if display is false)")));
    }

    public static MessageEmbed getPowerPointsEmbed(PowerPoints powerPoints, boolean ephemeral, boolean image){
        AtomicReference<String> imageUrl = new AtomicReference<>();
        Thread thread = null;
        if (ephemeral && image) {
             thread = new Thread("thread") {
                public void run() {
                    Android24.getImageUrl(Graphics.statsImage(powerPoints), imageUrl);
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
            e.printStackTrace();
        }
        ppEmbed.setImage(imageUrl.get());
        return ppEmbed.build();
    }

    public static MessageEmbed getPowerPointsEmbed(PowerPoints powerPoints, boolean ephemeral) {
        AtomicReference<String> imageUrl = new AtomicReference<>();
        Thread thread = null;
        if (ephemeral) {
            thread = new Thread("thread") {
                public void run() {
                    Android24.getImageUrl(Graphics.statsImage(powerPoints), imageUrl);
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
            e.printStackTrace();
        }

        ppEmbed.setImage(imageUrl.get());
        return ppEmbed.build();
    }

}
