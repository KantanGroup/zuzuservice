package com.jtests.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "example_sentence")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sentence implements java.io.Serializable {
    @Id
    private int id;//`	INTEGER PRIMARY KEY AUTOINCREMENT,
    private String sentence;//`	VARCHAR,
    @Column(name = "translation", length = 2048)
    private String translation;//`	VARCHAR,
    @Column(name = "kanji_transliteration")
    private String kanjiTransliteration;//`	VARCHAR,
    @Column(name = "kana_transliteration")
    private String kanaTransliteration;//`	VARCHAR,
    @Column(name = "kanji_code", nullable = false)
    private int kanjiCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getKanjiTransliteration() {
        return kanjiTransliteration;
    }

    public void setKanjiTransliteration(String kanjiTransliteration) {
        this.kanjiTransliteration = kanjiTransliteration;
    }

    public String getKanaTransliteration() {
        return kanaTransliteration;
    }

    public void setKanaTransliteration(String kanaTransliteration) {
        this.kanaTransliteration = kanaTransliteration;
    }

    public int getKanjiCode() {
        return kanjiCode;
    }

    public void setKanjiCode(int kanjiCode) {
        this.kanjiCode = kanjiCode;
    }
}

