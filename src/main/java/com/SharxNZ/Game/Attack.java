package com.SharxNZ.Game;

import com.SharxNZ.Android24;

import javax.naming.NameNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Attack extends Ability{

    enum ATTACK_TYPE {
        Strike, Ki;
    }

    protected boolean counter;
    protected ATTACK_TYPE attackType;

    private static PreparedStatement getAttack;

    static {
        try {
            getAttack = Android24.getConnection().prepareStatement(
                    "SELECT * FROM android24.attacks where AttackName = ? or AttackAbbreviated = ?;");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public Attack(String name) throws SQLException, NameNotFoundException {
        getAttack.setString(1, name);
        getAttack.setString(2, name);
        ResultSet resultSet = getAttack.executeQuery();
        if (!resultSet.next())
            throw new NameNotFoundException("The name of the attack does not exists");
        this.name = resultSet.getString(1);
        abbreviated = resultSet.getString(2);
        attackPowerUp = resultSet.getInt(3);
        defencePowerUp = resultSet.getInt(4);
        speedPowerUp = resultSet.getInt(5);
        kiConsumption = resultSet.getInt(6);
        counter = resultSet.getBoolean(7);
        attackType = ATTACK_TYPE.valueOf(resultSet.getString(8));
    }

    // Because I have to call the super constructor on the first line...
    protected Attack(){}

    protected void setAttack(String name, String abbreviated, int attackPowerUp, int defencePowerUp, int speedPowerUp, int kiConsumption, boolean counter, ATTACK_TYPE attackType) {
        this.name = name;
        this.abbreviated = abbreviated;
        this.attackPowerUp = attackPowerUp;
        this.defencePowerUp = defencePowerUp;
        this.speedPowerUp = speedPowerUp;
        this.kiConsumption = kiConsumption;
        this.counter = counter;
        this.attackType = attackType;
    }


    public boolean isCounter() {
        return counter;
    }

    public ATTACK_TYPE getAttackType() {
        return attackType;
    }

    public void setCounter(boolean counter) {
        this.counter = counter;
    }

    public void setAttackType(ATTACK_TYPE attackType) {
        this.attackType = attackType;
    }


}
