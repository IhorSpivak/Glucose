package com.ixbiopharma.glucose.progress;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.repository.ExerciseRepository;
import com.ixbiopharma.glucose.repository.GlucoseRepository;
import com.ixbiopharma.glucose.repository.JournalRepository;
import com.ixbiopharma.glucose.repository.WeightRepository;
import com.ixbiopharma.glucose.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.ixbiopharma.glucose.progress.ProgressFragment.ACTIVITY;
import static com.ixbiopharma.glucose.progress.ProgressFragment.WEIGHT;

/**
 * ProgressPresenter
 * <p>
 * Created by ivan on 02.05.17.
 */

class ProgressPresenter implements ProgressContract.Presenter {

    private ProgressContract.View view;
    private int type;
    private JournalRepository journalRepository;
    private ExerciseRepository exerciseRepository;
    private WeightRepository weightRepository;
    private GlucoseRepository glucoseRepository;

    ProgressPresenter(ProgressContract.View view,
                      int type,
                      JournalRepository journalRepository,
                      ExerciseRepository exerciseRepository,
                      WeightRepository weightRepository,
                      GlucoseRepository glucoseRepository) {

        this.glucoseRepository = glucoseRepository;
        this.weightRepository = weightRepository;
        this.exerciseRepository = exerciseRepository;
        this.view = view;
        this.type = type;
        this.journalRepository = journalRepository;
    }

    @Override
    public void getYearData(int year) {
        //todo
        getWeekData(Calendar.getInstance());
//        ArrayList<Float> dataListF = new ArrayList<>();
//        ArrayList<Float> dataListF2 = new ArrayList<>();
//        ArrayList<ArrayList<Float>> dataListFs = new ArrayList<>();
//
//        Random generator = new Random();
//        float k = generator.nextFloat() * 1f;
//
//
//        int koeffff = 1;
//
//        if (type == WEIGHT) {
//            koeffff = 6;
//        } else if (type == ACTIVITY) {
//            koeffff = 3;
//        }
//
//        dataListF.add(8f * koeffff + k);
//        dataListF.add(8.1f * koeffff + k);
//        dataListF.add(9.1f * koeffff + k);
//        dataListF.add(7.4f * koeffff + k);
//        dataListF.add(7.1f * koeffff + k);
//        dataListF.add(7.4f * koeffff + k);
//        k = generator.nextFloat() * 3f;
//        dataListF.add(8.1f * koeffff + k);
//        dataListF.add(9.1f * koeffff + k);
//        dataListF.add(8.1f * koeffff + k);
//        dataListF.add(7.1f * koeffff + k);
//        dataListF.add(8.1f * koeffff + k);
//        dataListF.add(7.4f * koeffff + k);
//
//        dataListF2.add(4f * koeffff + k);
//        dataListF2.add(4.1f * koeffff + k);
//        dataListF2.add(4.5f * koeffff + k);
//        dataListF2.add(2.1f * koeffff + k);
//        dataListF2.add(3.1f * koeffff + k);
//        dataListF2.add(3.4f * koeffff + k);
//        k = generator.nextFloat() * 2f;
//        dataListF2.add(4.5f * koeffff + k);
//        dataListF2.add(3.4f * koeffff + k);
//        dataListF2.add(4.5f * koeffff + k);
//        dataListF2.add(2.1f * koeffff + k);
//        dataListF2.add(2.1f * koeffff + k);
//        dataListF2.add(4.5f * koeffff + k);
//
//        dataListFs.add(dataListF);
//        dataListFs.add(dataListF2);
//
//        List<Float> all = new ArrayList<>();
//        all.addAll(dataListF);
//        all.addAll(dataListF2);
//
//        float max = Collections.max(all);
//        float min = Collections.min(all);
//        float av = (max + min) / 2;
//
//        view.setGrapthData(
//                StringUtils.format(max),
//                StringUtils.format(min),
//                StringUtils.format(av),
//                (int) (min - 2 * koeffff),
//                (int) (max + 2 * koeffff)
//        );
//
//
//        view.onDataSetsLoaded(dataListFs);
    }

