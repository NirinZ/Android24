package com.SharxNZ.Commands.GameCommands;


import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Saiyan;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.SharxNZ.Android24.debugChannelID;
import static com.SharxNZ.Android24.jda;

public class GetStats extends Command {

    public GetStats(){
        super.name = "getStats";
        super.aliases = new String[]{"gs"};
        super.help = "Display you stats";
        Android24.commandListUpdateAction.addCommands(new CommandData(super.name.toLowerCase(), this.help)
                .addOptions(new OptionData(OptionType.BOOLEAN, "display", "display your stats")));
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply(getStats(Being.getBeing(commandEvent.getAuthor().getIdLong())));
    }
    public static String getStats(Being being){

    /*            long[] stats = new long[]{
                        ASaiyan.getHealth(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getStrikeAttack(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getKiAttack(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getDefence(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId()),
                        ASaiyan.getSpeed(commandEvent.getGuild().getId(), commandEvent.getAuthor().getId())
                };*/
        long[] stats = new long[6];
        switch (being.getRace()) {
            case Saiyan:
                stats[0] = ((long)being.getHealth() + Saiyan.baseHealth) * being.getLevel();
                stats[1] = ((long)being.getKi() + Saiyan.baseKi) * being.getLevel();
                stats[2] = ((long)being.getStrikeAttack() + Saiyan.baseStrikeAttack) * being.getLevel();
                stats[3] = ((long)being.getKiAttack() + Saiyan.baseKiAttack) * being.getLevel();
                stats[4] = ((long)being.getDefence() + Saiyan.baseDefence) * being.getLevel();
                stats[5] = ((long) being.getSpeed() + Saiyan.baseSpeed) * being.getLevel();
                break;
            default:
                jda.getTextChannelById(debugChannelID).sendMessage("I got to default").queue();
                jda.getTextChannelById(debugChannelID).sendMessage(being.getRace().toString()).queue();
        }
        return ("Your health: " + stats[0] +
                "\nYour ki: " + stats[1] +
                "\nYour strike attack: " + stats[2] +
                "\nYour ki attack: " + stats[3] +
                "\nYour defence: " + stats[4] +
                "\nYour speed: " + stats[5]
        );
    }
//    public static Being getStats(String guildID, String userID) throws SQLException {
//        Statement sql = Android24.getSql();
//        String query = "SELECT up.*, Level " +
//                "FROM `"+ guildID +"`.users_power up " +
//                "join users_data ud " +
//                "on ud.UserID = up.UserID " +
//                "where ud.UserID="+ userID +";";
//        ResultSet resultSet = sql.executeQuery(query);
//        if (resultSet.next()){
//            // resultSet.getMetaData().getColumnCount()
//            return new Being(resultSet.getLong(1),
//                    resultSet.getString(2),
//                    resultSet.getShort(3),
//                    resultSet.getShort(4),
//                    resultSet.getShort(5),
//                    resultSet.getShort(6),
//                    resultSet.getShort(7),
//                    resultSet.getShort(8)
//                    resultSet.getShort(9));
//        }
//        return new Being();
//
//
//    }
}
