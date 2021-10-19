package com.SharxNZ;

//Normal JAVA

//Discord (JDA/JDA utilities)
import com.SharxNZ.Buttons.BattleButtons;
import com.SharxNZ.Buttons.PPButtons;
import com.SharxNZ.Buttons.ShopButton;
import com.SharxNZ.Commands.*;
import com.SharxNZ.Commands.GameCommands.GetStats;
import com.SharxNZ.Commands.GameCommands.Inventory;
import com.SharxNZ.Commands.GameCommands.StartGameCommand;
import com.SharxNZ.Commands.ModeretionCommands.refreshRoles;
import com.SharxNZ.GameFunctions.Beginning;
import com.SharxNZ.GameFunctions.GFButtons;
import com.SharxNZ.GameFunctions.StartGame;
import com.SharxNZ.GameFunctions.XP;
import com.SharxNZ.Slash.AddingCommands;
import com.SharxNZ.Slash.SelectMenuEvents;
import com.SharxNZ.Slash.SlashCommandEvents;
import com.SharxNZ.Utilities.AddTransGif;
import com.drew.imaging.ImageProcessingException;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

//SQL
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


public abstract class Android24 {

    public static JDA jda;
    public static String prefix = "!";
    public static float difficulty = 0.4f; // should be 0.3
    public static short xp = 50; // should be 20
    public static final long debugChannelID = 887426748306825217L;
    private static final long nirinId = 739532349280354404L;
    public static User nirin;
    public static EventWaiter eventWaiter = new EventWaiter();
    private static CommandListUpdateAction commandListDebug;
    private static CommandListUpdateAction commandListAll;
    public static final long cacheChannelID = 866689902758068244L;
    private static final HikariDataSource dataSource = new HikariDataSource();

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
        jda.getTextChannelById(debugChannelID).sendMessage(nirin.getAsMention()+"\n"+throwables.toString()).queue();
    }

    public static void log(String log){
        jda.getTextChannelById(debugChannelID).sendMessage(log).queue();
    }

    public static void configureCache(JDABuilder builder) {
        // Cache members who are in a voice channel
        MemberCachePolicy policy = MemberCachePolicy.VOICE;
        // Cache members who are in a voice channel
        // AND are also online
        policy = policy.and(MemberCachePolicy.ONLINE);
        // Cache members who are in a voice channel
        // AND are also online
        // OR are the owner of the guild
        policy = policy.or(MemberCachePolicy.OWNER);
        //policy.cacheMember(jda.getGuildById(728638053559828581L).retrieveMemberById(nirinId).complete());
        builder.setMemberCachePolicy(policy);
    }

    public static void addCommands(CommandData... commandData){
        commandListDebug.addCommands(commandData);
        commandListAll.addCommands(commandData);
    }

    private static void queueCommands(){
        commandListDebug.queue();
        commandListAll.queue();
    }

    //אפשר לשלוח קבצים באפמרל!!!! לסדר דחוף את הפוור פוינטס

    /**
     *
     *
     * Improvements:
     * 1)
     * ניתן לשפר ביצועים אם אני אעשה אבדוק אם אני יכול לעשות get במקום retrieve.
     * כל מה שאני צריך לעשות זה להחליף את כל הקוד בפונציות שמקבלות את ה- user/ member ולעשות:
     *if(getUser()!= null){
     *     function(user)
     *} else{
     *     retrieveUser.queue(user -> function(user))
     *}
     *
     * 2)
     * צריך ליעל את כל הקוד ולשפר ביצועים ולעשות שהוא יהיה יותר נקיא, נהיר וטוב.
     * שיהיו כמה שפחות SQL
     *
     * */

    // TODO: 16/10/2021 make the result gif sql
    // TODO: 18/10/2021 add the race = null option

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, ImageProcessingException, IOException {

//        String url = "https://c.tenor.com/aUIzEUjVdgIAAAAd/goku-mad.gif";
//
//        System.out.println(Gif.getGifAnimatedTimeLengthFromUrl(url));

//        System.exit(9);

        // Set the database
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://159.89.111.155:3306/?user=Android24");
        dataSource.setUsername("Android24");
        dataSource.setPassword(System.getenv("MySQLPass"));
        dataSource.setMinimumIdle(6);



        //!docs JDABuilder#setMemberCachePolicy
        List<GatewayIntent> gatewayIntents = new ArrayList<>();

        JDABuilder jdaBuilder = JDABuilder.createDefault(System.getenv("Android24Token"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.watching("24 GAMING's videos"));

        gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.enableIntents(gatewayIntents);
        configureCache(jdaBuilder);

        jda = jdaBuilder.build();
        jda.awaitReady();
        jda.retrieveUserById(nirinId).queue(user -> nirin = user);

        commandListDebug = jda.getGuildById(728638053559828581L).updateCommands();
        commandListAll = jda.updateCommands();

        commandListDebug.addCommands(new CommandData("test", "Test command"));

        jda.addEventListener(new Ping());
        jda.addEventListener(new XP());
        jda.addEventListener(new Beginning());
        jda.addEventListener(new Marco());
        jda.addEventListener(new SlashCommandEvents());
        jda.addEventListener(new SelectMenuEvents());
        jda.addEventListener(new PPButtons());
        jda.addEventListener(new ShopButton());
        jda.addEventListener(new GFButtons());
        jda.addEventListener(new BattleButtons());
        jda.addEventListener(eventWaiter);
        Level.Level();
        StartGame.StartGame();
        Inventory.start();
        AddingCommands.AddingCommands();
        Scams.Scams();
        //jda.addEventListener(new SlashTest());

        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId("739532349280354404");
        commandClientBuilder.setPrefix(prefix);
        commandClientBuilder.setHelpWord("help");

        commandClientBuilder.addCommand(new Echo());
        commandClientBuilder.addCommand(new RefreshNames());
        commandClientBuilder.addCommand(new StartGameCommand());
        commandClientBuilder.addCommand(new GetStats());
        commandClientBuilder.addCommand(new refreshRoles());
        commandClientBuilder.addCommand(new AddTransGif());

        CommandClient commandClient = commandClientBuilder.build();
        jda.addEventListener(commandClient);

        // commandListUpdateAction = jda.updateCommands();
        queueCommands();

        jda.getCategoryById(890945913734971472L).getTextChannels().stream().forEach(textChannel -> textChannel.delete().queue());
        jda.getTextChannelById(debugChannelID).sendMessage("אני דלוק").queue();
        // jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        // Test 2
    }
}
