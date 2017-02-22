package com.zuzuapps.task.app.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author tuanta17
 */
public class CommonUtils {

    public static boolean createFile(Path filePath) {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                return true;
            }
            delay(10);
        } catch (Exception ex) {
        }
        return false;
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

    public static String generateUserAgent() {
        List<String> userAgents = new ArrayList<String>();
        // Firefox
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
        userAgents.add("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10; rv:33.0) Gecko/20100101 Firefox/33.0");
        userAgents.add("Mozilla/5.0 (X11; Linux i586; rv:31.0) Gecko/20100101 Firefox/31.0");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20130401 Firefox/31.0");
        userAgents.add("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");
        // Chrome
        userAgents.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36");
        userAgents.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36");
        userAgents.add("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36");
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
        if (randomNum >= userAgents.size()) {
            randomNum--;
        }
        // Get user agent
        return userAgents.get(randomNum);
    }

    public static void sortFilesOrderByTime(File[] files) {
        try {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });
        } catch (Exception ex) {
        }
    }

    public static void deleteDirectory(File directory) {
        try {
            FileUtils.deleteDirectory(directory);
        } catch (Exception ex) {
        }
    }
}
