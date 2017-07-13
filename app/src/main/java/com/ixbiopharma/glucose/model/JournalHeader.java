package com.ixbiopharma.glucose.model;

import android.text.format.DateUtils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * JournalHeader
 *
 * Created by ivan on 27.05.17.
 */

public class JournalHeader {

    private Date date;

    public String getAgo(){
        return DateUtils.getRelativeTimeSpanString(
                date.getTime(), System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS).toString();
    }

    public JournalHeader(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
