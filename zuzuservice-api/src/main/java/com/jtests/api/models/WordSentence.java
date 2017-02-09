package com.jtests.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "example_sentence_word")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordSentence implements java.io.Serializable {
    @Id
    private int id;
    @Column(name = "example_sentence_id", nullable = false)
    private int sentenceId;
    @Column(name = "example_word_id", nullable = false)
    private int wordId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }
}
