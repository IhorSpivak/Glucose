package com.ixbiopharma.glucose.progress;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * ProgressContract
 *
 * Created by ivan on 02.05.17.
 */

interface ProgressContract {

    interface View{
        void onDataSetsLoaded(ArrayList<ArrayList<Float>> dataListFs);
        void setGrapthData(String a1, String a2, String a3, int i1, int i2);
    }

    interface Presenter{
        void getYearData(int year);
        void getWeekData(Calendar calendar);
        void getDayData(Calendar calendar);
    }
}
