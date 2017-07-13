package com.ixbiopharma.glucose.journal;

import android.support.annotation.NonNull;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;

import java.util.List;

/**
 * JournalContract
 * <p>
 * Created by ivan on 22.04.17.
 */

interface JournalContract {

    interface View {
        void showSliderData(@NonNull List<DataType> dataList);

        void showDataList(@NonNull List<Object> dateNews);

        void showAdvice(@NonNull String username,
                        @NonNull DayAdviceResponse dayAdviceResponse);

        void showLoading();

        void hideLoading();
    }

    interface Presenter {
        void onFilterClick(int position);

        void resume();

        void destroy();

        void updateWalk(float value);

        void sync();

        void delete(DataType dataType);
    }
}
