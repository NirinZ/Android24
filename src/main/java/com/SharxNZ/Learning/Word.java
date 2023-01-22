package com.SharxNZ.Learning;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.MarkdownUtil;

enum WordsColor {
    Green, Yellow, Red
}

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
    protected WordsColor color;

    public Word(String word, String translation, String example, String translatedExample, WordsColor color) {
        this.word = word;
        this.translation = translation;
        this.example = example;
        this.translatedExample = translatedExample;
        this.color = color;
    }

    public MessageEmbed getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(word);
        embedBuilder.setDescription(MarkdownUtil.spoiler(translation));
        embedBuilder.addField("Cost 💵", cost + "$", true);
        embedBuilder.addField("Counter Attack", counter + "", true);
        embedBuilder.addField("Attack Type", attackType + "", true);
        embedBuilder.addField("Attack power up", attackPowerUp + "", true);
        embedBuilder.addField("Defence power up", defencePowerUp + "", true);
        embedBuilder.addField("Speed power up", speedPowerUp + "", true);
        embedBuilder.addField("Ki consumption", kiConsumption + "", true);
        if (getForcedRace() != null)
            embedBuilder.addField("Only for race: ", forcedRace, true);
        embedBuilder.addField("Description", description, false);
        embedBuilder.setImage(getGif());

//        return embedBuilder;
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

    public WordsColor getColor() {
        return color;
    }

    public void setColor(WordsColor color) {
        this.color = color;
    }
}
