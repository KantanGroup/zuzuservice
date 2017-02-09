package com.jtests.api.models;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grammars")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Grammar implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "grammar")
    private String grammar;
    @Column(name = "hiragana")
    private String hiragana;
    @Column(name = "romaji")
    private String romaji;
    @Column(name = "definition")
    private String definition;
    @Column(name = "formated")
    private String formated;
    @Column(name = "jlpt")
    private String jlpt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrammar() {
        return grammar;
    }

    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    public String getHiragana() {
        return hiragana;
    }

    public void setHiragana(String hiragana) {
        this.hiragana = hiragana;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getFormated() {
        return formated;
    }

    public void setFormated(String formated) {
        this.formated = formated;
    }

    public String getJlpt() {
        return jlpt;
    }

    public void setJlpt(String jlpt) {
        this.jlpt = jlpt;
    }
}
