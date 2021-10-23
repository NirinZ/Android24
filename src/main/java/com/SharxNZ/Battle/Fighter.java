package com.SharxNZ.Battle;

import com.SharxNZ.Android24;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Game.Attack;
import com.SharxNZ.Gifs.ActionGif;
import com.SharxNZ.Gifs.Gif;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Fighter extends Stats {

    protected long health = super.health;
    protected long ki = super.ki;
    protected long strikeAttack = super.strikeAttack;
    protected long kiAttack = super.kiAttack;
    protected long defence = super.defence;
    protected long speed = super.speed;

    protected Fighter target;
    protected Attack attack;
    protected final ArrayList<String> specialAttacks = new ArrayList<>();

    protected static final Random rand = new Random();


    public Fighter(long userID) {
        super(userID, true);
        try (
                Connection con = Android24.getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT AttackAbbreviated FROM android24.users_attacks WHERE UserID = ?;")
        ) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();
            specialAttacks.add("Strike");
            specialAttacks.add("Ki");
            specialAttacks.add("Defence");
            while (resultSet.next())
                specialAttacks.add(resultSet.getString(1));
        } catch (SQLException throwables) {
            Android24.logError(throwables);
        }
    }

    protected void resetStats() {
        System.out.println("TN :" + transformation.getName());
        strikeAttack = super.strikeAttack * transformation.getAttackPowerUp();
        kiAttack = super.kiAttack * transformation.getAttackPowerUp();
        defence = super.defence * transformation.getDefencePowerUp();
        speed = super.speed * transformation.getSpeedPowerUp();
    }

    /**
     * @return power
     */
    public short takeDamage(long damage) {
        if (damage >= 0) {
            health -= damage;
            return (short) Math.max(0, Math.min(100, 100 * damage / (super.health / 4)));
        } else return 0;
    }

    public Fighter getTarget() {
        return target;
    }

    public void setTarget(Fighter target) {
        this.target = target;
    }

    public Attack getAttack() {
        return attack;
    }

    @Nullable
    public Gif setAttack(@NotNull Attack attack) {
        resetStats();
        if (attack.getAttackType() != Attack.ATTACK_TYPE.Charge)
            ki -= (long) attack.getKiConsumption() * transformation.getKiConsumption();
        switch (attack.getAttackType()) {
            case Strike -> strikeAttack *= attack.getAttackPowerUp();
            case Ki -> kiAttack *= attack.getAttackPowerUp();
            case Charge -> ki = Math.min((ki + super.ki / 15), super.ki);

        }
        defence *= attack.getDefencePowerUp();
        speed *= attack.getSpeedPowerUp();
        this.attack = attack;
        return ActionGif.getActionGif(race, transformation.getAbbreviated(), attack.getAbbreviated());
    }

    public void randomizeStats() {
        strikeAttack *= rand.nextDouble() + 1;
        kiAttack *= rand.nextDouble() + 1;
        defence *= rand.nextDouble() + 1;
        speed *= rand.nextDouble() + 1;
    }

    @NotNull
    protected static String getStatBar(long part, long full, String chr) {
        part = 10 * (Math.max(part, 0)) / full;
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < part; i++) {
            bar.append(chr);
        }
        for (int i = 0; i < 10 - part; i++) {
            bar.append("⬛");
        }
        return bar.toString();
    }

    public MessageEmbed currentStats() {
        EmbedBuilder builder = new EmbedBuilder();
        if (transformation.getName() != null) {
            builder.setDescription(transformation.getName());
            builder.setColor(transformation.getColor());
        }
        builder.setTitle(name);
        builder.addField("Health", health + "/" + super.health + "\n" + getStatBar(health, super.health, "🟩"), false);
        builder.addField("Ki", ki + "/" + super.ki + "\n" + getStatBar(ki, super.ki, "🟦"), false);
        return builder.build();
    }

    @Override
    public String toString() {
        return "Fighter{" +
                "name=" + name +
                ", userID=" + userID +
                ", attack=" + attack.getName() +
                ", target=" + target.name +
                ", health=" + health +
                ", ki=" + ki +
                ", strikeAttack=" + strikeAttack +
                ", kiAttack=" + kiAttack +
                ", defence=" + defence +
                ", speed=" + speed +
                ", transformation='" + transformation + '\'' +
                '}';
    }

    public ArrayList<String> getSpecialAttacks() {
        return specialAttacks;
    }

}
