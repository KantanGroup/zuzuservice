/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A commandline runner that prints a timestamp.
 */
@EnableTask
@Configuration
@EnableConfigurationProperties({ScreenshootTaskProperties.class})
@Import(GooglePlayCommonConfiguration.class)
public class ScreenshootTaskConfiguration {

    @Bean
    public TimestampTask timeStampTask() {
        return new TimestampTask();
    }

    /**
     * A commandline runner that prints a timestamp.
     */
    public class TimestampTask implements CommandLineRunner {
        private final Log logger = LogFactory.getLog(TimestampTask.class);

        @Autowired
        private ScreenshootTaskProperties config;

        @Override
        public void run(String... strings) throws Exception {
            DateFormat dateFormat = new SimpleDateFormat(config.getFormat());
            logger.info("TimestampTask running at " + dateFormat.format(new Date()));
        }
    }

}
