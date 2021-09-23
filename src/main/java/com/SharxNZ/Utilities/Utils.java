package com.SharxNZ.Utilities;

import com.SharxNZ.Commands.GameCommands.PowerPoints;
import com.SharxNZ.Android24;
import com.SharxNZ.Game.Being;
import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.gif.GifControlDirectory;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Utils {


    public static String checkRace(long userID) {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement statement = con.prepareStatement(
                        "select Race from android24.users_data where userID = ?;")
        ) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultSet.getString(1);
            else
                return null;
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return null;
        }
    }

    public static boolean checkInGame(long userID) {
        try (
                Connection con = Android24.getConnection();
                PreparedStatement inGameStatement = con.prepareStatement(
                        "SELECT `Race` FROM `android24`.`users_data` WHERE `UserID` = ?;")
        ) {

            inGameStatement.setLong(1, userID);
            ResultSet resultSet = inGameStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString(1) != null;
            else
                return false;
        } catch (SQLException throwables) {
            Android24.logError(throwables);
            throwables.printStackTrace();
            return false;
        }
    }

    public static <T> boolean inUse(T instance) {
        switch (instance.getClass().getSimpleName()) {
            case "Being":
                Being being = (Being) instance;
                return being.getInUse();
            case "PowerPoints":
                PowerPoints powerPoints = (PowerPoints) instance;
                return powerPoints.getInUse();
            default:
                return false;
        }

        // In Java's next version:
        /*switch (instance){
            case Being b:
        }*/
    }

    public static <T> void setInUse(T instance) {
        switch (instance.getClass().getSimpleName()) {
            case "Being":
                Being being = (Being) instance;
                being.setInUse(false);
                break;
            case "PowerPoints":
                PowerPoints powerPoints = (PowerPoints) instance;
                powerPoints.setInUse(false);
                break;
        }
    }

    public static <T1, T2> void garbageCollector(HashMap<T1, T2> hashMap) {
        Lock lock = new ReentrantLock();
        Timer timer = new Timer();
        final int[] i = {0};
        HashMap<T1, T2> tempMap = (HashMap<T1, T2>) hashMap.clone();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (T1 key : tempMap.keySet()) {
                    lock.lock();
                    if (inUse(tempMap.get(key))) {
                        setInUse(tempMap.get(key));
                        System.out.println(" === Prolonged === ");
                    } else {
                        tempMap.remove(key);
                        System.out.println(" === deleted === ");
                    }
                    lock.unlock();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 30000, 30000);
    }
}
