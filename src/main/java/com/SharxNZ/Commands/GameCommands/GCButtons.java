package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Commands.GameCommands.PowerPoints;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Utilities.Utils;
import com.SharxNZ.Utilities.Graphics;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.HashSet;

public class GCButtons extends ListenerAdapter {

    public static HashSet<Long> save = new HashSet<>();
    
    @Override
    public void onButtonClick(ButtonClickEvent buttonClickEvent){
        String[] split = buttonClickEvent.getComponentId().split("#");
        long userID = buttonClickEvent.getUser().getIdLong();
        if (!split[0].equals(Long.toString(userID))) {
            return;
        }
        String command = split[1];
        String[] args = split[2].split(":");

        //For power points
        String buttonID = userID + "#$#" + split[2];
        ActionRow actionRow = ActionRow.of(Button.secondary(buttonID.replace("$","Left"), "⬅"),
                Button.secondary(buttonID.replace("$","Right"), "➡"),
                Button.secondary(buttonID.replace("$","Up"), "⬆"), Button.secondary(buttonID.replace("$","Down"), "⬇"),
                Button.primary(buttonID.replace("$","Save"), "Save ✅"));
        PowerPoints powerPoints = PowerPoints.getPowerPoints(userID);


        switch (command) {
            case "Down" -> {
                // arg[0] = ephemeral, arg[1] = image

                powerPoints.subtractValue();
                if (!Boolean.parseBoolean(args[0]) && Boolean.parseBoolean(args[1])) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, false, true))
                            .addActionRows(actionRow).queue();
                    buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                            Boolean.parseBoolean(args[0]), Boolean.parseBoolean(args[1]))).queue();
            }
            case "Up" -> {
                powerPoints.addValue();
                if (!Boolean.parseBoolean(args[0]) && Boolean.parseBoolean(args[1])) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, false, true))
                            .addActionRows(actionRow).queue();
                    buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                            Boolean.parseBoolean(args[0]), Boolean.parseBoolean(args[1]))).queue();
            }
            case "Left" -> {
                powerPoints.previousValue();
                buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                        Boolean.parseBoolean(args[0]), false)).queue();
            }
            case "Right" -> {
                powerPoints.nextValue();
                buttonClickEvent.editMessageEmbeds().setEmbeds(Stats.getPowerPointsEmbed(powerPoints,
                        Boolean.parseBoolean(args[0]), false)).queue();
            }
            case "Save" -> {
                if (!save.contains(userID)) {
                    save.add(userID);
                    if(Boolean.parseBoolean(args[0]))
                        buttonClickEvent.deferEdit().setEmbeds(Stats.getPowerPointsEmbed(
                                powerPoints, true)).queue();
                    else{
                        buttonClickEvent.deferEdit().queue();
                        buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                                .addEmbeds(Stats.getPowerPointsEmbed(powerPoints, true))
                                .addActionRows(actionRow).queue();
                        buttonClickEvent.getMessage().delete().queue();
                    }
                } else {
                    powerPoints.save();
                    buttonClickEvent.editMessageEmbeds(Utils.savedEmbed()).queue();
                    //buttonClickEvent.getMessage().delete().queue();
                }
            }
        }
    }
}
