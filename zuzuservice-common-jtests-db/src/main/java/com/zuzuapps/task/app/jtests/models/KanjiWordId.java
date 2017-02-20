package com.zuzuapps.task.app.jtests.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KanjiWordId implements java.io.Serializable {
    @Column(name = "kanji_code", nullable = false)
    private int kanjiCode;
    @Column(name = "example_word_id", nullable = false)
    private int wordId;

    public int getKanjiCode() {
        return kanjiCode;
    }

    public void setKanjiCode(int kanjiCode) {
        this.kanjiCode = kanjiCode;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KanjiWordId) {
            KanjiWordId children = (KanjiWordId) obj;
            return this.getKanjiCode() == children.getKanjiCode() && this.getWordId() == children.getWordId();
        }
        return false;
    }
}
