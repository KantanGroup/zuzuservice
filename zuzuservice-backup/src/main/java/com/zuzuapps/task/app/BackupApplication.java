package com.zuzuapps.task.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.solr.models.AppIndexSolr;
import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import com.zuzuapps.task.app.solr.models.AppTrendSolr;
import com.zuzuapps.task.app.solr.repositories.AppIndexSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppInformationSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppTrendSolrRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author tuanta17
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@Import({AppstoreCommonConfiguration.class})
public class BackupApplication {
    static final String root = "/opt/zuzuservice/";

    @Autowired
    AppInformationSolrRepository appInformationSolrRepository;

    @Autowired
    AppIndexSolrRepository appIndexSolrRepository;

    @Autowired
    AppTrendSolrRepository appTrendSolrRepository;


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackupApplication.class, args);
    }

    public void importAppInformation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            File dir = Paths.get(root, "information", "queue").toFile();
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                for (File json : files) {
                    System.out.println("File " + json.getAbsolutePath());
                    try {
                        List<AppInformationSolr> apps = mapper.readValue(json, new TypeReference<List<AppInformationSolr>>() {
                        });
                        appInformationSolrRepository.save(apps);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    FileUtils.deleteQuietly(json);
                    CommonUtils.delay(100);
                }
            } else {
                System.out.println("File not found " + dir.getAbsolutePath());
            }
            CommonUtils.delay(1000);
        }
    }

    public void importAppIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            File dir = Paths.get(root, "index", "queue").toFile();
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                for (File json : files) {
                    System.out.println("File " + json.getAbsolutePath());
                    try {
                        List<AppIndexSolr> apps = mapper.readValue(json, new TypeReference<List<AppIndexSolr>>() {
                        });
                        appIndexSolrRepository.save(apps);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    FileUtils.deleteQuietly(json);
                    CommonUtils.delay(100);
                }
            } else {
                System.out.println("File not found " + dir.getAbsolutePath());
            }
            CommonUtils.delay(1000);
        }
    }

    public void importAppTrend() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            File dir = Paths.get(root, "trend", "queue").toFile();
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                for (File json : files) {
                    System.out.println("File " + json.getAbsolutePath());
                    try {
                        List<AppTrendSolr> apps = mapper.readValue(json, new TypeReference<List<AppTrendSolr>>() {
                        });
                        appTrendSolrRepository.save(apps);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    FileUtils.deleteQuietly(json);
                    CommonUtils.delay(100);
                }
            } else {
                System.out.println("File not found " + dir.getAbsolutePath());
            }
            CommonUtils.delay(1000);
        }
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
