package com.SharxNZ;

//Normal JAVA
import java.io.IOException;
import java.util.*;

//Discord (JDA/JDA utilities)
import com.SharxNZ.Commands.*;
import com.SharxNZ.Commands.GameCommands.GetStats;
import com.SharxNZ.Commands.GameCommands.StartGame;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Commands.GameCommands.StartGameCommand;
import com.SharxNZ.GameFunctions.Beginning;
import com.SharxNZ.GameFunctions.Levels;
import com.SharxNZ.Slash.ButtonClick;
import com.SharxNZ.Slash.SlashCommands;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

//SQL
import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.security.auth.login.LoginException;

public class Daishinkan{

    public static JDA jda;
    public static CommandClientBuilder commandClientBuilder;
    public static CommandListUpdateAction commandListUpdateAction;
    public static String prefix = "!";
    public static float difficulty = 0.3f;
    public static final long debugChannelID = 728653495900569603L;
    private static final long cacheChannelID = 866689902758068244L;

    public static Statement getStatement() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/?user=Daishinkan";
        String uname = "Daishinkan";
        String password = System.getenv("MySQLPass");
        Connection con = DriverManager.getConnection(url, uname, password);
        return con.createStatement();
    }

    public static void logError(Exception e){
        jda.getTextChannelById(debugChannelID).sendMessage(e.toString()).queue();
    }

    public static void getImageUrl(byte[] image, AtomicReference<String> value){
        EmbedBuilder wrapper = new EmbedBuilder();

        value.set(
        Daishinkan.jda.getTextChannelById(Daishinkan.cacheChannelID)
                .sendFile(image, "png.png")
                .setEmbeds(wrapper.setImage("attachment://png.png").build())
                .complete().getEmbeds().get(0).getImage().getUrl());
    }

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, IOException {

        //System.out.println(ASaiyan.getSpeed("45656"));

        //System.exit(9);

        List<GatewayIntent> gatewayIntents = new ArrayList<>();

        JDABuilder jdaBuilder = JDABuilder.createDefault(System.getenv("DaishinkenToken"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.watching("over Zen-ho sana"));

        gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.enableIntents(gatewayIntents);

        jda = jdaBuilder.build();
        jda.awaitReady();

        commandListUpdateAction = jda.getGuildById(728638053559828581L).updateCommands();

        jda.addEventListener(new Ping());
        jda.addEventListener(new Levels());
        jda.addEventListener(new Beginning());
        jda.addEventListener(new Marco());
        jda.addEventListener(new SlashCommands());
        jda.addEventListener(new ButtonClick());
        Stats.Start();
        StartGame.StartGame();
        //jda.addEventListener(new SlashTest());

        commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId("739532349280354404");
        commandClientBuilder.setPrefix(prefix);
        commandClientBuilder.setHelpWord("help");
        commandClientBuilder.addCommand(new Echo());
        commandClientBuilder.addCommand(new RefrashNames());
        commandClientBuilder.addCommand(new Level());
        commandClientBuilder.addCommand(new StartGameCommand());
        commandClientBuilder.addCommand(new GetStats());
        CommandClient commandClient = commandClientBuilder.build();
        jda.addEventListener(commandClient);

        // commandListUpdateAction = jda.updateCommands();
        commandListUpdateAction.queue();

        jda.getTextChannelById(debugChannelID).sendMessage("אני דלוק").queue();
        // jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
//hi world
    }
}
