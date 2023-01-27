package com.SharxNZ.Learning;

import com.SharxNZ.Android24;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//enum WordsColor {
//    Green, Yellow, Red
//}

public class Word {

    public static final String colorWordPrefix = "colorword#";
    protected String word;
    protected String translation;
    protected String example;
    protected String translatedExample;
    protected char color;

    public Word(String word, String translation, String example, String translatedExample, char color) {
        this.word = word;
        this.translation = translation;
        this.example = example;
        this.translatedExample = translatedExample;
        this.color = color;
    }

//    public Word getWordByTitle(String title) {
//        SQL for the word
//    }

    public EmbedBuilder getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(word);
        embedBuilder.setDescription(MarkdownUtil.spoiler(translation));
        embedBuilder.addField("Example", MarkdownUtil.spoiler(example), true);
        embedBuilder.addField("Translated Example", MarkdownUtil.spoiler(translatedExample), true);
        Color embedColor = new Color(255,255,255);
        switch (color) {
            case 'G' -> embedColor = Color.green;
            case 'Y' -> embedColor = Color.yellow;
            case 'R' -> embedColor = Color.RED;
        }
        embedBuilder.setColor(embedColor);
//        embedBuilder.setImage(getGif());
        return embedBuilder;
    }

    public static Color changeColor(String language, String word, String color) throws SQLException {
        // ניתן להכניס כאן פעולה שמזהה את השפה של המילה, וכך מורידה את הצורך לספק שפה.
        // ניתן לעשות זאת במידה ואצתרך לקרוא לפעולה הזו ממקום אחר שלא יכול לספק לי שפת מקור.
        // כנראה אעשה את זה אם אוסיף אפשרות ללמוד אנגלית ועברית בו זמנית באותה רשימה.

        try (
                Connection con = Android24.getWordsConnection();
                PreparedStatement colorStatement = con.prepareStatement(
                        String.format("UPDATE %s SET `color` = ? WHERE `word` = ?;", language))
                ){
            colorStatement.setString(1, color);
            colorStatement.setString(2, word);
            colorStatement.executeUpdate();

            Color embedColor = new Color(255,255,255);
            switch (color) {
                case "G" -> embedColor = Color.green;
                case "Y" -> embedColor = Color.yellow;
                case "R" -> embedColor = Color.RED;
            }
            return embedColor;
        }
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getTranslatedExample() {
        return translatedExample;
    }

    public void setTranslatedExample(String translatedExample) {
        this.translatedExample = translatedExample;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }
}