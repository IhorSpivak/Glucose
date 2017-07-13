package com.ixbiopharma.glucose.model;

/**
 * JournalAdvice
 *
 * Created by ivan on 27.05.17.
 */

public class JournalAdvice {
    private String advice;
    private String description;

    public JournalAdvice(String advice, String description) {
        this.advice = advice;
        this.description = description;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
