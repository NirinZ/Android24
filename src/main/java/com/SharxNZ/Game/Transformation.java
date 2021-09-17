package com.SharxNZ.Game;

import com.SharxNZ.Android24;

import javax.naming.NameNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transformation extends Ability{

    protected boolean soloTransformation;

    private static PreparedStatement getTransformation;

    static {
        try {
            getTransformation = Android24.getConnection().prepareStatement(
                    "SELECT * FROM android24.transformations where TransformationName = ? or TransformationAbbreviated = ?;");
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
        }
    }

    public Transformation(String name) throws SQLException, NameNotFoundException {
        getTransformation.setString(1, name);
        getTransformation.setString(2, name);
        ResultSet resultSet = getTransformation.executeQuery();
        if (!resultSet.next())
            throw new NameNotFoundException("The name of the transformation does not exists");
        this.name = resultSet.getString(1);
        abbreviated = resultSet.getString(2);
        attackPowerUp = resultSet.getInt(3);
        defencePowerUp = resultSet.getInt(4);
        speedPowerUp = resultSet.getInt(5);
        kiConsumption = resultSet.getInt(6);
        soloTransformation = resultSet.getBoolean(7);
    }

    // Because I have to call the super constructor on the first line...
    protected Transformation(){}

    protected void setTransformation(String name, String abbreviated, int attackPowerUp, int defencePowerUp, int speedPowerUp, int kiConsumption, boolean soloTransformation) {
        this.name = name;
        this.abbreviated = abbreviated;
        this.attackPowerUp = attackPowerUp;
        this.defencePowerUp = defencePowerUp;
        this.speedPowerUp = speedPowerUp;
        this.kiConsumption = kiConsumption;
        this.soloTransformation = soloTransformation;
    }

    public boolean isSoloTransformation() {
        return soloTransformation;
    }

    public void setSoloTransformation(boolean soloTransformation) {
        this.soloTransformation = soloTransformation;
    }
}
