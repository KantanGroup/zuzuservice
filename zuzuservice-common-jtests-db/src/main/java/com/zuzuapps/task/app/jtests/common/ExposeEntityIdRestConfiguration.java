package com.zuzuapps.task.app.jtests.common;

import com.zuzuapps.task.app.jtests.models.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * @author tuanta17
 */
@Configuration
public class ExposeEntityIdRestConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Kanji.class, Sentence.class, Word.class, Radical.class, Name.class, Grammar.class);
    }
}