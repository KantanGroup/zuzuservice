package com.zuzuapps.task.app.solr.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tuanta17
 */
@Service
public class AppInformationSolrService {
    final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    @Qualifier("appInformationTemplate")
    private SolrTemplate appInformationTemplate;

    public List<GroupEntry<AppInformationSolr>> findTopApplication(long startDownload, long endDownload) throws JsonProcessingException {
        Field field = new SimpleField("app_id");
        SimpleQuery groupQuery = new SimpleQuery(new SimpleStringCriteria("min_installs:[" + startDownload + " TO " + endDownload + "]"));
        groupQuery.addSort(new Sort(Sort.Direction.DESC, "point"));
        GroupOptions groupOptions = new GroupOptions().addGroupByField(field);
        groupQuery.setGroupOptions(groupOptions);
        GroupPage<AppInformationSolr> page = appInformationTemplate.queryForGroupPage(groupQuery, AppInformationSolr.class);
        GroupResult<AppInformationSolr> fieldGroup = page.getGroupResult(field);
        return fieldGroup.getGroupEntries().getContent();
    }
}
