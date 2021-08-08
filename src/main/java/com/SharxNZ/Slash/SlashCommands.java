package com.SharxNZ.Slash;

import com.SharxNZ.Commands.GameCommands.*;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Race;
import com.SharxNZ.Utilities.Graphics;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.Objects;

public class SlashCommands extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent slashCommandEvent)
    {
        // Only accept commands from guilds
        if (slashCommandEvent.getGuild() == null)
            return;
        String guildID = slashCommandEvent.getGuild().getId();
        String userID = slashCommandEvent.getUser().getId();
        switch (slashCommandEvent.getName()) {
            case "echo":
                slashCommandEvent.reply(slashCommandEvent.getOption("content").getAsString()).queue();
                break;
            case "ping":
                slashCommandEvent.reply("Pong!").queue();
                //slashCommandEvent.getHook().sendFile()
                break;
            case "getstats":
                if(!slashCommandEvent.getOptions().isEmpty() && slashCommandEvent.getOptions().get(0).getAsBoolean()) {
                    slashCommandEvent.reply(GetStats.getStats(Being.getBeing(guildID, userID))).queue();
                }
                else{
                    slashCommandEvent.reply(GetStats.getStats(Being.getBeing(guildID, userID)))
                            .setEphemeral(true).queue();
                }
                break;
            case "level":
                if(!slashCommandEvent.getOptions().isEmpty() && slashCommandEvent.getOptions().get(0).getAsBoolean()) {
                    slashCommandEvent.deferReply().queue();
                    slashCommandEvent.getHook().sendFile(Objects.requireNonNull(Level.returnLevel(guildID, userID)), "Level.jpg").queue();
                }
                else
                    slashCommandEvent.deferReply(true).addEmbeds(Level.returnLevelEmbed(guildID, userID)).queue();
                break;

            case "get_power_points":
                ButtonClick.save.remove(guildID+"#"+userID);
                PowerPoints.getPPoints().remove(guildID+"#"+userID);
                boolean ephemeral = slashCommandEvent.getOptionsByName("display").isEmpty() || !slashCommandEvent.getOptionsByName("display").get(0).getAsBoolean();
                boolean refresh = !(slashCommandEvent.getOptionsByName("refresh").isEmpty() || !slashCommandEvent.getOptionsByName("refresh").get(0).getAsBoolean());
                String args = ephemeral + ":" + refresh;
                String buttonID = userID + "#$#" + args;
                ActionRow actionRow = ActionRow.of(Button.secondary(buttonID.replace("$","Left"), "⬅"),
                        Button.secondary(buttonID.replace("$","Right"), "➡"),
                        Button.secondary(buttonID.replace("$","Up"), "⬆"), Button.secondary(buttonID.replace("$","Down"), "⬇"),
                        Button.primary(buttonID.replace("$","Save"), "Save ✅")/*.withEmoji(Emoji.fromEmote())*/);
                if (ephemeral){
                    slashCommandEvent.deferReply(true).queue();
                    slashCommandEvent.getHook()
                            .sendMessageEmbeds(Stats.getPowerPointsEmbed(
                                    PowerPoints.getPowerPoints(guildID, userID), true, true))
                            .setEphemeral(true).addActionRows(actionRow).queue();
                }
                else{
                    PowerPoints powerPoints = PowerPoints.getPowerPoints(guildID, userID);
                    slashCommandEvent.deferReply().queue();
                    slashCommandEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(
                                    PowerPoints.getPowerPoints(guildID, userID), false, true))
                            .addActionRows(actionRow).queue();
                }

                break;
            case "start_game":
                slashCommandEvent.reply(StartGame.startGame(guildID, userID,
                        Race.valueOf(slashCommandEvent.getOption("race").getAsString())))
                        .setEphemeral(true).queue();
                break;
            case "test":
                slashCommandEvent.reply("").queue();
                //slashCommandEvent.getHook().sendFile().addEmbeds().queue();
            default:
                slashCommandEvent.reply("What is that?").queue();

        }
    }
}
