package com.SharxNZ.Buttons;

import com.SharxNZ.Android24;
import com.SharxNZ.Learning.Word;
import com.SharxNZ.Utilities.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;

public class WordsButtons extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent buttonClickEvent) {
        if(buttonClickEvent.getComponentId().startsWith(Word.colorWordPrefix) &&
                buttonClickEvent.getUser().getIdLong() == Android24.nirin.getIdLong()) {

            String[] arguments = buttonClickEvent.getComponentId().split("#");
            //argument[0] => Word.colorWordPrefix
            String word = buttonClickEvent.getMessage().getEmbeds().get(0).getTitle();
            String color = arguments[1];
            String language = arguments[2];

            try {
                Color embedColor = Word.changeColor(language, word, color);
                EmbedBuilder embed = new EmbedBuilder(buttonClickEvent.getMessage().getEmbeds().get(0));
                buttonClickEvent.editMessageEmbeds(embed.setColor(embedColor).build()).queue();
            } catch (SQLException throwables) {
                Android24.logError(throwables);
                throwables.printStackTrace();
                buttonClickEvent.replyEmbeds(Embeds.errorEmbed(throwables)).setEphemeral(true).queue();
            }
        }
    }
}
