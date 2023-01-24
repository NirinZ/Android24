package com.SharxNZ.Learning;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;

//enum WordsColor {
//    Green, Yellow, Red
//}

public class Word {

    /**
    *  חשבתי לעשות שהוא ימשוך את המילה בפעולה הבונה,
     *  אבל אם אני רוצה מספר גדול של מילים, עדיף כבר למשוך אותם בבת אחת
     *  לכן בפ"ב, עדיף לעשות רק את התכונות של המילה, ללא הקריאה לDB
    * */

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

    public EmbedBuilder getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(word);
        embedBuilder.setDescription(MarkdownUtil.spoiler(translation));
        embedBuilder.addField("Example", example, true);
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