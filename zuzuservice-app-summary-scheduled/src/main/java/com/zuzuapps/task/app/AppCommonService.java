package com.zuzuapps.task.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.appstore.models.CountryMaster;
import com.zuzuapps.task.app.common.GZipUtil;
import com.zuzuapps.task.app.services.ScreenshotObjectService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author tuanta17
 */
@Service
public class AppCommonService {
    protected static final String JSON_FILE_EXTENSION = ".json";
    protected static final String GZ_FILE_EXTENSION = ".gz";
    protected static final String ZERO_NUMBER = "0";
    protected static final String REGEX_3_UNDER_LINE = "___";
    protected static final String COUNTRY_CODE_DEFAULT = "us";
    protected static final String LANGUAGE_CODE_DEFAULT = "en";
    protected final ObjectMapper mapper = new ObjectMapper();
    final Log logger = LogFactory.getLog("AppCommonService");
    @Value("${time.get.app.information:1000}")
    protected long timeGetAppInformation;
    @Value("${time.get.app.summary:1000}")
    protected long timeGetAppSummary;
    @Value("${time.wait.runtime.local:100}")
    protected long timeWaitRuntimeLocal;
    @Value("${time.get.app.screenshot:1000}")
    protected int timeGetAppScreenshot;
    @Value("${time.update.app.information:7}")
    protected int timeUpdateAppInformation;
    @Autowired
    protected ScreenshotObjectService screenshotApplicationPlayService;

    /**
     * Time to update
     *
     * @param appTime App time
     */
    protected boolean isTimeToUpdate(Date appTime) {
        if (appTime == null) return true;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -timeUpdateAppInformation);
        return appTime.before(cal.getTime());
    }

    protected void moveFile(String source, String destination) {
        try {
            Path src = Paths.get(source);
            Path des = Paths.get(destination);
            logger.debug("Move json file " + source + " to log folder " + destination);
            Files.move(src, des.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            Path inputFile = Paths.get(des.toFile().getAbsolutePath(), src.getFileName().toString());
            Path zipFile = Paths.get(des.toFile().getAbsolutePath(), src.getFileName() + GZ_FILE_EXTENSION);
            logger.debug("Zip json file " + inputFile + " to file " + zipFile);
            new GZipUtil().gzip(inputFile.toFile().getAbsolutePath(), zipFile.toFile().getAbsolutePath());
            logger.debug("Remove json file " + inputFile);
            Files.delete(inputFile);
        } catch (Exception ex) {
            logger.info("Move json file error " + ex.getMessage());
        }
    }

    /**
     * Get all countries from json
     */
    private List<CountryMaster> getAllCountries() {
        try {
            Path file = Paths.get("countries.json");
            return mapper.readValue(file.toFile().getAbsoluteFile(), new TypeReference<List<CountryMaster>>() {
            });
        } catch (IOException e) {
            return new ArrayList<CountryMaster>();
        }
    }

    /**
     * Get countries from json
     */
    protected List<CountryMaster> getCountries() {
        List<CountryMaster> allCountries = getAllCountries();
        List<CountryMaster> countries = new ArrayList<CountryMaster>();
        for (CountryMaster country : allCountries) {
            if (country.getType() != 0) {
                countries.add(country);
            }
        }
        Collections.sort(countries, new Comparator<CountryMaster>() {
            public int compare(CountryMaster o1, CountryMaster o2) {
                if (o1.getType() == o2.getType())
                    return 0;
                return o1.getType() > o2.getType() ? -1 : 1;
            }
        });
        return countries;
    }

}
