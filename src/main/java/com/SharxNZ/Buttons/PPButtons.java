package com.SharxNZ.Buttons;

import com.SharxNZ.Commands.GameCommands.PowerPoints;
import com.SharxNZ.Utilities.Embeds;
import com.SharxNZ.Utilities.Graphics;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.utils.AttachmentOption;

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
                        Button.primary(buttonID.replace("$", "Refresh"), "Reload 🔄"))
        ));

        PowerPoints powerPoints = PowerPoints.getPowerPoints(userID);

        boolean ephemeral = Boolean.parseBoolean(args[0]);
        boolean image = Boolean.parseBoolean(args[1]);

        switch (command) {
            case "Down" -> {
                // arg[0] = ephemeral, arg[1] = image
                //          display             refresh
                if (save.contains(userID))
                    return;
                powerPoints.subtractValue();
                if (image) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(powerPoints.getPowerPointsEmbed())
                            .addActionRows(actionRows).setEphemeral(ephemeral).queue();
                    if (!ephemeral)
                        buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(powerPoints.getPowerPointsEmbed()).queue();
            }
            case "Up" -> {
                if (save.contains(userID))
                    return;
                powerPoints.addValue();
                if (image) {
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(powerPoints.getPowerPointsEmbed())
                            .addActionRows(actionRows).setEphemeral(ephemeral).queue();
                    if (!ephemeral)
                        buttonClickEvent.getMessage().delete().queue();
                } else
                    buttonClickEvent.editMessageEmbeds().setEmbeds(powerPoints.getPowerPointsEmbed()).queue();
            }
            case "Left" -> {
                if (save.contains(userID))
                    return;
                powerPoints.previousValue();
                buttonClickEvent.editMessageEmbeds().setEmbeds(powerPoints.getPowerPointsEmbed()).queue();
            }
            case "Right" -> {
                if (save.contains(userID))
                    return;
                powerPoints.nextValue();
                buttonClickEvent.editMessageEmbeds().setEmbeds(powerPoints.getPowerPointsEmbed()).queue();
            }
            case "Save" -> {
                if (!save.contains(userID)) {
                    save.add(userID);
                    buttonClickEvent.deferEdit().queue();
                    buttonClickEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(powerPoints.getWarningEmbed())
                            .addActionRows(actionRows).queue();
                } else {
                    powerPoints.save();
                    buttonClickEvent.editMessageEmbeds(Embeds.savedEmbed()).queue();
                }
                if (!ephemeral)
                    buttonClickEvent.getMessage().delete().queue();
            }
            case "Discard" -> {
                save.remove(userID);
                PowerPoints.getPPoints().remove(userID);
                buttonClickEvent.editMessageEmbeds(Embeds.discardEmbed()).queue();
            }
            case "Refresh" -> {
                if (save.contains(userID))
                    return;
                buttonClickEvent.deferEdit().queue();
                buttonClickEvent.getHook()
                        .sendFile(Graphics.statsImage(powerPoints), "png.png")
                        .addEmbeds(powerPoints.getPowerPointsEmbed())
                        .addActionRows(actionRows)
                        .setEphemeral(ephemeral).queue();
                if (!ephemeral)
                    buttonClickEvent.getMessage().delete().queue();
            }
        }
    }
}

