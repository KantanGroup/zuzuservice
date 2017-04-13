package com.zuzuapps.task.app.controllers;

import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.ExecuteRuntimeException;
import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import com.zuzuapps.task.app.solr.services.AppInformationSolrService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author tuanta17
 */
@RequestMapping("/api/v1")
@Controller
public class TopDownloadController {
    final Log logger = LogFactory.getLog("ApInformationController");

    @Autowired
    AppInformationSolrService appInformatioService;

    @RequestMapping("/app/information/top")
    @ResponseBody
    public List<GroupEntry<AppInformationSolr>> getTopDownload(@RequestParam("start") long start, @RequestParam("end") long end) throws ExecuteRuntimeException {
        try {
            return appInformatioService.findTopApplication(start, end);
        } catch (Exception e) {
            throw new ExecuteRuntimeException(ExceptionCodes.DATA_READ_WRITE_EXCEPTION, e.getCause());
        }
    }
}
