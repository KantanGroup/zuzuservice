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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * @author tuanta17
 */
@ConfigurationProperties("screenshoot.extract")
public class GooglePlayScreenshootProcessorProperties {

    /**
     * The timestamp format, "yyyy-MM-dd HH:mm:ss.SSS" by default.
     */
    private String format = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Server folder path for store data, "/tmp" by default.
     */
    private String folderPath = "/tmp";

    public String getFormat() {
        Assert.hasText(format, "format must not be empty nor null");
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        Assert.hasText(folderPath, "folder path must not be empty nor null");
        this.folderPath = folderPath;
    }
}