    @Override
    public void getWeekData(Calendar calendar) {
        ArrayList<ArrayList<Float>> dataListFs = new ArrayList<>();
        ArrayList<Float> dataListF = new ArrayList<>();
        ArrayList<Float> dataListF2 = new ArrayList<>();
        List<DataType> allData = new ArrayList<>();
        SparseArray<List<Float>> dataByDays = new SparseArray<>();

        if (type == WEIGHT) {
            allData.addAll(weightRepository.getAllWeight());
        } else if (type == ACTIVITY) {
            allData.addAll(exerciseRepository.getAllActivity());
        } else {
            allData.addAll(glucoseRepository.getAllGlucose());
        }

        for (int i = 0; i < allData.size(); i++) {
            DataType data = allData.get(i);

            if (isSameWeek(calendar.getTime(), data.getDate())) {
                Calendar c = Calendar.getInstance();
                c.setTime(data.getDate());
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                if (dataByDays.get(dayOfWeek) == null){
                    dataByDays.put(dayOfWeek, new ArrayList<>());
                }

                dataByDays.get(dayOfWeek).add((float) data.getValue());
            }
        }

        for (int i = 0; i < 6; i++){
            List<Float> day = dataByDays.get(i);
            float max = 0;
            float min = 0;

            try {
                max = Collections.max(day);
                min = Collections.min(day);
            } catch (Exception e) {
            }

            dataListF.add(max);
            dataListF2.add(min);
        }


        int koeffff = 1;

        if (type == WEIGHT) {
            koeffff = 6;
        } else if (type == ACTIVITY) {
            koeffff = 3;
        }

        List<Float> all = new ArrayList<>();
        all.addAll(dataListF);
        all.addAll(dataListF2);

        float max = Collections.max(all);
        float min = Collections.min(all);
        float av = (max + min) / 2;

        view.setGrapthData(
                StringUtils.format(max),
                StringUtils.format(min),
                StringUtils.format(av),
                (int) (min - 2 * koeffff),
                (int) (max + 2 * koeffff)
        );

        view.onDataSetsLoaded(dataListFs);

    }

    private static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private boolean isSameWeek(Date first, Date second) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(first);
        int year1 = c1.get(c1.YEAR);
        int week1 = c1.get(c1.WEEK_OF_YEAR);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(second);
        int year2 = c2.get(c2.YEAR);
        int week2 = c2.get(c2.WEEK_OF_YEAR);

        return week1 == week2 && year1 == year2;
    }

    @Override
    public void getDayData(Calendar calendar) {
        ArrayList<Float> dataListF = new ArrayList<>();
        ArrayList<ArrayList<Float>> dataListFs = new ArrayList<>();

        List<DataType> dataList = new ArrayList<>();

        if (type == WEIGHT) {
            dataList.addAll(weightRepository.getAllWeight());
        } else if (type == ACTIVITY) {
            dataList.addAll(exerciseRepository.getAllActivity());
        } else {
            dataList.addAll(glucoseRepository.getAllGlucose());
        }

        for (int i = 0; i < dataList.size(); i++) {
            DataType glucose = dataList.get(i);
            @SuppressLint("WrongConstant") boolean sameDay =
                    calendar.get(Calendar.DAY_OF_YEAR) ==
                            toCalendar(glucose.getDate()).get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                dataListF.add((float) glucose.getValue());
            }
        }

        int koeffff = 1;

        if (type == WEIGHT) {
            koeffff = 6;
        } else if (type == ACTIVITY) {
            koeffff = 3;
        }

        float max = 0;
        float min = 0;

        try {
            max = Collections.max(dataListF);
            min = Collections.min(dataListF);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        float av = (max + min) / 2;

        view.setGrapthData(
                StringUtils.format(max),
                StringUtils.format(min),
                StringUtils.format(av),
                (int) (min - 2 * koeffff),
                (int) (max + 2 * koeffff)
        );

        dataListFs.add(dataListF);

        view.onDataSetsLoaded(dataListFs);

    }

}
