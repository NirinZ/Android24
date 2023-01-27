package com.SharxNZ.Learning;


import com.SharxNZ.Android24;
import com.SharxNZ.Utilities.Embeds;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class WordsList {

    public static final String sePrefix = "scrollingEvent#";
    protected Queue<Word> words = new LinkedList<>();

    public WordsList(String language, String colors, int number) throws SQLException {
        try (
                Connection con = Android24.getWordsConnection();
                PreparedStatement getWords = con.prepareStatement(String.format( """
                        SELECT *
                        FROM %s
                        WHERE color in (?, ?, ?)
                        ORDER BY Rand()
                        LIMIT ?;
                        """, language))
                ) {
            getWords.setString(1, colors.charAt(0) + "");
            getWords.setString(2, colors.charAt(colors.length()/3) + "");
            getWords.setString(3, colors.charAt(colors.length() - 1) + "");
            getWords.setInt(4, number);

            ResultSet resultSet = getWords.executeQuery();
            while (resultSet.next())
                words.add(new Word(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6).charAt(0)
                ));

            resultSet.close();
        }




    }

    public MessageEmbed getCurrentEmbed(User user) {
        if (!words.isEmpty())
            return words.peek().getEmbed().setFooter(user.getName() , user.getAvatarUrl()).build();
        return Embeds.errorEmbed("The list is empty for some reason...");
    }

    public MessageEmbed startLearningEvent(User user, String id) {
        scrollingEvent(user, id);
        return getCurrentEmbed(user);
    }

    /**
     * The reason that there is Listener here and in the WordsButtons, is that in here I gust changing the color in
     * the scrolling event, and in the buttons, I'm also changing the value at the DataBase + editing the Embed.
     * I wanted to edit the embed in the buttons because I'll use it to edit also the embeds of the find function
     * */
    private void scrollingEvent(User user, String id) {
        Android24.eventWaiter.waitForEvent(ButtonClickEvent.class, bce -> (bce.getComponentId().startsWith(sePrefix) || bce.getComponentId().startsWith(Word.colorWordPrefix))
                && bce.getComponentId().endsWith(id) && bce.getUser().equals(user), bce -> {
            /*else { */
            if (bce.getComponentId().startsWith(sePrefix)) {
                switch (bce.getComponentId().split("#")[1]) {
                    case "know" -> words.poll();
                    case "dknow" -> words.add(words.poll());
                }
                if (!words.isEmpty()) {
                    bce.editMessageEmbeds(getCurrentEmbed(user)).queue();
                    scrollingEvent(user, id);
                } else
                    bce.editMessageEmbeds(Embeds.successEmbed("You done the list! 😊")).setActionRows().queue();
            } else if (bce.getComponentId().startsWith(Word.colorWordPrefix)){
                words.peek().setColor(bce.getComponentId().split("#")[1].charAt(0));
                scrollingEvent(user, id);
            }
        }, 5, TimeUnit.MINUTES, () -> {
        });
    }

    public Queue<Word> getWords() {
        return words;
    }

    public void setWords(Queue<Word> words) {
        this.words = words;
    }
}
