package com.SharxNZ.Game;

import net.dv8tion.jda.api.EmbedBuilder;

public abstract class Ability {

    protected String name;
    protected String abbreviated;
    protected int attackPowerUp;
    protected int defencePowerUp;
    protected int speedPowerUp;
    protected int kiConsumption;


    public EmbedBuilder getEmbed(){
        return null;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviated() {
        return abbreviated;
    }

    public int getAttackPowerUp() {
        return attackPowerUp;
    }

    public int getDefencePowerUp() {
        return defencePowerUp;
    }

    public int getSpeedPowerUp() {
        return speedPowerUp;
    }

    public int getKiConsumption() {
        return kiConsumption;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbreviated(String abbreviated) {
        this.abbreviated = abbreviated;
    }

    public void setAttackPowerUp(int attackPowerUp) {
        this.attackPowerUp = attackPowerUp;
    }

    public void setDefencePowerUp(int defencePowerUp) {
        this.defencePowerUp = defencePowerUp;
    }

    public void setSpeedPowerUp(int speedPowerUp) {
        this.speedPowerUp = speedPowerUp;
    }

    public void setKiConsumption(int kiConsumption) {
        this.kiConsumption = kiConsumption;
    }

    @Override
    public String toString(){
        return name;
    }

}
