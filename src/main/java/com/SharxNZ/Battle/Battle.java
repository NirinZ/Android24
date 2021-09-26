package com.SharxNZ.Battle;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Attack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import javax.naming.NameNotFoundException;
import java.sql.SQLException;
import java.util.*;

enum TurnType {
    Attack, Defense
}

public class Battle {

    private long channelId;
    private HashSet<Long> fightersSet;
    private Queue<Fighter> attackOrder;
    private TurnType turnType = TurnType.Attack;

    private static HashMap<Long, Battle> battles;

    public Battle(Guild guild, User @NotNull ... users) {
        ChannelAction<TextChannel> channel = guild.getCategoryById(890945913734971472L).createTextChannel(users[0].getName() + " vs " + users[1].getName());
        for (User user : users) {
            fightersSet.add(user.getIdLong());
            attackOrder.add(new Fighter(user.getIdLong()));
            channel.addMemberPermissionOverride(user.getIdLong(), EnumSet.of(Permission.MESSAGE_WRITE), null);
        }
        channel.queue(textChannel -> {
            String mention = "";
            Arrays.stream(users).forEach(user -> mention.concat(user.getAsMention()));
            textChannel.sendMessage("The battle started!\n" + mention).queue();
        });
        battles.put(channelId, this);
    }

    public String turn(long fighterId, String action) {
        Fighter fighter = turnType == TurnType.Attack ? attackOrder.peek() : attackOrder.peek().getTarget();

        if (fighter.getUserID() != fighterId)
            return "It's not your turn!";

        if (turnType == TurnType.Attack && action.equals("Defense")) {
            return "You can't defence in the attack phase";
        }

        try {
            Attack attack = new Attack(action);
            if(attack.getKiConsumption() > fighter.getKi())
                return "You don't have enough ki to use this attack!";
            fighter.
            fighter.setAttack(attack);
            if(turnType == TurnType.Attack)
                turnType = TurnType.Defense;
            else
                attackOrder.add(attackOrder.poll());
            return battleTurn();

        } catch (NameNotFoundException e) {
            return "This attack doesn't exists";
        } catch (SQLException e) {
            Android24.logError(e);
            e.printStackTrace();
            return "Some error has accrue";
        }
    }

    private String battleTurn() {

    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public HashSet<Long> getFightersSet() {
        return fightersSet;
    }

    public void setFightersSet(HashSet<Long> fightersSet) {
        this.fightersSet = fightersSet;
    }

    public Queue<Fighter> getAttackOrder() {
        return attackOrder;
    }

    public void setAttackOrder(Queue<Fighter> attackOrder) {
        this.attackOrder = attackOrder;
    }

    public TurnType getTurnType() {
        return turnType;
    }

    public void setTurnType(TurnType turnType) {
        this.turnType = turnType;
    }

    public static Battle getBattle(long channelId) {
        return battles.get(channelId);
    }

}
