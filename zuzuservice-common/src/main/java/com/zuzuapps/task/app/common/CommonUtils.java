package com.zuzuapps.task.app.common;

import java.io.File;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tuanta17
 */
public class CommonUtils {
    /**
     * Get folder by service and type
     *
     * @param root      Root path
     * @param service   Service name
     * @param type      Type name
     * @param subFolder Have subfolder
     * @return Absolute path
     */
    public static String getFolderBy(String root, String country, String category, String collection, String service, String type, boolean subFolder) {
        String daily = getTimeBy("yyyMMdd");
        String hourly = getTimeBy("yyyyMMddHH");
        String minutely = getTimeBy("yyyyMMddHHmm");
        File folder;
        if (subFolder) {
            folder = Paths.get(root, country, category.toLowerCase(), collection, service, type, daily, hourly, minutely).toFile();
        } else {
            folder = Paths.get(root, country, category.toLowerCase(), collection, service, type, daily).toFile();
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    public static String getFolderBy(String root, String country, String category, String collection, boolean subFolder) {
        String daily = getTimeBy("yyyMMdd");
        String hourly = getTimeBy("yyyyMMddHH");
        String minutely = getTimeBy("yyyyMMddHHmm");
        File folder;
        if (subFolder) {
            folder = Paths.get(root, country, category.toLowerCase(), collection, daily, hourly, minutely).toFile();
        } else {
            folder = Paths.get(root, country, category.toLowerCase(), collection, daily).toFile();
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
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

    public static void main(String[] args) {
        System.out.println(CommonUtils.getFolderBy("/tmp", "vn", "GAME", "free","app", "queue", true));
    }
}
