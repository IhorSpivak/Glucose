package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * JournalRepository
 * <p>
 * Created by ivan on 21.05.17.
 */

public interface JournalRepository {

    List<DataType> getJournalSliderLastItems();

    List<DataType> getJournalDataList();

    Observable<DayAdviceResponse> getDayAdvice();

    Date getLastWalkingUpdateDate();

    void setLastWalkingUpdate(long l);

    Completable sync();
}
