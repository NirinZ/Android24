package com.SharxNZ.Battle;

import com.SharxNZ.Android24;
import com.SharxNZ.Commands.GameCommands.Stats;
import com.SharxNZ.Game.Attack;
import com.SharxNZ.Game.Being;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Fighter extends Stats {

    private Fighter target;
    private Attack attack;
    private ArrayList<String> specialAttacks;


    protected Fighter(long userID) {
        super(userID);
        try (
                Connection con = Android24.getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT AttackAbbreviated FROM android24.users_attacks WHERE UserID = ?;")
        ) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                specialAttacks.add(resultSet.getString(1));
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public Fighter getTarget() {
        return target;
    }

    public void setTarget(Fighter target) {
        this.target = target;
    }

    public Attack getAttack() {
        return attack;
    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    public ArrayList<String> getSpecialAttacks() {
        return specialAttacks;
    }

}
