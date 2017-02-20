package com.zuzuapps.task.app.jtests.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "kanji")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Kanji implements java.io.Serializable {
    @Id
    private int code;
    @Column(name = "sequence_id")
    private int sequenceId;
    private String meaning;
    @Column(name = "on_reading")
    private String onReading;
    @Column(name = "kun_reading")
    private String kunReading;
    @Column(name = "radicals")
    private String kanjiRadicals;
    @Column(name = "stroke_count")
    private int strokeCount;
    @Column(name = "jlpt_level")
    private int jlptLevel;
    @Column(name = "grade_level")
    private int gradeLevel;
    private int frequency;
    @Column(name = "stroke_paths", length = 4096)
    private String strokePaths;
    @Column(name = "koohiiStory1", length = 2048)
    private String koohiiStory1;
    @Column(name = "koohiiStory2", length = 2048)
    private String koohiiStory2;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "kanji_radical",
            joinColumns = @JoinColumn(name = "kanji_code", referencedColumnName = "code", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "radical_code", referencedColumnName = "code", nullable = false, updatable = false))
    private List<Radical> radicals;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "example_word_kanji",
            joinColumns = @JoinColumn(name = "kanji_code", referencedColumnName = "code", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "example_word_id", referencedColumnName = "id", nullable = false, updatable = false))
    private Set<Word> words;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "kanjiCode", orphanRemoval = true)
    private Set<Sentence> sentences;

    public List<Radical> getRadicals() {
        return radicals;
    }

    public void setRadicals(List<Radical> radicals) {
        this.radicals = radicals;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }

    public Set<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(Set<Sentence> sentences) {
        this.sentences = sentences;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getOnReading() {
        return onReading;
    }

    public void setOnReading(String onReading) {
        this.onReading = onReading;
    }

    public String getKunReading() {
        return kunReading;
    }

    public void setKunReading(String kunReading) {
        this.kunReading = kunReading;
    }

    public String getKanjiRadicals() {
        return kanjiRadicals;
    }

    public void setKanjiRadicals(String kanjiRadicals) {
        this.kanjiRadicals = kanjiRadicals;
    }

    public int getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public int getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(int jlptLevel) {
        this.jlptLevel = jlptLevel;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getStrokePaths() {
        return strokePaths;
    }

    public void setStrokePaths(String strokePaths) {
        this.strokePaths = strokePaths;
    }

    public String getKoohiiStory1() {
        return koohiiStory1;
    }

    public void setKoohiiStory1(String koohiiStory1) {
        this.koohiiStory1 = koohiiStory1;
    }

    public String getKoohiiStory2() {
        return koohiiStory2;
    }

    public void setKoohiiStory2(String koohiiStory2) {
        this.koohiiStory2 = koohiiStory2;
    }
}
