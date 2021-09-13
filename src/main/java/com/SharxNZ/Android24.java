package com.SharxNZ;

//Normal JAVA
import java.io.IOException;
import java.util.*;

//Discord (JDA/JDA utilities)
import com.SharxNZ.Buttons.PPButtons;
import com.SharxNZ.Buttons.ShopButton;
import com.SharxNZ.Commands.*;
import com.SharxNZ.Commands.GameCommands.*;
import com.SharxNZ.GameFunctions.GFButtons;
import com.SharxNZ.GameFunctions.StartGame;
import com.SharxNZ.GameFunctions.Beginning;
import com.SharxNZ.GameFunctions.XP;
import com.SharxNZ.Slash.SlashCommands;
import com.SharxNZ.Utilities.Embeds;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.commons.dbcp2.BasicDataSource;

//SQL
import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.security.auth.login.LoginException;

public abstract class Android24 {

    public static JDA jda;
    private static CommandListUpdateAction commandListDebug;
    private static CommandListUpdateAction commandListAll;
    public static String prefix = "!";
    public static float difficulty = 0.3f;
    public static final long debugChannelID = 728653495900569603L;
    private static final long cacheChannelID = 866689902758068244L;
    private static final BasicDataSource dataSource = new BasicDataSource();

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }


    public static void logError(Exception throwables){
        jda.getTextChannelById(debugChannelID).sendMessage(throwables.toString()).queue();
    }

    public static void log(String log){
        jda.getTextChannelById(debugChannelID).sendMessage(log + "").queue();
    }

    public static void getImageUrl(byte[] image, AtomicReference<String> value){
        EmbedBuilder wrapper = new EmbedBuilder();

        value.set(
        Android24.jda.getTextChannelById(Android24.cacheChannelID)
                .sendFile(image, "png.png")
                .setEmbeds(wrapper.setImage("attachment://png.png").build())
                .complete().getEmbeds().get(0).getImage().getUrl());
    }

    public static void addCommand(CommandData commandData){
        commandListDebug.addCommands(commandData);
        commandListAll.addCommands(commandData);
    }

    private static void queueCommands(){
        commandListDebug.queue();
        commandListAll.queue();
    }

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, IOException {

        //System.out.println(ASaiyan.getSpeed("45656"));

        //System.exit(9);

        // Set the data source for the SQL connection
        dataSource.setUrl("jdbc:mysql://159.89.111.155:3306/?user=Android24");
        dataSource.setUsername("Android24");
        dataSource.setPassword(System.getenv("MySQLPass"));
        dataSource.setMinIdle(1);
        dataSource.setMaxIdle(3);
        dataSource.setMaxTotal(50);


        List<GatewayIntent> gatewayIntents = new ArrayList<>();


        JDABuilder jdaBuilder = JDABuilder.createDefault(System.getenv("Android24Token"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.watching("24 GAMING's videos"));

        gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.enableIntents(gatewayIntents);

        jda = jdaBuilder.build();
        jda.awaitReady();

        commandListDebug = jda.getGuildById(728638053559828581L).updateCommands();
        commandListAll = jda.updateCommands();

        commandListDebug.addCommands(new CommandData("test", "test command"));

        jda.addEventListener(new Ping());
        jda.addEventListener(new XP());
        jda.addEventListener(new Beginning());
        jda.addEventListener(new Marco());
        jda.addEventListener(new SlashCommands());
        jda.addEventListener(new PPButtons());
        jda.addEventListener(new ShopButton());
        jda.addEventListener(new GFButtons());
        Stats.Stats();
        Level.Level();
        StartGame.StartGame();
        Embeds.Embeds();
        Shop.Shop();
        //jda.addEventListener(new SlashTest());

        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId("739532349280354404");
        commandClientBuilder.setPrefix(prefix);
        commandClientBuilder.setHelpWord("help");
        commandClientBuilder.addCommand(new Echo());
        commandClientBuilder.addCommand(new RefreshNames());
        commandClientBuilder.addCommand(new StartGameCommand());
        commandClientBuilder.addCommand(new GetStats());
        CommandClient commandClient = commandClientBuilder.build();
        jda.addEventListener(commandClient);

        // commandListUpdateAction = jda.updateCommands();
        queueCommands();

        jda.getTextChannelById(debugChannelID).sendMessage("אני דלוק").queue();
        // jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        // test 2
    }
}
