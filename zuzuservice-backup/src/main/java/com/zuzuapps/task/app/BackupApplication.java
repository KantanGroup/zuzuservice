package com.zuzuapps.task.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.export.solr.repositories.AppIndexSolrRepository;
import com.zuzuapps.task.app.export.solr.repositories.AppInformationSolrRepository;
import com.zuzuapps.task.app.export.solr.repositories.AppTrendSolrRepository;
import com.zuzuapps.task.app.services.CommonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author tuanta17
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableSolrRepositories(basePackages = {"com.zuzuapps.task.app.export.solr.repositories"}, multicoreSupport = true)
public class BackupApplication {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog("BackupApplication");

    @Autowired
    AppInformationSolrRepository appInformationSolrRepository;

    @Autowired
    AppIndexSolrRepository appIndexSolrRepository;

    @Autowired
    AppTrendSolrRepository appTrendSolrRepository;


    @Value("${source.solr.host:\"http://demo.zuzuapps.com/solr\"}")
    private String sitePath;

    @Autowired
    private CommonService<AppIndexObject> appIndexObjectCommonService;

    @Autowired
    private CommonService<AppTrendObject> appTrendObjectCommonService;

    @Autowired
    private CommonService<AppInformationObject> appInformationObjectCommonService;

    public static void main(String[] args) {
        SpringApplication.run(BackupApplication.class, args);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }

    private String getResponse(String link) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = null;
            response = client.execute(request);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(
                            response.getEntity().getContent()));
            String line = "";
            StringBuilder data = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                data.append(line);
            }
            return data.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return "";
    }

    public void importAppInformation() throws IOException {
        logger.info("Import app information have been started");
        int size = 500;
        int page = 0;
        while (true) {
            try {
                StringBuilder url = new StringBuilder(sitePath + "/app-information-index/select?indent=on&q=*:*&rows=" + size + "&start=" + size * page + "&wt=json");
                logger.info("URL request: " + url.toString());
                AppInformationObject appObject = mapper.readValue(getResponse(url.toString()), AppInformationObject.class);
                if (appObject != null && !appObject.getResponse().getDocs().isEmpty()) {
                    appInformationSolrRepository.save(appObject.getResponse().getDocs());
                } else {
                    break;
                }
            } catch (Exception e) {
                logger.error(e.getCause(), e);
            }
            page++;
            CommonUtils.delay(10);
        }
        logger.info("Import app information have been finished");
    }

    public void importAppIndex() throws IOException {
        logger.info("Import app index have been started");
        int size = 1000;
        int page = 0;
        while (true) {
            try {
                StringBuilder url = new StringBuilder(sitePath + "/app-index/select?indent=on&q=*:*&rows=" + size + "&start=" + size * page + "&wt=json");
                logger.info("URL request: " + url.toString());
                AppIndexObject appObject = mapper.readValue(getResponse(url.toString()), AppIndexObject.class);
                if (appObject != null && !appObject.getResponse().getDocs().isEmpty()) {
                    appIndexSolrRepository.save(appObject.getResponse().getDocs());
                } else {
                    break;
                }
            } catch (Exception e) {
                logger.error(e.getCause(), e);
            }
            page++;
            CommonUtils.delay(100);
        }
        logger.info("Import app index have been finished");
    }

    public void importAppTrend() throws IOException {
        logger.info("Import app trend have been started");
        int size = 1000;
        int page = 0;
        while (true) {
            try {
                StringBuilder url = new StringBuilder(sitePath + "/app-trend-index/select?indent=on&q=*:*&rows=" + size + "&start=" + size * page + "&wt=json");
                logger.info("URL request: " + url.toString());
                AppTrendObject appObject = mapper.readValue(getResponse(url.toString()), AppTrendObject.class);
                if (appObject != null && !appObject.getResponse().getDocs().isEmpty()) {
                    appTrendSolrRepository.save(appObject.getResponse().getDocs());
                } else {
                    break;
                }
            } catch (Exception e) {
                logger.error(e.getCause(), e);
            }
            page++;
            CommonUtils.delay(100);
        }
        logger.info("Import app trend have been finished");
    }

    @Bean
    public CommandLineRunner schedulingRunner(final TaskExecutor executor) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                executor.execute(new AppIndexImport());
                executor.execute(new AppTrendImport());
                executor.execute(new AppInformationImport());
            }
        };
    }

    class AppIndexImport implements Runnable {

        @Override
        public void run() {
            try {
                importAppIndex();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class AppTrendImport implements Runnable {

        @Override
        public void run() {
            try {
                importAppTrend();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class AppInformationImport implements Runnable {

        @Override
        public void run() {
            try {
                importAppInformation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
