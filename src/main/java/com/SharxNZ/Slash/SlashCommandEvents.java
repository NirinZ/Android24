package com.SharxNZ.Slash;

import com.SharxNZ.Android24;
import com.SharxNZ.Buttons.PPButtons;
import com.SharxNZ.Commands.GameCommands.*;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Race;
import com.SharxNZ.GameFunctions.StartGame;
import com.SharxNZ.Utilities.Embeds;
import com.SharxNZ.Utilities.Graphics;
import com.SharxNZ.Utilities.Test;
import com.SharxNZ.Utilities.Utils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import javax.naming.NameNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SlashCommandEvents extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent slashCommandEvent) {
        // Only accept commands from guilds
        if (slashCommandEvent.getGuild() == null)
            return;

        User user = slashCommandEvent.getUser();
        long guildID = slashCommandEvent.getGuild().getIdLong();
        long userID = user.getIdLong();

        //Make sure you are in the game
        if(!slashCommandEvent.getName().equals("start_game") && !Utils.checkInGame(userID)){
            slashCommandEvent.reply("You are not in the game! Please use the command `/start_game`" +
                    " or use the buttons to join the game 😁").setEphemeral(true).queue();
            return;
        }

        switch (slashCommandEvent.getName()) {
            case "start_game" -> {
                slashCommandEvent.reply(StartGame.startGame(userID,
                        Race.valueOf(slashCommandEvent.getOption("race").getAsString())))
                        .setEphemeral(true).queue();
            }
            case "echo" -> slashCommandEvent.reply(slashCommandEvent.getOption("content").getAsString()).queue();

            case "ping" -> slashCommandEvent.reply("Pong!").queue();
                //slashCommandEvent.getHook().sendFile()

            case "stats" -> {
                if (!slashCommandEvent.getOptions().isEmpty() && slashCommandEvent.getOptions().get(0).getAsBoolean())
                    slashCommandEvent.replyEmbeds(GetStats.statsEmbed(user)).queue();
                else
                    slashCommandEvent.replyEmbeds(GetStats.statsEmbed(user)).setEphemeral(true).queue();
            }

            case "level" -> {
                String userURL = user.getAvatarUrl();
                if (!slashCommandEvent.getOptions().isEmpty() && slashCommandEvent.getOptions().get(0).getAsBoolean()) {
                    slashCommandEvent.deferReply().queue();
                    slashCommandEvent.getHook().sendFile(Objects.requireNonNull(Level.returnLevel(guildID, userID, userURL)), "Level.jpg").queue();
                } else {
                    slashCommandEvent.deferReply(true).queue();
                    slashCommandEvent.getHook().sendMessageEmbeds(Level.returnLevelEmbed(guildID, userID, userURL)).queue();
                }
            }
            case "get_power_points" -> {
                PPButtons.save.remove(userID);
                PowerPoints.getPPoints().remove(userID);
                boolean ephemeral = slashCommandEvent.getOptionsByName("display").isEmpty() || !slashCommandEvent.getOptionsByName("display").get(0).getAsBoolean();
                boolean refresh = !(slashCommandEvent.getOptionsByName("refresh").isEmpty() || !slashCommandEvent.getOptionsByName("refresh").get(0).getAsBoolean());
                String args = ephemeral + ":" + refresh;
                String buttonID = userID + "#$#" + args;
                // userID#command#ephemeral:refresh
                ArrayList<ActionRow> ppButtons = new ArrayList<>(Arrays.asList(
                        //Raw 1
                        ActionRow.of(Button.secondary(buttonID.replace("$", "Left"), "⬅"),
                                Button.secondary(buttonID.replace("$", "Right"), "➡"),
                                Button.secondary(buttonID.replace("$", "Up"), "⬆"),
                                Button.secondary(buttonID.replace("$", "Down"), "⬇")),
                        //Raw 2
                        ActionRow.of(Button.success(buttonID.replace("$", "Save"), "Save ✅")/*.withEmoji(Emoji.fromEmote())*/,
                                Button.danger(buttonID.replace("$", "Discard"), "Discard ❌"),
                                Button.primary(buttonID.replace("$", "Refresh"), "Refresh 🔄"))
                ));

                if (ephemeral) {
                    slashCommandEvent.deferReply(true).queue();
                    slashCommandEvent.getHook()
                            .sendMessageEmbeds(PowerPoints.getPowerPointsEmbed(
                                    PowerPoints.getPowerPoints(userID), true, true))
                            .setEphemeral(true).addActionRows(ppButtons).queue();
                } else {
                    PowerPoints powerPoints = PowerPoints.getPowerPoints(userID);
                    slashCommandEvent.deferReply().queue();
                    slashCommandEvent.getHook().sendFile(Graphics.statsImage(powerPoints), "png.png")
                            .addEmbeds(PowerPoints.getPowerPointsEmbed(
                                    PowerPoints.getPowerPoints(userID), false, true))
                            .addActionRows(ppButtons).queue();
                }

            }

            case "shop" -> {
                ArrayList<ActionRow> shpButtons = new ArrayList<>(Arrays.asList(
                        //Raw 1
                        ActionRow.of(Button.primary("sce#left#" + slashCommandEvent.getId(), "⬅"),
                                Button.primary("sce#right#" + slashCommandEvent.getId(), "➡")),
                        //Raw 2
                        ActionRow.of(Button.success("sce#buy#" + slashCommandEvent.getId(), "Buy 💵"))
                ));
                if (slashCommandEvent.getSubcommandName().equals("view")) {
                    if (slashCommandEvent.getOptionsByName("type").isEmpty() || slashCommandEvent.getOption("type").getAsString().equals("List"))
                        switch (slashCommandEvent.getOption("of").getAsString()) {
                            case "Special Attacks" -> slashCommandEvent.replyEmbeds(Shop.attacksShop(userID)).setEphemeral(true).queue();
                            case "Transformations" -> slashCommandEvent.replyEmbeds(Shop.transformationsShop(userID)).setEphemeral(true).queue();
                        }
                    else
                        switch (slashCommandEvent.getOption("of").getAsString()) {
                            case "Special Attacks" -> slashCommandEvent.replyEmbeds(Shop.getAttacksShop(userID).startScrollingEvent(user, slashCommandEvent.getId())).setEphemeral(true)
                                    .addActionRows(shpButtons).queue();
                            case "Transformations" -> slashCommandEvent.replyEmbeds(Shop.getTransformationsShop(userID).startScrollingEvent(user, slashCommandEvent.getId())).setEphemeral(true)
                                    .addActionRows(shpButtons).queue();
                        }
                } else if (slashCommandEvent.getSubcommandName().equals("buy"))
                    slashCommandEvent.replyEmbeds(Shop.shopView(slashCommandEvent.getOption("item").getAsString()))
                            .setEphemeral(true).addActionRow(Button.success("shop#" + slashCommandEvent.getOption("item").getAsString(), "Buy 💵")).queue();
            }

            case "inventory" -> {
                if (slashCommandEvent.getOptionsByName("type").isEmpty() || slashCommandEvent.getOption("type").getAsString().equals("List"))
                    switch (slashCommandEvent.getOption("of").getAsString()) {
                        case "Special Attacks" -> slashCommandEvent.replyEmbeds(Inventory.attacks(user)).setEphemeral(true).queue();
                        case "Transformations" -> slashCommandEvent.replyEmbeds(Inventory.transformations(user)).setEphemeral(true).queue();
                    }
                else
                    try {
                        switch (slashCommandEvent.getOption("of").getAsString()) {
                            case "Special Attacks" -> slashCommandEvent.replyEmbeds(Being.getDisplayAttacks(user.getIdLong()).startScrollingEvent(user, slashCommandEvent.getId())).setEphemeral(true)
                                    .addActionRow(Button.primary("sce#left#" + slashCommandEvent.getId(), "⬅"), Button.primary("sce#right#" + slashCommandEvent.getId(), "➡")).queue();
                            case "Transformations" -> slashCommandEvent.replyEmbeds(Being.getDisplayTransformations(user.getIdLong()).startScrollingEvent(user, slashCommandEvent.getId())).setEphemeral(true)
                                    .addActionRow(Button.primary("sce#left#" + slashCommandEvent.getId(), "⬅"), Button.primary("sce#right#" + slashCommandEvent.getId(), "➡")).queue();
                        }
                    } catch (SQLException throwables) {
                        Android24.logError(throwables);
                        throwables.printStackTrace();
                    } catch (NameNotFoundException exception) {
                        slashCommandEvent.replyEmbeds(Embeds.errorTextEmbed("You have no items to display...")).queue();
                    }
            }

            case "nuke" -> {
                slashCommandEvent.reply("""
                        **You got busted and reported to the admin!**
                        https://tenor.com/view/f-bi-raid-swat-gif-11500735
                        https://tenor.com/view/busted-police-unmarked-undercove-gif-20202846""").setEphemeral(true).queue();
                slashCommandEvent.getJDA().retrieveUserById(303807596555534337L).queue(bob -> {
                    slashCommandEvent.getJDA().getTextChannelById(790508049222729739L).sendMessage(
                            slashCommandEvent.getUser().getAsMention() + " tried to nuke the server! 😱\n"
                                    +bob.getAsMention()).queue();
                });
            }

            case "test" -> {
                Test test = new Test(userID);
                slashCommandEvent.reply("cid: " + slashCommandEvent.getCommandId()+
                        "\nid: "+slashCommandEvent.getId())
                        .addActionRow(SelectionMenu.create("t:test").setPlaceholder("Test").setRequiredRange(0,2)
                                .addOption("T1", "T1").addOption("T2","T2").build()).queue();

            }
            //slashCommandEvent.getHook().sendFile().addEmbeds().queue();
            default -> slashCommandEvent.reply("Unregistered command :" + slashCommandEvent.getName() + " | " + slashCommandEvent.getSubcommandName()
             + " ~ " + slashCommandEvent.getSubcommandGroup()).queue();
        }
    }
}
