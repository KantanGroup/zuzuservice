package com.zuzuapps.task.app.jtests.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "radical")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Radical implements java.io.Serializable {
    @Id
    private int code;
    @Column(name = "variant_of_code")
    private int variantOfCode;
    @Column(name = "stroke_count")
    private int strokeCount;
    private String meaning;
    private String reading;
    private int frequency;
    private boolean important;
    @Column(name = "stroke_paths", length = 4096)
    private String strokePaths;
    @Column(name = "sequence_id")
    private int sequenceId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getVariantOfCode() {
        return variantOfCode;
    }

    public void setVariantOfCode(int variantOfCode) {
        this.variantOfCode = variantOfCode;
    }

    public int getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getStrokePaths() {
        return strokePaths;
    }

    public void setStrokePaths(String strokePaths) {
        this.strokePaths = strokePaths;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
}
