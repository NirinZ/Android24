package com.SharxNZ.Utilities;

import com.SharxNZ.Android24;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public abstract class Gifs {

    private static String statementSql = """
            SELECT count(GifID) as 'precise' FROM gifs.transform where `From` # ? and `To` ~ ?
            union
            SELECT count(GifID) as 'total' FROM gifs.transform where `To` ~ ?;
            """;
    private static String preciseSql = "SELECT Length ,Gif FROM gifs.transform where `From` # ? and `To` ~ ? ORDER BY RAND() LIMIT 1;";
    private static String otherSql = "SELECT Length ,Gif FROM gifs.transform where `To` ~ ? ORDER BY RAND() LIMIT 1;";

    private static String fixSql(String from, String to, String sql) {
        if (from == null)
            sql = sql.replaceAll("#", "is");
        else
            sql = sql.replaceAll("#", "=");
        if (to == null)
            sql = sql.replaceAll("~", "is");
        else
            sql = sql.replaceAll("~", "=");
        return sql;
    }

    public static Pair<String, Integer> getTransGif(String from, String to) {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement statement = con.prepareStatement(fixSql(from, to, statementSql))
        ) {
            final int multiplier = 3;
            Random rand = new Random();
            int precise;
            int other = 0;
            statement.setString(1, from);
            statement.setString(2, to);
            statement.setString(3, to);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            precise = resultSet.getInt(1);
            if (resultSet.next())
                other = resultSet.getInt(1) - precise;
            statement.close();
            if (from != null && from.equals(to))
                other = 0;
            if ((precise + other) == 0)
                return null;
            if (precise >= rand.nextInt(precise * multiplier + other + 1)) {
                PreparedStatement pStatement = con.prepareStatement(fixSql(from, to, preciseSql));
                pStatement.setString(1, from);
                pStatement.setString(2, to);
                ResultSet pResultSet = pStatement.executeQuery();
                if (!pResultSet.next())
                    return null;
                Pair<String, Integer> pair = new Pair<>(pResultSet.getString(2), pResultSet.getInt(1));
                pStatement.close();
                return pair;
            } else {
                PreparedStatement oStatement = con.prepareStatement(fixSql(from, to, otherSql));
                oStatement.setString(1, to);
                ResultSet oResultSet = oStatement.executeQuery();
                if (!oResultSet.next())
                    return null;
                Pair<String, Integer> pair = new Pair<>(oResultSet.getString(2), oResultSet.getInt(1));
                oStatement.close();
                return pair;
            }

        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }
}
