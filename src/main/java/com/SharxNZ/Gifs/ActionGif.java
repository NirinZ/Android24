package com.SharxNZ.Gifs;

import com.SharxNZ.Game.Attack;
import com.SharxNZ.Game.Race;
import com.SharxNZ.Game.Transformation;
import com.drew.imaging.ImageProcessingException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class ActionGif extends Gif{
    protected String transformation;
    protected String attack;

    public ActionGif(@NotNull Race race, @NotNull Transformation transformation, @NotNull Attack attack, String link) throws ImageProcessingException, IOException {
        this.setGifAnimatedTimeLengthFromUrl(link);
        this.race = race.getName();
        this.transformation = transformation.getAbbreviated();
        this.attack = attack.getAbbreviated();
    }


    public static void checkGif(@NotNull ActionGif gif, User user, String id) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Transformation Gif");
        builder.addField("Race", gif.race, false);
        builder.addField("Transformation", gif.transformation != null ? gif.transformation : "base", false);
        builder.addField("Attack", gif.attack, false);
        builder.addField("Length", String.valueOf(gif.length), false);
        builder.setImage(gif.link);
        builder.setColor(Color.YELLOW);
        builder.setFooter("Sent by: " + user.getAsTag(), user.getAvatarUrl());

        sendGifCheck(builder.build(), gif, user, id);

    }

    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }
}
