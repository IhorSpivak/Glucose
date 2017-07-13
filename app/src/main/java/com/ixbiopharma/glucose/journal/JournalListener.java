package com.ixbiopharma.glucose.journal;

import com.ixbiopharma.glucose.model.DataType;

/**
 * JournalListener
 *
 * Created by ivan on 01.06.17.
 */

interface JournalListener {

    void onJournalClick(DataType dataType);
    void onDelete(DataType dataType);
    void onEdit(DataType dataType);
}
