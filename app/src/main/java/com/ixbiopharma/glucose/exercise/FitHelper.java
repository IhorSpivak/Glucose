package com.ixbiopharma.glucose.exercise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class FitHelper<T extends AppCompatActivity & FitHelper.Listener> {

    private static final String TAG = "BasicHistoryApi";
    private static GoogleApiClient mClient = null;
    private T appCompatActivity;

    public FitHelper(T appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        try {
       //     buildFitnessClient(appCompatActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildFitnessClient(AppCompatActivity context) {
        mClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                new InsertAndVerifyDataTask().execute();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                            }
                        }
                )
                .enableAutoManage(
                        context,
                        0,
                        result -> Log.e(TAG, "Connection failed. " + result.toString()))
                .build();
    }

    private class InsertAndVerifyDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            DataSet dataSet = insertFitnessData(appCompatActivity);

            com.google.android.gms.common.api.Status insertStatus =
                    Fitness.HistoryApi.insertData(mClient, dataSet)
                            .await(1, TimeUnit.MINUTES);

            if (!insertStatus.isSuccess()) return null;


            DataReadRequest readRequest = queryFitnessData();

            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest)
                            .await(1, TimeUnit.MINUTES);

            printData(dataReadResult);

            return null;
        }
    }

    @SuppressLint("WrongConstant")
    private DataSet insertFitnessData(Context context) {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(context)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setStreamName(TAG + " - step count")
                .setType(DataSource.TYPE_RAW)
                .build();

        // Create a data set
        int stepCountDelta = 950;
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(dataPoint);

        return dataSet;
    }

    @SuppressLint("WrongConstant")
    private DataReadRequest queryFitnessData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        return new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    private void printData(DataReadResult dataReadResult) {
        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
    }

    private void dumpDataSet(DataSet dataSet) {
        DateFormat dateFormat = getTimeInstance();
        String s = "";
        int mins = 0;
        try {
            for (DataPoint dp : dataSet.getDataPoints()) {
                s = s + "Data point:";
                mins = (int) ((dp.getEndTime(TimeUnit.MILLISECONDS) - dp.getStartTime(TimeUnit.MILLISECONDS)) / 60000);
                s = s + "\nType: " + dp.getDataType().getName();
                s = s + "\nStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS));
                s = s + "\nEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS));
                s = s + "\naaaaa: " + mins;

                for (Field field : dp.getDataType().getFields()) {
                    s = s + "\nField: " + field.getName() +
                            " Value: " + dp.getValue(field);
                }

                Log.e("fit", s);

                appCompatActivity.setInitValue(mins);

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Listener {
        void setInitValue(float value);
    }
}
