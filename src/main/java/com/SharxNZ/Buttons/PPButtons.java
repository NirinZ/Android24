package com.SharxNZ.Buttons;

import com.SharxNZ.Commands.GameCommands.PowerPoints;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Utilities.Embeds;
import com.SharxNZ.Utilities.Utils;
import com.SharxNZ.Utilities.Graphics;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PPButtons extends ListenerAdapter {

    public static HashSet<Long> save = new HashSet<>();

    @Override
    public void onButtonClick(ButtonClickEvent buttonClickEvent) {
        String[] split = buttonClickEvent.getComponentId().split("#");
        long userID = buttonClickEvent.getUser().getIdLong();
        if (!split[0].equals(Long.toString(userID)))
            return;

        String command = split[1];
        String[] args = split[2].split(":");

        //For power points
        String buttonID = userID + "#$#" + split[2];
        ArrayList<ActionRow> actionRows = new ArrayList<>(Arrays.asList(
                //Raw 1
                ActionRow.of(Button.secondary(buttonID.replace("$", "Left"), "⬅"),
                        Button.secondary(buttonID.replace("$", "Right"), "➡"),
                        Button.secondary(buttonID.replace("$", "Up"), "⬆"),
                        Button.secondary(buttonID.replace("$", "Down"), "⬇")),
                //Raw 2
                ActionRow.of(Button.success(buttonID.replace("$", "Save"), "Save ✅")/*.withEmoji(Emoji.fromEmote())*/,
                        Button.danger(buttonID.replace("$", "Discard"), "Discard ❌"),
                        Button.primary(buttonID.replace("$", "Reload"), "Reload 🔄"))
        ));

        PowerPoints powerPoints = PowerPoints.getPowerPoints(userID);


        switch (command) {
            case "Down" -> {
                // arg[0] = ephemeral, arg[1] = image
                //          display             refresh
                if (save.contains(userID))
                    return;
                powerPoints.subtractValue();
                if (!Boolean.parseBoolean(args[0]) && Boolean.parseBoolean(args[1])) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, false, true))
                            .addActionRows(actionRows).queue();
                    buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                            Boolean.parseBoolean(args[0]), Boolean.parseBoolean(args[1]))).queue();
            }
            case "Up" -> {
                if (save.contains(userID))
                    return;
                powerPoints.addValue();
                if (!Boolean.parseBoolean(args[0]) && Boolean.parseBoolean(args[1])) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, false, true))
                            .addActionRows(actionRows).queue();
                    buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                            Boolean.parseBoolean(args[0]), Boolean.parseBoolean(args[1]))).queue();
            }
            case "Left" -> {
                if (save.contains(userID))
                    return;
                powerPoints.previousValue();
                buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                        Boolean.parseBoolean(args[0]), false)).queue();
            }
            case "Right" -> {
                if (save.contains(userID))
                    return;
                powerPoints.nextValue();
                buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                        Boolean.parseBoolean(args[0]), false)).queue();
            }
            case "Save" -> {
                if (!save.contains(userID)) {
                    save.add(userID);
                    if (Boolean.parseBoolean(args[0]))
                        buttonClickEvent.deferEdit().setEmbeds(Stats.getPowerPointsEmbed(
                                powerPoints, true)).queue();
                    else {
                        buttonClickEvent.deferEdit().queue();
                        buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                                .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, true))
                                .addActionRows(actionRows).queue();
                        buttonClickEvent.getMessage().delete().queue();
                    }
                } else {
                    powerPoints.save();
                    buttonClickEvent.editMessageEmbeds(Embeds.savedEmbed()).queue();
                    //buttonClickEvent.getMessage().delete().queue();
                }
            }
            case "Discard" -> {
                save.remove(userID);
                PowerPoints.getPPoints().remove(userID);
                buttonClickEvent.editMessageEmbeds(Embeds.discardEmbed()).queue();
            }
            case "Refresh" -> {
                if (save.contains(userID))
                    return;
                if (!Boolean.parseBoolean(args[0])) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, false, true))
                            .addActionRows(actionRows).queue();
                    buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                            Boolean.parseBoolean(args[0]), true)).queue();
            }
        }
    }
}

