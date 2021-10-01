package com.SharxNZ.Battle;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Attack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import javax.naming.NameNotFoundException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

enum TurnType {
    Attack, Defence
}

public class Battle {

    private long channelId;
    private final HashMap<Long, Fighter> fightersMap = new HashMap<>();
    private Queue<Fighter> attackOrder = new LinkedList<>();
    private TurnType turnType = TurnType.Attack;
    private byte passes;
    private Fighter attacker;
    private Fighter defender;

    private static final HashMap<Long, Battle> battles = new HashMap<>();

    public Battle(Guild guild, User @NotNull ... users) {
        ChannelAction<TextChannel> channel = guild.getCategoryById(890945913734971472L).createTextChannel(users[0].getName() + " vs " + users[1].getName());
        String mention = "";
        Fighter tempFighter;
        for (User user : users) {
            mention = mention.concat(user.getAsMention());
            tempFighter = new Fighter(user.getIdLong());
            fightersMap.put(user.getIdLong(), tempFighter);
            attackOrder.add(tempFighter);
            channel.addMemberPermissionOverride(user.getIdLong(), EnumSet.of(Permission.MESSAGE_WRITE), null);
        }
        tempFighter = attackOrder.poll();
        long firstFighter = tempFighter.getUserID();
        while (attackOrder.peek().getUserID() != firstFighter) {
            tempFighter.setTarget(attackOrder.peek());
            attackOrder.add(tempFighter);
            tempFighter = attackOrder.poll();
        }
        tempFighter.setTarget(attackOrder.peek());
        attackOrder.add(tempFighter);
        String finalMention = mention;
        channel.queue(textChannel -> {
            channelId = textChannel.getIdLong();
            battles.put(channelId, this);
            textChannel.sendMessage("The battle started!\n" + finalMention).queue();
        });
        attacker = attackOrder.poll();
        defender = attacker.getTarget();
        waiter();
    }

    private void waiter() {
        Android24.eventWaiter.waitForEvent(SlashCommandEvent.class, sce -> sce.getName().equals("battle")
                && !sce.getSubcommandName().equals("pvp") && channelId == sce.getChannel().getIdLong(), sce -> {
            switch (sce.getSubcommandName()) {
                case "action", "special_attack" -> {
                    long userID = sce.getUser().getIdLong();
                    if (getFightersMap().containsKey(userID)) {
                        sce.reply(turn(userID, sce.getOptionsByName("target").isEmpty() ? 0 : sce.getOption("target").getAsUser().getIdLong()
                                , sce.getOptions().get(0).getAsString())).queue();
                    } else {
                        sce.reply("You are not fighting in this battle").setEphemeral(true).queue();
                    }
                }
            }
            passes = 0;
            waiter();
        }, 1, TimeUnit.MINUTES, () -> {
            switch (turnType) {
                case Attack -> {
                    if (passes > battles.size() * 2) {
                        sendMessage("The battle is over due to inactivity");
                        end();
                        return;
                    }
                    sendMessage("You took to much time to respond and the turn have pass!");
                    nextTurn();
                    passes++;
                }
                case Defence -> {
                    defender.setAttack(new Attack(Attack.ATTACK_TYPE.Charge));
                    calculateTurn();
                }
            }
            waiter();
        });
    }

    public String turn(long fighterId, long targetId, String action) {
        Fighter fighter = turnType == TurnType.Attack ? attacker : defender;

        if (fighter.getUserID() != fighterId)
            return "It's not your turn!";

        if (turnType == TurnType.Attack && action.equals("Defence")) {
            return "You can't defence in the attack phase";
        }
        if (turnType == TurnType.Attack && action.equals("Charge")) {
            attacker.setAttack(new Attack(Attack.ATTACK_TYPE.Charge));
            nextTurn();
            return "You charged your energy!";
        }
        if (targetId != 0 && turnType == TurnType.Defence) {
            return "You can't set target on the defence phase";
        }
        if (targetId != 0 && !fightersMap.containsKey(targetId)) {
            return "this target is not in this battle!";
        } else if (targetId != 0)
            fighter.setTarget(fightersMap.get(targetId));

        try {
            Attack attack = new Attack(action);
            if (!fighter.getSpecialAttacks().contains(attack.getAbbreviated()))
                return "You don't have this attack!";
            if (attack.getKiConsumption() > fighter.getKi())
                return "You don't have enough ki to use this attack!";
            fighter.setAttack(attack);
            if (turnType == TurnType.Attack) {
                turnType = TurnType.Defence;
                return "You've been attacked! <@" + fighter.getTarget().getUserID() + ">\nWhat will you do?";
            }

            return calculateTurn();

        } catch (NameNotFoundException e) {
            return "This attack doesn't exists";
        } catch (SQLException e) {
            Android24.logError(e);
            e.printStackTrace();
            return "Some error has accrue";
        }
    }

