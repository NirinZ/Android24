package com.SharxNZ.Buttons;

import com.SharxNZ.Android24;
import com.SharxNZ.Battle.Battle;
import com.SharxNZ.Utilities.Embeds;
import com.SharxNZ.Utilities.Utils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BattleButtons extends ListenerAdapter {


    @Override
    public void onButtonClick(@NotNull ButtonClickEvent buttonClickEvent) {

        // battle#command#button#userID1:userID2(optional)
        // userID1 - the user how requested the battle
        // userID2 - the user how needs to approve the battle (if exists)
        String[] split = buttonClickEvent.getComponentId().split("#");
        if (!split[0].equals("battle"))
            return;

        User clickedUser = buttonClickEvent.getUser();
        Guild guild = buttonClickEvent.getGuild();

        String command = split[1];
        String button = split[2];
        String[] args = split[3].split(":");

        if (clickedUser.getId().equals(args[0])) {
            buttonClickEvent.replyEmbeds(Embeds.errorEmbed("You can't fight with yourself!")).setEphemeral(true).queue();
            return;
        }

        if (!Utils.checkInGame(clickedUser.getIdLong())) {
            buttonClickEvent.replyEmbeds(Embeds.errorEmbed("You have to be in the game to participate in a battle, use the command `/start_game`!")).setEphemeral(true).queue();
            return;
        }


        switch (command) {
            case "pvp" -> {
                switch (button) {
                    case "fight" -> Android24.jda.retrieveUserById(args[0]).queue(user -> {
                        new Battle(guild, user, clickedUser);
                        buttonClickEvent.editMessage(new MessageBuilder("The battle has started in: " + user.getName() + " vs " + clickedUser.getName()).build()).queue();
                    });
                    case "ufight" -> {
                        if (clickedUser.getId().equals(args[1])) {
                            Android24.jda.retrieveUserById(args[0]).queue(user -> {
                                new Battle(guild, user, clickedUser);
                                buttonClickEvent.editMessage(new MessageBuilder("The battle has started in: " + user.getName() + " vs " + clickedUser.getName()).build()).queue();
                            });
                        } else {
                            buttonClickEvent.replyEmbeds(Embeds.errorEmbed("You are not the one how challenged!")).setEphemeral(true).queue();
                        }

                    }
                    case "udecline" -> {
                        if (clickedUser.getId().equals(args[1]))
                            buttonClickEvent.editMessageEmbeds(Embeds.errorEmbed("The request hase been declined...")).queue(interactionHook ->
                                    interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
                        else {
                            buttonClickEvent.replyEmbeds(Embeds.errorEmbed("You are not the one how challenged!")).setEphemeral(true).queue();
                        }

                    }
                }
            }
        }


    }
}
