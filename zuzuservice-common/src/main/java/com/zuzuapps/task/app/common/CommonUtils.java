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
     * Get top folder
     *
     * @return Absolute path
     */
    public static String getTopFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.top.name(), DataTypeEnum.queue.name(), time).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * Get summary folder
     *
     * @return Absolute path
     */
    public static String getSummaryFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.summary.name(), DataTypeEnum.queue.name(), time).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * Get application folder
     *
     * @return Absolute path
     */
    public static String getAppFolderBy(String root, String appId, String language) {
        File folder = Paths.get(root, DataServiceEnum.information.name(), DataTypeEnum.queue.name(), appId, language).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * Get screens shoot folder
     *
     * @return Absolute path
     */
    public static String getScreenshootFolderBy(String root, String appId, String time) {
        File folder = Paths.get(root, DataServiceEnum.screenshoot.name(), DataTypeEnum.queue.name(), appId, time).toFile();
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

    public static String getDailyByTime() {
        return getTimeBy("yyyMMdd");
    }

    public static String getHourlyByTime() {
        return getTimeBy("yyyyMMddHH");
    }

    public static String getMinutelyByTime() {
        return getTimeBy("yyyyMMddHHmm");
    }
}
