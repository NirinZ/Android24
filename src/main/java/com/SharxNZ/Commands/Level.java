package com.SharxNZ.Commands;

import com.SharxNZ.Android24;
import com.SharxNZ.Utilities.Graphics;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

public class Level extends Command {

    private final Statement sqlStatement = Android24.getConnection().createStatement();

    public Level() throws SQLException {
        super.name = "level";
        super.aliases = new String[]{"l", "lvl"};
        super.help = "Returns your level";
        super.arguments = "[The person you want to get the rank of (optional)]";
        Android24.commandListUpdateAction.addCommands(new CommandData(super.name.toLowerCase(), this.help)
                .addOptions(new OptionData(OptionType.BOOLEAN, "display", "display your level")));
        //super.category = new Category("XP");
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        short lvl;
        String arg = commandEvent.getArgs();
        commandEvent.reply(commandEvent.getGuild().getDefaultChannel().getName() + " || " + commandEvent.getGuild().getDefaultChannel().getId());
        if(arg.equals("")) {
            commandEvent.getAuthor().openPrivateChannel().queue(privateChannel -> {
//                privateChannel.sendMessage("On " + commandEvent.getGuild().getName() + " server: \nYour level is: " + lvl +
//                        "\nAnd your XP is: " + xp).queue();
                privateChannel.sendFile(returnLevel(commandEvent.getGuild().getId(),
                        commandEvent.getAuthor().getId(), commandEvent.getAuthor().getAvatarUrl()), "level.jpg").queue();
            });
        } else{
            String memberId  = commandEvent.getMessage().getMentionedMembers().get(0).getId();
            try {
                ResultSet resultLvl = sqlStatement.executeQuery(
                        "SELECT Level FROM `"+ commandEvent.getGuild().getId() +"`.users_data " +
                                "where UserID=" + memberId + ";");
                if(resultLvl.next()){
                lvl = resultLvl.getShort(1);
                commandEvent.reply(commandEvent.getMessage().getMentionedMembers().get(0).getEffectiveName() + " level is: " + Short.toString(lvl));
                } else{
                    commandEvent.reply("This user isn't in the system");
                }
                resultLvl.close();
            } catch (SQLException throwables) {
                commandEvent.reply(throwables.toString());
                throwables.printStackTrace();
            }
        }
    }
    public static byte[] returnLevel(String guildID, String userID, String userURL){
        try {
            Statement sqlStatement = Android24.getConnection().createStatement();
            ResultSet resultLvl = sqlStatement.executeQuery(
                    "SELECT Level, XP FROM `" + guildID + "`.users_data " +
                            "where UserID=" + userID + ";");
//            PreparedStatement pst = Android24.getConnection().prepareStatement(
//                    "SELECT Level, XP FROM `?`.users_data where UserID=?;");
//            pst.setString(1, guildID);
//            pst.setString(2, userID);
//            ResultSet resultLvl = pst.executeQuery();
            if(resultLvl.next()) {
                String guildName = Android24.jda.getGuildById(guildID).getName();
                short level = resultLvl.getShort(1);
                long xp = resultLvl.getLong(2);
                //sqlStatement.close();
                return Graphics.levelImage(userURL, guildName, level, xp);
            }
            else return null;
        } catch (Exception throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }

    public static MessageEmbed returnLevelEmbed(String guildID, String userID, String userURL){
        AtomicReference<String> imageUrl = new AtomicReference<>();
        Android24.getImageUrl(Level.returnLevel(guildID, userID, userURL), imageUrl);
        return new EmbedBuilder().setImage(imageUrl.get()).build();
    }
}
