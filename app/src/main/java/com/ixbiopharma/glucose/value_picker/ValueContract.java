package com.ixbiopharma.glucose.value_picker;

import android.os.Parcelable;

import java.util.Date;

/**
 * ValueContract
 * <p>
 * Created by ivan on 22.04.17.
 */

interface ValueContract {

    interface View {
        void setItemTypeData(String title,
                             String hint,
                             String valuePoint,
                             int maxValue,
                             double lastValue,
                             int koef,
                             int valueKoef,
                             boolean showPlaceHolder);

        <T extends Parcelable> void onRecordSaved(T obj);

        void showLoading();

        void hideLoading();

        void onError(String message);

        void showDate(Date date);
    }

    interface Presenter {
        int getValueColor(float current);

        void save(String value, long timeMills);

        void destroy();
    }
}
