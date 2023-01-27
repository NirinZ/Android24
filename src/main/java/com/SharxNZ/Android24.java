package com.SharxNZ;

//Normal JAVA

//Discord (JDA/JDA utilities)

import com.SharxNZ.Battle.Battle;
import com.SharxNZ.Buttons.BattleButtons;
import com.SharxNZ.Buttons.PPButtons;
import com.SharxNZ.Buttons.ShopButton;
import com.SharxNZ.Buttons.WordsButtons;
import com.SharxNZ.Commands.*;
import com.SharxNZ.Commands.GameCommands.GetStats;
import com.SharxNZ.Commands.GameCommands.Inventory;
import com.SharxNZ.Commands.GameCommands.StartGameCommand;
import com.SharxNZ.Commands.ModeretionCommands.*;
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
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class Android24 {

    public static final long debugChannelID = 887426748306825217L;
    public static final long nirinsChannelID = 882524002516598784L;
    public static final long cacheChannelID = 866689902758068244L;
    private static final String branch = "Words";
    private static final String commitId = "";
    private static final long nirinId = 739532349280354404L;
    private static final HikariDataSource dataSource = new HikariDataSource();
    private static final HikariDataSource wordsDataSource = new HikariDataSource();
    public static JDA jda;
    public static String prefix = "!";
    public static float difficulty = 0.4f; // should be 0.3
    public static short xp = 50; // should be 20
    public static User nirin;
    public static EventWaiter eventWaiter = new EventWaiter();
    private static CommandListUpdateAction commandListDebug;
    private static CommandListUpdateAction commandListAll;

    public static String getCommitName(){
        return branch + ": " + commitId;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            logError(throwables);
            return null;
        }
    }

    public static Connection getWordsConnection() {
        try {
            return wordsDataSource.getConnection();
        } catch (SQLException throwables) {
            logError(throwables);
            return null;
        }
    }

    public static void logError(@NotNull Exception throwable) {
        throwable.printStackTrace();
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement stack : throwable.getStackTrace())
            builder.append(stack).append("\n");
        if (builder.length() + throwable.toString().length() < 1950)
            jda.getTextChannelById(debugChannelID).sendMessage("<@" + nirinId + ">\n" + throwable + "\nStack Trace:\n"
                    + builder).queue();
        else
            jda.getTextChannelById(debugChannelID).sendMessage("<@" + nirinId + ">\n" + throwable + "\nStack Trace:\n"
                    + builder.substring(0, 1000)).queue();
    }

    public static void logError(@NotNull Exception throwables, String text) {
        throwables.printStackTrace();
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement stack : throwables.getStackTrace())
            builder.append(stack).append("\n");
        jda.getTextChannelById(debugChannelID).sendMessage("<@" + nirinId + ">\n" + "Text:\n" + text + "\n----------\n"
                + throwables + "\nStack Trace:\n" + builder).queue();
    }

    public static void log(String log) {
        jda.getTextChannelById(debugChannelID).sendMessage(log).queue();
    }

    public static void configureCache(@NotNull JDABuilder builder) {
        // Cache members who are in a voice channel
        MemberCachePolicy policy = MemberCachePolicy.VOICE;
        // Cache members who are in a voice channel
        // AND are also online
        policy = policy.and(MemberCachePolicy.ONLINE);
        // Cache members who are in a voice channel
        // AND are also online
        // OR are the owner of the guild
        policy = policy.or(MemberCachePolicy.OWNER);
        // policy.cacheMember(jda.getGuildById(728638053559828581L).retrieveMemberById(nirinId).complete());
        builder.setMemberCachePolicy(policy);
        // builder.setChunkingFilter(ChunkingFilter.ALL); I can use that to cache ALL the members
    }

    public static void addCommands(CommandData... commandData) {
        commandListDebug.addCommands(commandData);
        commandListAll.addCommands(commandData);
    }

    private static void queueCommands() {
        commandListDebug.queue();
        commandListAll.queue();
    }

    //todo work on the find function

    /**
     * Important:
     * הגיפים לא עובדים, צריך לסדר אותם
     *
     *
     *
     * Improvements:
     * 1)
     * ניתן לשפר ביצועים אם אני אעשה אבדוק אם אני יכול לעשות get במקום retrieve.
     * כל מה שאני צריך לעשות זה להחליף את כל הקוד בפונציות שמקבלות את ה- user/ member ולעשות:
     * if(getUser()!= null){
     * function(user)
     * } else{
     * retrieveUser.queue(user -> function(user))
     * }
     * <p>
     * 2)
     * צריך ליעל את כל הקוד ולשפר ביצועים ולעשות שהוא יהיה יותר נקיא, נהיר וטוב.
     * שיהיו כמה שפחות SQL
     *
     *
     * Bags:
     * אם אני מתפתח משרת אחד, ושוב עושה את זה אבל משרת אחר, הבוט לא יודע להוריד לי את הרול לבד...
     * נגיד אם אני הופך משרת 1 ל ססג ואז משרת שני לססב ובשרת 1 הופך לסס3 הבוט לא ידע להוריד לי את הרול של ססג בגלל שאני לא רשום כססג אלא כססב
     *
     * כשהמשחק ישתחרר בצורת בטא צריך לעשות באלאנס דחוףףף, כרגע המשחק ממש לא כיף ונגמר בone shot אפילו אם יש הבדל כח קטן
     * בגלל שכל הרמות כח עובדים בכפולות
     *
     * Others:
     * ללמוד איך להשתמש ב- לוגר של JDA ו-HikariPool
     *
     */

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, ImageProcessingException, IOException {

//        String t2 = "כגק";
//
//        System.out.println();
//
//        System.exit(9);

        // java --enable-preview -jar bot-b.jar > log.log

        // Set the database
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://159.89.111.155:3306/?user=Android24");
        dataSource.setUsername("Android24");
        dataSource.setPassword(System.getenv("MySQLPass"));
        dataSource.setMinimumIdle(6);

        // Words Database
        wordsDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        wordsDataSource.setJdbcUrl("jdbc:mysql://sql11.freesqldatabase.com/?user=sql11423530?currentSchema=sql11423530");
        wordsDataSource.setUsername("sql11423530");
        wordsDataSource.setPassword(System.getenv("WordsDBpass"));
        wordsDataSource.setSchema("sql11423530");
        wordsDataSource.setMinimumIdle(3);


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
        jda.addEventListener(new WordsButtons());
        jda.addEventListener(eventWaiter);
        Level.Level();
        StartGame.StartGame();
        Inventory.start();
        AddingCommands.AddingCommands();
        Scams.Scams();

        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId("739532349280354404");
        commandClientBuilder.setPrefix(prefix);
        commandClientBuilder.setHelpWord("help");

        commandClientBuilder.addCommand(new Echo());
        commandClientBuilder.addCommand(new RefreshNames());
        commandClientBuilder.addCommand(new LeaveServer());
        commandClientBuilder.addCommand(new AllInvites());
        commandClientBuilder.addCommand(new CleanDatabase());
        commandClientBuilder.addCommand(new StartGameCommand());
        commandClientBuilder.addCommand(new GetStats());
        commandClientBuilder.addCommand(new refreshRoles());
        commandClientBuilder.addCommand(new AddTransGif());
        commandClientBuilder.addCommand(new IP());
        commandClientBuilder.addCommand(new SysCommand());

        CommandClient commandClient = commandClientBuilder.build();
        jda.addEventListener(commandClient);

        queueCommands();

        Battle.battlesCleanup();
        jda.getTextChannelById(nirinsChannelID).sendMessage(getCommitName()).queue();

    }
}
