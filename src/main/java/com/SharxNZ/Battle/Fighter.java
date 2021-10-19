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

    private final long baseHealth = super.health;
    private final long baseKi = super.ki;
    private final long baseStrikeAttack = super.strikeAttack;
    private final long baseKiAttack = super.kiAttack;
    private final long baseDefence = super.defence;
    private final long baseSpeed = super.speed;

    private Fighter target;
    private Attack attack;
    private final ArrayList<String> specialAttacks = new ArrayList<>();

    private static final Random rand = new Random();


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
            throwables.printStackTrace();
        }
    }

    private void resetStats() {
        System.out.println("TN :" + transformation.getName());
        strikeAttack = baseStrikeAttack * transformation.getAttackPowerUp();
        kiAttack = baseKiAttack * transformation.getAttackPowerUp();
        defence = baseDefence * transformation.getDefencePowerUp();
        speed = baseSpeed * transformation.getSpeedPowerUp();
    }

    /**
     * @return power
     */
    public short takeDamage(long damage) {
        if (damage >= 0) {
            health -= damage;
            return (short) Math.max(0, Math.min(100, 100 * damage / (baseHealth / 4)));
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
            ki -= attack.getKiConsumption() * transformation.getKiConsumption();
        switch (attack.getAttackType()) {
            case Strike -> strikeAttack *= attack.getAttackPowerUp();
            case Ki -> kiAttack *= attack.getAttackPowerUp();
            case Charge -> ki = Math.min((ki + baseKi / 15), baseKi);

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

    private static String getStatBar(long part, long full, String chr) {
        part = 10 * (Math.max(part, 0)) / full;
        String bar = "";
        for (int i = 0; i < part; i++) {
            bar += chr;
        }
        for (int i = 0; i < 10 - part; i++) {
            bar += "⬛";
        }
        return bar;
    }

    public MessageEmbed currentStats() {
        EmbedBuilder builder = new EmbedBuilder();
        if (transformation.getName() != null) {
            builder.setDescription(transformation.getName());
            builder.setColor(transformation.getColor());
        }
        builder.setTitle(name);
        builder.addField("Health", health + "/" + baseHealth + "\n" + getStatBar(health, baseHealth, "🟩"), false);
        builder.addField("Ki", ki + "/" + baseKi + "\n" + getStatBar(ki, baseKi, "🟦"), false);
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
