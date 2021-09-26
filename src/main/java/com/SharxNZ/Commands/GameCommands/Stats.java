package com.SharxNZ.Commands.GameCommands;

import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.SharxNZ.Game.Transformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.SharxNZ.Android24.logError;

public class Stats extends Being {


    public Stats(long userID) {
        super(userID); // לנסות לסדר את האובג'קט פול

        try (
                Connection con = Android24.getConnection();
                PreparedStatement raceStats = con.prepareStatement(
                        "SELECT * FROM android24.races WHERE RaceName = ?;")
        ) {

            raceStats.setString(1, super.getRace());
            ResultSet resultSet = raceStats.executeQuery();
            if (!resultSet.next())
                throw new Exception("Not a legal race");
            if (currentTrans == null) {
                this.health.set((super.getHealth() + resultSet.getShort(2)) * super.getLevel());
                this.ki.set((super.getKi() + resultSet.getShort(3)) * super.getLevel());
                this.strikeAttack.set((super.getStrikeAttack() + resultSet.getShort(4)) * super.getLevel());
                this.kiAttack.set((super.getKiAttack() + resultSet.getShort(5)) * super.getLevel());
                this.defence.set((super.getDefence() + resultSet.getShort(6)) * super.getLevel());
                this.speed.set((super.getSpeed() + resultSet.getShort(7)) * super.getLevel());
            } else {
                Transformation transformation = new Transformation(this.currentTrans);
                this.health.set((super.getHealth() + resultSet.getShort(2)) * super.getLevel());
                this.ki.set((super.getKi() + resultSet.getShort(3)) * super.getLevel());
                this.strikeAttack.set(((super.getStrikeAttack() + resultSet.getShort(4)) * super.getLevel()) * transformation.getAttackPowerUp());
                this.kiAttack.set(((super.getKiAttack() + resultSet.getShort(5)) * super.getLevel()) * transformation.getAttackPowerUp());
                this.defence.set(((super.getDefence() + resultSet.getShort(6)) * super.getLevel()) * transformation.getDefencePowerUp());
                this.speed.set(((super.getSpeed() + resultSet.getShort(7)) * super.getLevel()) * transformation.getSpeedPowerUp());
            }
        } catch (Exception throwables) {
            logError(throwables);
            throwables.printStackTrace();

        }
    }

}
