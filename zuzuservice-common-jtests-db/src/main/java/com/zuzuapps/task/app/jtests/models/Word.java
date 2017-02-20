package com.zuzuapps.task.app.jtests.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "example_word")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Word implements java.io.Serializable {
    @Id
    private int id;//`	INTEGER PRIMARY KEY AUTOINCREMENT,
    private String word;//`	VARCHAR,
    @Column(name = "reading", length = 2048)
    private String reading;//`	VARCHAR,
    @Column(name = "meaning", length = 2048)
    private String meaning;//`	VARCHAR,
    @Column(name = "is_definition")
    private boolean isDefinition;//	BOOLEAN,
    @Column(name = "jlpt_level")
    private int jlptLevel;//`	INTEGER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public boolean isDefinition() {
        return isDefinition;
    }

    public void setDefinition(boolean definition) {
        isDefinition = definition;
    }

    public int getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(int jlptLevel) {
        this.jlptLevel = jlptLevel;
    }
}

