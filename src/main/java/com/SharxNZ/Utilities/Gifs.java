package com.SharxNZ.Utilities;

import com.SharxNZ.Android24;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Gifs {


    public static String getTransGif(String from, String to){
        try{
            final float multiplier = 3;

            PreparedStatement statement = Android24.getConnection().prepareStatement("""
                            SELECT count(GifID) as 'total' FROM gifs.transform where `to` = ?
                            union
                            SELECT count(GifID) as 'precise' FROM gifs.transform where `from` = ? and `to` = ?;
                            """);
            int total;
            int precise = 0;
            statement.setString(1, to);
            statement.setString(2, from);
            statement.setString(3, to);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            total = resultSet.getInt(1);
            if(resultSet.next())
                precise = resultSet.getInt(1);
            //precise*multiplier/total;
            return null;

        }catch (SQLException throwables){
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }
}
