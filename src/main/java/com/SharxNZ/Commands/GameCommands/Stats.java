package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Utilities.Graphics;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import static com.SharxNZ.Android24.logError;

public class Stats extends Being {

    private static PreparedStatement raceStats;

    static {
        try {
            raceStats = Android24.getConnection().prepareStatement(
                    "SELECT * FROM android24.races WHERE RaceName = ?;");
        } catch (SQLException throwables) {
            logError(throwables);
            throwables.printStackTrace();
        }
    }

    public Stats(long userID){
        super(userID);
        try {
            raceStats.setString(1, super.getRace());
            ResultSet resultSet = raceStats.executeQuery();
            if (!resultSet.next())
                throw new Exception("Not a legal race");

            this.health.set((super.getHealth() + resultSet.getShort(2)) * super.getLevel());
            this.ki.set((super.getKi() + resultSet.getShort(3)) * super.getLevel());
            this.strikeAttack.set((super.getStrikeAttack() + resultSet.getShort(4)) * super.getLevel());
            this.kiAttack.set((super.getKiAttack() + resultSet.getShort(5)) * super.getLevel());
            this.defence.set((super.getDefence() + resultSet.getShort(6)) * super.getLevel());
            this.speed.set((super.getSpeed() + resultSet.getShort(7)) * super.getLevel());

        } catch (Exception throwables) {
            logError(throwables);
            throwables.printStackTrace();

        }
    }

}
