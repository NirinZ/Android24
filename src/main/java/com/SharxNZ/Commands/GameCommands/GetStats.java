package com.SharxNZ.Commands.GameCommands;


import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Transformation;
import com.SharxNZ.Utilities.Embeds;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.naming.NameNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.SharxNZ.Android24.*;

public class GetStats extends Command {

    public GetStats() {
        super.name = "stats";
        super.aliases = new String[]{"st"};
        super.help = "Display your stats";
        Android24.addCommands(new CommandData(super.name.toLowerCase(), this.help)
                .addOptions(new OptionData(OptionType.BOOLEAN, "display", "display your stats")));
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply(getStats(Being.getBeing(commandEvent.getAuthor().getIdLong())));
    }

    public static String getStats(Being being) {

    /*            long[] stats = new long[]{
                        ASaiyan.getHealth(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getStrikeAttack(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getKiAttack(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getDefence(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getSpeed(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId())
                };*/
        long[] stats = new long[6];
        try (
                Connection con = Android24.getConnection();
                PreparedStatement raceStats = con.prepareStatement(
                        "SELECT * FROM android24.races WHERE RaceName = ?;")
        ) {

            raceStats.setString(1, being.getRace());
            ResultSet resultSet = raceStats.executeQuery();
            if (!resultSet.next())
                return "No there is no a race like this";

            stats[0] = ((long) being.getHealth() + resultSet.getShort(2)) * being.getLevel();
            stats[1] = ((long) being.getKi() + resultSet.getShort(3)) * being.getLevel();
            stats[2] = ((long) being.getStrikeAttack() + resultSet.getShort(4)) * being.getLevel();
            stats[3] = ((long) being.getKiAttack() + resultSet.getShort(5)) * being.getLevel();
            stats[4] = ((long) being.getDefence() + resultSet.getShort(6)) * being.getLevel();
            stats[5] = ((long) being.getSpeed() + resultSet.getShort(7)) * being.getLevel();

            raceStats.close();
            return ("Your health: " + stats[0] +
                    "\nYour ki: " + stats[1] +
                    "\nYour strike attack: " + stats[2] +
                    "\nYour ki attack: " + stats[3] +
                    "\nYour defence: " + stats[4] +
                    "\nYour speed: " + stats[5]
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logError(throwables);
            return "An error in the execute";
        }
    }

    public static MessageEmbed statsEmbed(User user) {
        try {
            Being being = Being.getBeing(user.getIdLong());
            EmbedBuilder generalEmbed = new EmbedBuilder();
            generalEmbed.setTitle(user.getName());
            if (being.getCurrentTrans() == null)
                generalEmbed.setDescription(being.getRace());
            else {
                generalEmbed.setDescription(being.getRace() + " - " + being.getCurrentTrans());
                generalEmbed.setColor(new Transformation(being.getCurrentTrans()).getColor());
            }
            generalEmbed.addField("Level", being.getLevel() + "", true);
            generalEmbed.addField("Zeni", being.getZeni() + "$", true);
            generalEmbed.addField("Power Points", being.getPowerPoints() + "", true);
            Stats stats = being.getStats();
            generalEmbed.addField("Health", stats.getHealth() + "", true);
            generalEmbed.addField("Ki", stats.getKi() + "", true);
            generalEmbed.addField("Strike Attack", stats.getStrikeAttack() + "", true);
            generalEmbed.addField("Ki Attack", stats.getKiAttack() + "", true);
            generalEmbed.addField("Defence", stats.getDefence() + "", true);
            generalEmbed.addField("Speed", stats.getSpeed() + "", true);
            generalEmbed.setFooter("The stats of " + user.getAsTag(), user.getAvatarUrl());

            return generalEmbed.build();
        } catch (NameNotFoundException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return Embeds.errorEmbed();
        }
    }
}
