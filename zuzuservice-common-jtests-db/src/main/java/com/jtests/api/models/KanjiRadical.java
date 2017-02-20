package com.jtests.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "kanji_radical")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KanjiRadical implements java.io.Serializable {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "kanjiCode", column = @Column(name = "kanji_code", nullable = false)),
            @AttributeOverride(name = "radicalCode", column = @Column(name = "radical_code", nullable = false))})
    private KanjiRadicalId id;

    private int occurrences;

    public KanjiRadicalId getId() {
        return id;
    }

    public void setId(KanjiRadicalId id) {
        this.id = id;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }
}
