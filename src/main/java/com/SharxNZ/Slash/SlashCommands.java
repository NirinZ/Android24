package com.SharxNZ.Slash;

import com.SharxNZ.Commands.GameCommands.*;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Race;
import com.SharxNZ.GameFunctions.StartGame;
import com.SharxNZ.Utilities.Graphics;
import com.SharxNZ.Utilities.Utils;
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

        long guildID = slashCommandEvent.getGuild().getIdLong();
        long userID = slashCommandEvent.getUser().getIdLong();

        //Make sure you are in the game
        if(!slashCommandEvent.getName().equals("start_game") && !Utils.checkInGame(userID)){
            slashCommandEvent.reply("You are not in the game! Please use the command `/start_game`" +
                    " or use the buttons to join the game 😁").setEphemeral(true).queue();
            return;
        }

        switch (slashCommandEvent.getName()) {
            case "start_game":
                slashCommandEvent.reply(StartGame.startGame(userID,
                        Race.valueOf(slashCommandEvent.getOption("race").getAsString())))
                        .setEphemeral(true).queue();
                break;
            case "echo":
                slashCommandEvent.reply(slashCommandEvent.getOption("content").getAsString()).queue();
                break;
            case "ping":
                slashCommandEvent.reply("Pong!").queue();
                //slashCommandEvent.getHook().sendFile()
                break;
            case "getstats":
                if(!slashCommandEvent.getOptions().isEmpty() && slashCommandEvent.getOptions().get(0).getAsBoolean()) {
                    slashCommandEvent.reply(GetStats.getStats(Being.getBeing(userID))).queue();
                }
                else{
                    slashCommandEvent.reply(GetStats.getStats(Being.getBeing(userID)))
                            .setEphemeral(true).queue();
                }
                break;

            case "level":
                String userURL = slashCommandEvent.getUser().getAvatarUrl();
                if(!slashCommandEvent.getOptions().isEmpty() && slashCommandEvent.getOptions().get(0).getAsBoolean()) {
                    slashCommandEvent.deferReply().queue();
                    slashCommandEvent.getHook().sendFile(Objects.requireNonNull(Level.returnLevel(guildID, userID, userURL)), "Level.jpg").queue();
                } else {
                    slashCommandEvent.deferReply(true).queue();
                    slashCommandEvent.getHook().sendMessageEmbeds(Level.returnLevelEmbed(guildID, userID, userURL)).queue();
                }
                break;
            case "get_power_points":
                GCButtons.save.remove(userID);
                PowerPoints.getPPoints().remove(userID);
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
                                    PowerPoints.getPowerPoints(userID), true, true))
                            .setEphemeral(true).addActionRows(actionRow).queue();
                }
                else{
                    PowerPoints powerPoints = PowerPoints.getPowerPoints(userID);
                    slashCommandEvent.deferReply().queue();
                    slashCommandEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(Stats.getPowerPointsEmbed(
                                    PowerPoints.getPowerPoints(userID), false, true))
                            .addActionRows(actionRow).queue();
                }

                break;
            case "test":
                slashCommandEvent.reply("").queue();
                //slashCommandEvent.getHook().sendFile().addEmbeds().queue();
            default:
                slashCommandEvent.reply("What is that?").queue();

        }
    }
}