    private void nextTurn() {
        attackOrder.add(attacker);
        attacker = attackOrder.poll();
        defender = attacker.getTarget();
        sendMessage("This is your turn " + attacker.getName());
    }

    private String calculateTurn() {

        switch (attacker.getAttack().getAttackType().toString() + defender.getAttack().getAttackType().toString()) {
            case "StrikeStrike" -> {
                StrikeStrike(attacker, defender);
                StrikeStrike(defender, attacker);
            }
            case "StrikeKi" -> StrikeKi(attacker, defender);
            case "KiStrike" -> StrikeKi(defender, attacker);
            case "StrikeDefence" -> StrikeDefence(attacker, defender);
            case "KiKi" -> KiKi(attacker, defender);
            case "KiDefence" -> KiDefence(attacker, defender);
            case "StrikeCharge" -> StrikeCharge(attacker, defender);
            case "KiCharge" -> KiCharge(attacker, defender);
        }
        sendMessage(attacker.currentStats());
        sendMessage(defender.currentStats());
        if (attacker.getHealth() <= 0 && defender.getHealth() <= 0)
            sendMessage("Tai!");
        else if (attacker.getHealth() <= 0)
            sendMessage(defender.getName() + " Wins!");
        else if (defender.getHealth() <= 0)
            sendMessage(attacker.getName() + " Wins!");
        if (attacker.getHealth() <= 0 || defender.getHealth() <= 0) {
            end();
            return "Battle over";
        }
        turnType = TurnType.Attack;
        nextTurn();
        return "Turn ended";
    }

    private static void StrikeStrike(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        double speedDiff = attacker.getSpeed() / (defender.getSpeed() + 0.0);
        int damage;
        System.out.println(attacker);
        System.out.println(defender);
        System.out.println(speedDiff);
        if (speedDiff > 2) {
            damage = (int) (attacker.getStrikeAttack() - defender.getDefence() * 0.2);
            //לשפר את הדרך שבא ההגנה עובדת
            System.out.println("to the face");
        } else if (speedDiff > 0.6) {
            damage = attacker.getStrikeAttack() - defender.getDefence();
            System.out.println("defence");
        } else if (speedDiff > 0.3) {
            damage = 0;
            System.out.println("dodged");
        } else {
            System.out.println("counter dodged");
            StrikeStrike(defender, attacker);
            return;
        }
        if (damage < 0) {
            System.out.println("counter defence");
            StrikeStrike(defender, attacker);
            return;
        }
        System.out.println(damage);
        if (defender.takeDamage(damage)) {
            System.out.println("you are ded!");
        } else System.out.println("not dead");
    }

    public static void StrikeKi(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        double speedDiff = attacker.getSpeed() / (defender.getSpeed() + 0.0);
        int damage;
        System.out.println(attacker);
        System.out.println(defender);
        System.out.println(speedDiff);
        if (speedDiff > 2) {
            damage = (int) (attacker.getStrikeAttack() - defender.getDefence() * 0.2);
            System.out.println("attack punched defender");
            System.out.println(damage);
            if (damage < 0) {
                System.out.println("counter defence for the blaster");
                StrikeStrike(defender, attacker);
                return;
            }
            if (defender.takeDamage(damage)) {
                System.out.println("you are ded!");
            } else System.out.println("not dead");
            return;
        } else if (speedDiff > 0.6) {
            System.out.println("Defender blast attacker but there was defence");
            damage = defender.getKiAttack() - attacker.getDefence();
        } else {
            System.out.println("Defender blast attacker in the face");
            damage = (int) (defender.getKiAttack() - attacker.getDefence() * 0.2);
        }
        if (damage < 0) {
            System.out.println("counter defence for the strike");
            StrikeStrike(attacker, defender);
            return;
        }
        System.out.println(damage);
        if (attacker.takeDamage(damage)) {
            System.out.println("you are ded!");
        } else System.out.println("not dead");
    }

