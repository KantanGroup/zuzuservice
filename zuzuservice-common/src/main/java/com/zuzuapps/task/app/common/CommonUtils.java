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
    public static String queueTopFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.top.name(), DataTypeEnum.queue.name(), time).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * Get top folder
     *
     * @return Absolute path
     */
    public static String logTopFolderBy(String root, String time, String country) {
        File folder = Paths.get(root, DataServiceEnum.top.name(), DataTypeEnum.log.name(), time, country).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * Get top folder
     *
     * @return Absolute path
     */
    public static String errorTopFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.top.name(), DataTypeEnum.error.name(), time).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * Get top folder
     *
     * @return Absolute path
     */
    public static String masterTopFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.top.name(), DataTypeEnum.master.name(), time).toFile();
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
    public static String queueSummaryFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.summary.name(), DataTypeEnum.queue.name(), time).toFile();
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
    public static String logSummaryFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.summary.name(), DataTypeEnum.log.name(), time).toFile();
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
    public static String masterSummaryFolderBy(String root, String time) {
        File folder = Paths.get(root, DataServiceEnum.summary.name(), DataTypeEnum.master.name(), time).toFile();
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
    public static String queueInformationFolderBy(String root, String countryCode) {
        File folder = Paths.get(root, DataServiceEnum.information.name(), DataTypeEnum.queue.name(), countryCode).toFile();
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
    public static String errorInformationFolderBy(String root, String appId) {
        File folder = Paths.get(root, DataServiceEnum.information.name(), DataTypeEnum.error.name(), appId).toFile();
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
    public static String logInformationFolderBy(String root, String appId, String language) {
        File folder = Paths.get(root, DataServiceEnum.information.name(), DataTypeEnum.log.name(), appId, language).toFile();
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
    public static String masterAppFolderBy(String root, String appId, String language) {
        File folder = Paths.get(root, DataServiceEnum.information.name(), DataTypeEnum.master.name(), appId, language).toFile();
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
    public static String queueAppFolderBy(String root, String languageCode) {
        File folder = Paths.get(root, DataServiceEnum.app.name(), DataTypeEnum.queue.name(), languageCode).toFile();
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
    public static String errorAppFolderBy(String root, String appId) {
        File folder = Paths.get(root, DataServiceEnum.app.name(), DataTypeEnum.error.name(), appId).toFile();
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
    public static String logAppFolderBy(String root, String appId, String language) {
        File folder = Paths.get(root, DataServiceEnum.app.name(), DataTypeEnum.log.name(), appId, language).toFile();
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
