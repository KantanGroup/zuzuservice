package com.zuzuapps.task.app.queue.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.appstore.models.CountryMaster;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author tuanta17
 */
public class SiteMapGenerateTest {
    String[] apps = new String[]{"android_wear",
            "art_and_design",
            "auto_and_vehicles",
            "beauty",
            "books_and_reference",
            "business",
            "comics",
            "communication",
            "dating",
            "education",
            "entertainment",
            "events",
            "finance",
            "food_and_drink",
            "health_and_fitness",
            "house_and_home",
            "libraries_and_demo",
            "lifestyle",
            "maps_and_navigation",
            "medical",
            "music_and_audio",
            "news_and_magazines",
            "parenting",
            "personalization",
            "photography",
            "productivity",
            "shopping",
            "social",
            "sports",
            "tools",
            "travel_and_local",
            "video_players",
            "weather"};

    String[] games = new String[]{"game",
            "game_action",
            "game_adventure",
            "game_arcade",
            "game_board",
            "game_card",
            "game_casino",
            "game_casual",
            "game_educational",
            "game_music",
            "game_puzzle",
            "game_racing",
            "game_role_playing",
            "game_simulation",
            "game_sports",
            "game_strategy",
            "game_trivia",
            "game_word"};

    public StringBuilder addSitemapLink(String link) {
        StringBuilder sItem = new StringBuilder();
        sItem.append("<url>");
        sItem.append("<loc>").append(link).append("</loc>");
        sItem.append("<changefreq>daily</changefreq>");
        sItem.append("</url>");
        return sItem;
    }

    @Test
    public void generateSitemap() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        String host = "http://topapptrends.com";
        //http://topapptrends.com/top-mobile-app-trend-in-brazil/br
        File countryFile = Paths.get("src", "test", "resources", "countries.json").toFile();
        List<CountryMaster> locals = mapper.readValue(countryFile, new TypeReference<List<CountryMaster>>() {
        });
        StringBuilder sitemap = new StringBuilder();
        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">");
        // Top
        ///*
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                sitemap.append(addSitemapLink(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/top-app"));
            }
        }
        //*/

        // Category
        //http://topapptrends.com/app-trend-in-switzerland/googlestore/app-category
        ///*
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/app-category"));
                sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/game-category"));
            }
        }
        //*/

        // Top of category
        //http://localhost:3000/app-trend-in-south-korea/googlestore/top-app/communication-category
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                for (String category : apps) {
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/top-app/" + StringUtils.join(category.split("_"), "-") + "-category"));
                }
                for (String category : games) {
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/top-app/" + StringUtils.join(category.split("_"), "-") + "-category"));
                }
            }
        }

        // List of top
        //http://localhost:3000/app-trend-in-south-korea/googlestore/list-app/topselling-free
        //*
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                sitemap.append(addSitemapLink(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/topgrossing"));
                sitemap.append(addSitemapLink(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/topselling-free"));
                sitemap.append(addSitemapLink(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/topselling-new-free"));
                sitemap.append(addSitemapLink(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/topselling-paid"));
                sitemap.append(addSitemapLink(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/topselling-new-paid"));
            }
        }
        //*/

        // List of category
        //http://localhost:3000/app-trend-in-south-korea/googlestore/list-app/communication-category/topselling-free
        //*
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                for (String category : apps) {
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topgrossing"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-free"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-new-free"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-paid"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-new-paid"));
                }
                for (String category : games) {
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topgrossing"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-free"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-new-free"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-paid"));
                    sitemap.append(addSitemapLink(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/list-app/" + StringUtils.join(category.split("_"), "-") + "-category/topselling-new-paid"));
                }
            }
        }
        sitemap.append("</urlset>");
        //*/
        FileUtils.writeStringToFile(new File("/tmp/sitemap.xml"), sitemap.toString());
        //cs-CZ
        //en-US
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                //sitemap.append(addSitemapLink(country.getLanguageCode().toLowerCase() + "-" + country.getCountryCode().toUpperCase());
            }
        }
    }

}
