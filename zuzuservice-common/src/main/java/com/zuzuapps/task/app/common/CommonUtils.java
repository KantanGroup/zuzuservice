package com.zuzuapps.task.app.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tuanta17
 */
public class CommonUtils {

    public static boolean createFile(Path filePath) {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static File folderBy(String root, String... more) {
        File folder = Paths.get(root, more).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
     * Get time by format
     *
     * @param format String format
     * @return Time formatted
     */
    public static String getTimeBy(String format) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat(format);
        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(today);
    }

    /**
     * Get time by format
     *
     * @param format String format
     * @return Time formatted
     */
    public static String getTimeBy(Date time, String format) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat(format);
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(time);
    }

    public static String getDailyByTime() {
        return getTimeBy("yyyMMdd");
    }

    public static String getHourlyByTime() {
        return getTimeBy("yyyyMMddHH");
    }

    public static String getMinutelyByTime() {
        return getTimeBy("yyyyMMddHHmm");
    }

    /**
     * String to date time
     *
     * @param time Time
     */
    public static Date toDate(String time) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            return df.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * Delay in milliseconds
     *
     * @param time Milliseconds
     */
    public static void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

}