    public static void StrikeDefence(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        double speedDiff = attacker.getSpeed() / (defender.getSpeed() + 0.0);
        int damage;
        System.out.println(attacker);
        System.out.println(defender);
        System.out.println(speedDiff);
        if (speedDiff > 3) {
            System.out.println("to the face");
            damage = (int) (attacker.getStrikeAttack() - defender.getDefence() * 0.4);
        } else if (speedDiff > 0.7) {
            damage = attacker.getStrikeAttack() - defender.getDefence();
        } else if (speedDiff > 0.5) {
            System.out.println("dodge");
            damage = 0;
        } else {
            System.out.println("counter dodged");
            StrikeStrike(defender, attacker);
            return;
        }
        if (damage < 0) {
            System.out.println("counter defence");
            StrikeStrike(defender, attacker);
            return;
        }
        System.out.println(damage);
        if (defender.takeDamage(damage)) {
            System.out.println("you are ded!");
        } else System.out.println("not dead");
    }

    public static void KiKi(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        double powerDiff = attacker.getKiAttack() / (defender.getKiAttack() + 0.0);
        int damage;
        System.out.println(attacker);
        System.out.println(defender);
        System.out.println(powerDiff);

        damage = (int) (attacker.getKiAttack() - defender.getDefence() * 0.2);
        System.out.println(damage);
        if (powerDiff > 1.5) {
            System.out.println("blow in the defender face");
            if (defender.takeDamage(damage)) {
                System.out.println("you are ded!");
            } else System.out.println("not dead");
        } else if (powerDiff < 0.5) {
            System.out.println("blow in the attacker face");
            if (attacker.takeDamage(damage)) {
                System.out.println("you are ded!");
            } else System.out.println("not dead");
        } else
            System.out.println("Boom!");
    }

    public static void KiDefence(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        double speedDiff = attacker.getSpeed() / (defender.getSpeed() + 0.0);
        int damage;
        System.out.println(attacker);
        System.out.println(defender);
        System.out.println(speedDiff);
        if (speedDiff > 4) {
            System.out.println("to the face");
            damage = (int) (attacker.getKiAttack() - defender.getDefence() * 0.4);
        } else if (speedDiff > 0.5) {
            System.out.println("defence");
            damage = attacker.getKiAttack() - defender.getDefence();
        } else {
            System.out.println("dodge");
            damage = 0;
        }
        System.out.println(damage);
        if (defender.takeDamage(damage)) {
            System.out.println("you are ded!");
        } else System.out.println("not dead");
    }

    public static void StrikeCharge(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        System.out.println(attacker);
        System.out.println(defender);
        int damage = (int) (attacker.getStrikeAttack() - defender.getDefence() * 0.2);
        System.out.println(damage);
        if (defender.takeDamage(damage)) {
            System.out.println("you are ded!");
        } else System.out.println("not dead");
    }

    public static void KiCharge(@NotNull Fighter attacker, @NotNull Fighter defender) {
        attacker.randomizeStats();
        defender.randomizeStats();
        System.out.println(attacker);
        System.out.println(defender);
        int damage = (int) (attacker.getKiAttack() - defender.getDefence() * 0.2);
        System.out.println(damage);
        if (defender.takeDamage(damage)) {
            System.out.println("you are ded!");
        } else System.out.println("not dead");
    }

    private void end() {
        battles.remove(channelId);
        Android24.jda.getTextChannelById(channelId).delete().queueAfter(10, TimeUnit.SECONDS);
    }

    private void sendMessage(String message) {
        Android24.jda.getTextChannelById(channelId).sendMessage(message).queue();
    }

    private void sendMessage(MessageEmbed embed) {
        Android24.jda.getTextChannelById(channelId).sendMessageEmbeds(embed).queue();
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public HashMap<Long, Fighter> getFightersMap() {
        return fightersMap;
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
