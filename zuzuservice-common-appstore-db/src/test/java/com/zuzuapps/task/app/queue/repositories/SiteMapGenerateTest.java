package com.zuzuapps.task.app.queue.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.appstore.models.CountryMaster;
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

    @Test
    public void generateSitemap() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        String host = "http://topapptrends.com";
        //http://topapptrends.com/top-mobile-app-trend-in-brazil/br
        File countryFile = Paths.get("src", "test", "resources", "countries.json").toFile();
        List<CountryMaster> locals = mapper.readValue(countryFile, new TypeReference<List<CountryMaster>>(){});
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                System.out.println(host + "/top-mobile-app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/" + country.getCountryCode());
            }
        }
        //http://topapptrends.com/app-trend-in-switzerland/googlestore/app-category
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                System.out.println(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/app-category");
                System.out.println(host + "/app-trend-in-" + StringUtils.join(country.getCountryName().toLowerCase().split(" "), "-") + "/googlestore/game-category");
            }
        }
        //cs-CZ
        //en-US
        for (CountryMaster country : locals) {
            if (country.getType() > 0) {
                //System.out.println(country.getLanguageCode().toLowerCase() + "-" + country.getCountryCode().toUpperCase());
            }
        }
    }

}
