package com.SharxNZ.Slash;

import com.SharxNZ.Android24;
import com.SharxNZ.Buttons.PPButtons;
import com.SharxNZ.Commands.GameCommands.*;
import com.SharxNZ.Commands.Level;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Race;
import com.SharxNZ.Game.Transformation;
import com.SharxNZ.GameFunctions.StartGame;
import com.SharxNZ.Utilities.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
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
        Guild guild = slashCommandEvent.getGuild();
        long guildID = guild.getIdLong();
        long userID = user.getIdLong();

        //Make sure you are in the game
        if (!slashCommandEvent.getName().equals("start_game") && !Utils.checkInGame(userID)) {
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

            case "transform" -> {
                // Android24.log(guild.modifyRolePositions().selectPosition(0));
                if (Transformation.checkTransformation(userID, slashCommandEvent.getOption("name").getAsString())) {
                    guild.retrieveMemberById(userID).queue(member -> {
                        try {
                            if (Being.getBeing(userID).getTransformation() != null && member.getRoles().stream().anyMatch(role -> role.getName().equals(Being.getBeing(userID).getTransformation())))
                                guild.removeRoleFromMember(userID,
                                        guild.getRolesByName(Being.getBeing(userID).getTransformation(), true).get(0)).queue();

                            Transformation transformation = new Transformation(slashCommandEvent.getOption("name").getAsString());
                            if (guild.getRoles().stream().anyMatch(role -> role.getName().equals(transformation.getName()))) {
                                guild.addRoleToMember(userID,
                                        guild.getRolesByName(transformation.getName(), true).get(0)).queue();
                                Being.setTransformation(userID, transformation.getName());
                                //send gif
                                slashCommandEvent.reply("role added").setEphemeral(true).queue();
                            } else {
                                guild.createRole().setName(transformation.getName())
                                        .setColor(transformation.getColor()).queue(role -> {
                                    guild.addRoleToMember(userID,
                                            guild.getRolesByName(transformation.getName(), true).get(0)).queue();
                                    Being.setTransformation(userID, transformation.getName());
                                    //send gif
                                    slashCommandEvent.reply("role added").setEphemeral(true).queue();
                                    Server server = new Server(guildID);
                                    try {
                                        guild.modifyRolePositions().selectPosition(role).moveTo(
                                                guild.getRoleById(server.getTransRole()).getPosition() - 1).queue();
                                    } catch (NullPointerException nullPointerException) {
                                        guild.retrieveOwner().queue(owner -> {
                                            owner.getUser().openPrivateChannel().queue(privateChannel -> {
                                                privateChannel.sendMessage("You didn't have a role for the transformations 😱.\n" +
                                                        "Plz set one to prevent error and for best experience :-)").queue(null, throwable -> {
                                                    if (server.getLoggingCh() != 0)
                                                        guild.getTextChannelById(server.getLoggingCh()).sendMessage(
                                                                owner.getAsMention() +
                                                                        "\nYou didn't have a role for the transformations 😱.\n" +
                                                                        "Plz set one to prevent error and for best experience :-)").queue();
                                                });
                                            });
                                        });
                                    }
                                });
                            }
                        } catch (NameNotFoundException exception) {
                            slashCommandEvent.reply("This transformations doesn't exists.").setEphemeral(true).queue();
                        } catch (SQLException throwables) {
                            Android24.logError(throwables);
                            throwables.printStackTrace();
                        }
                    });

                } else if (slashCommandEvent.getOption("name").getAsString().equalsIgnoreCase("base")) {
                    if (Being.getBeing(userID).getTransformation() != null) {
                        if (guild.getMemberById(userID).getRoles().stream().anyMatch(role -> role.getName().equals(Being.getBeing(userID).getTransformation())))
                            guild.removeRoleFromMember(userID,
                                    guild.getRolesByName(Being.getBeing(userID).getTransformation(), true).get(0)).queue();
                        Being.setTransformation(userID, null);
                        //send gif
                        slashCommandEvent.reply("Reverted back").setEphemeral(true).queue();
                    } else
                        slashCommandEvent.reply("you're already in base").setEphemeral(true).queue();
                } else
                    slashCommandEvent.reply("You don't have this transformation...").setEphemeral(true).queue();
            }

            case "server_setup" -> {
                if (!guild.getMemberById(userID).hasPermission(Permission.MANAGE_PERMISSIONS)) {
                    slashCommandEvent.reply("You have to permission to use this command...").setEphemeral(true).queue();
                    return;
                }
                Server server = new Server(slashCommandEvent.getGuild().getIdLong());
                long comCh = !slashCommandEvent.getOptionsByName("cmd_channel").isEmpty() ? slashCommandEvent.getOption("cmd_channel").getAsLong() : server.getCommandsCh();
                long wlcCh = !slashCommandEvent.getOptionsByName("wlc_channel").isEmpty() ? slashCommandEvent.getOption("wlc_channel").getAsLong() : server.getWelcomeCh();
                long logCh = !slashCommandEvent.getOptionsByName("logg_channel").isEmpty() ? slashCommandEvent.getOption("logg_channel").getAsLong() : server.getLoggingCh();
                long transRl = !slashCommandEvent.getOptionsByName("trans_role").isEmpty() ? slashCommandEvent.getOption("trans_role").getAsLong() : server.getTransRole();
                boolean allowTrsGif = !slashCommandEvent.getOptionsByName("allow_trans_gif").isEmpty() ? slashCommandEvent.getOption("allow_trans_gif").getAsBoolean() : server.isAllowTransGif();
                server.setCommandsCh(comCh);
                server.setWelcomeCh(wlcCh);
                server.setLoggingCh(logCh);
                server.setTransRole(transRl);
                server.setAllowTransGif(allowTrsGif);

                server.setServer();
                slashCommandEvent.reply(
                        "Command channel: " + (guild.getTextChannelById(server.getCommandsCh()) != null ? guild.getTextChannelById(server.getCommandsCh()).getAsMention() : "`null`") +
                                "\nWelcome channel: " + (guild.getTextChannelById(server.getWelcomeCh()) != null ? guild.getTextChannelById(server.getWelcomeCh()).getAsMention() : "`null`") +
                                "\nLogging channel: " + (guild.getTextChannelById(server.getLoggingCh()) != null ? guild.getTextChannelById(server.getLoggingCh()).getAsMention() : "`null`") +
                                "\nTransformations role: " + (guild.getRoleById(server.getTransRole()) != null ? guild.getRoleById(server.getTransRole()).getAsMention() : "`null`") +
                                "\nAllow transformations gif globally: `" + server.isAllowTransGif() + "`"
                ).setEphemeral(true).queue();
            }

            case "nuke" -> {
                slashCommandEvent.reply("""
                        **You got busted and reported to the admin!**
                        https://tenor.com/view/f-bi-raid-swat-gif-11500735
                        https://tenor.com/view/busted-police-unmarked-undercove-gif-20202846""").setEphemeral(true).queue();
                slashCommandEvent.getJDA().retrieveUserById(303807596555534337L).queue(bob -> {
                    slashCommandEvent.getJDA().getTextChannelById(790508049222729739L).sendMessage(
                            slashCommandEvent.getUser().getAsMention() + " tried to nuke the server! 😱\n"
                                    + bob.getAsMention()).queue();
                });
            }

            case "test" -> {
                /*slashCommandEvent.reply("cid: " + slashCommandEvent.getCommandId()+
                        "\nid: "+slashCommandEvent.getId())
                        .addActionRow(SelectionMenu.create("t:test").setPlaceholder("Test").setRequiredRange(0,2)
                                .addOption("T1", "T1").addOption("T2","T2").build()).queue();*/

            }
            //slashCommandEvent.getHook().sendFile().addEmbeds().queue();
            default -> slashCommandEvent.reply("Unregistered command :" + slashCommandEvent.getName() + " | " + slashCommandEvent.getSubcommandName()
                    + " ~ " + slashCommandEvent.getSubcommandGroup()).queue();
        }
    }
}
