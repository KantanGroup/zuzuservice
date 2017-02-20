package com.zuzuapps.task.app.jtests.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "example_word_kanji")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KanjiWord implements java.io.Serializable {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "kanjiCode", column = @Column(name = "kanji_code", nullable = false)),
            @AttributeOverride(name = "wordId", column = @Column(name = "example_word_id", nullable = false))})
    private KanjiWordId id;

    public KanjiWordId getId() {
        return id;
    }

    public void setId(KanjiWordId id) {
        this.id = id;
    }
}
