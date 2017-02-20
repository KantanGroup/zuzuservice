package com.zuzuapps.task.app.jtests.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KanjiRadicalId implements java.io.Serializable {
    @Column(name = "kanji_code", nullable = false)
    private int kanjiCode;
    @Column(name = "radical_code", nullable = false)
    private int radicalCode;

    public int getKanjiCode() {
        return kanjiCode;
    }

    public void setKanjiCode(int kanjiCode) {
        this.kanjiCode = kanjiCode;
    }

    public int getRadicalCode() {
        return radicalCode;
    }

    public void setRadicalCode(int radicalCode) {
        this.radicalCode = radicalCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KanjiRadicalId) {
            KanjiRadicalId children = (KanjiRadicalId) obj;
            return this.getKanjiCode() == children.getKanjiCode() && this.getRadicalCode() == children.getRadicalCode();
        }
        return false;
    }
}
