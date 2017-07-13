package com.ixbiopharma.glucose.journal;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.DateNews;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.JournalAdvice;
import com.ixbiopharma.glucose.model.JournalHeader;
import com.ixbiopharma.glucose.model.JournalNewsWrapper;
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.model.UserProfile;
import com.ixbiopharma.glucose.model.Weight;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;
import com.ixbiopharma.glucose.repository.ExerciseRepository;
import com.ixbiopharma.glucose.repository.FoodRepository;
import com.ixbiopharma.glucose.repository.GlucoseRepository;
import com.ixbiopharma.glucose.repository.JournalRepository;
import com.ixbiopharma.glucose.repository.NewsRepository;
import com.ixbiopharma.glucose.repository.UserRepository;
import com.ixbiopharma.glucose.repository.WeightRepository;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * JournalPresenter
 * <p>
 * Created by ivan on 22.04.17.
 */

class JournalPresenter implements JournalContract.Presenter {

    @Nullable
    private JournalContract.View view;

    private NewsRepository newsRepository;
    private UserRepository userRepository;
    private JournalRepository journalRepository;
    private CompositeDisposable compositeDisposable;
    private ExerciseRepository exerciseRepository;
    private FoodRepository foodRepository;
    private GlucoseRepository glucoseRepository;
    private WeightRepository weightRepository;

    private static List<DataType> dataTypesCache = new ArrayList<>();
    private static HashMap<String, DateNews> dateNewsCache = new HashMap<>();

    @Nullable
    private static String firstNameCache;

    @Nullable
    private static DayAdviceResponse dayAdviceResponseCache;

    private int filter = 0;

    JournalPresenter(@Nullable JournalContract.View view,
                     NewsRepository newsRepository,
                     UserRepository userRepository,
                     JournalRepository journalRepository,
                     ExerciseRepository exerciseRepository,
                     FoodRepository foodRepository,
                     WeightRepository weightRepository,
                     GlucoseRepository glucoseRepository) {

        this.view = view;
        this.glucoseRepository = glucoseRepository;
        this.weightRepository = weightRepository;
        this.foodRepository = foodRepository;
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.journalRepository = journalRepository;

        compositeDisposable = new CompositeDisposable();

        getDayAdvice();
        updateDataCache();
        getDateNews();

        Realm.getDefaultInstance().addChangeListener(realm -> updateDataCache());

        sync();
    }

    private void updateDataCache() {
        dataTypesCache.clear();
        dataTypesCache.addAll(journalRepository.getJournalDataList());

        onFilterClick(filter);

        if (view != null) {
            view.showSliderData(journalRepository.getJournalSliderLastItems());
        }
    }

    @Override
    public void sync() {
        if (view != null) {
            getDateNews();

            compositeDisposable.add(
                    journalRepository.sync()
                            .doOnSubscribe(disposable -> view.showLoading())
                            .doOnComplete(() -> view.hideLoading())
                            .subscribe(() -> {

                            }, Throwable::printStackTrace)
            );
        }
    }

    @Override
    public void delete(DataType dataType) {
        if (dataType.getDataType() == Type.GLUCOSE) {
            glucoseRepository.deleteGlucose((Glucose) dataType);
        } else if (dataType.getDataType() == Type.EXERCISE) {
            exerciseRepository.deleteExercise((Exercise) dataType);
        } else if (dataType.getDataType() == Type.WIGHT) {
            weightRepository.deleteWeight((Weight) dataType);
        } else {
            foodRepository.deleteFood((Food) dataType);
        }
    }

    private void getDateNews() {
        Date currentDate = new Date();

        if (view != null) {
            compositeDisposable.add(
                    newsRepository.getDateNews(TimeUtils.getPrevYearDate(currentDate), currentDate)
                            .observeOn(Schedulers.newThread())
                            .subscribeOn(Schedulers.newThread())
                            .map(dateNewses -> {
                                for (int i = 0; i < dateNewses.size(); i++) {
                                    DateNews dateNews = dateNewses.get(i);
                                    dateNewsCache.put(dateNews.getDate(), dateNews);
                                }
                                return dateNewses;
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> view.hideLoading(), Throwable::printStackTrace));
        }
    }

    private void getDayAdvice() {
        compositeDisposable.add(
                Observable.zip(
                        journalRepository.getDayAdvice(),
                        userRepository.getUserProfile(), Pair::new)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(response -> {
                            firstNameCache = response.second.getFirstName();
                            dayAdviceResponseCache = response.first;

                            if (view != null) {
                                view.showAdvice(
                                        response.second.getFirstName(),
                                        response.first);
                            }
                        }, Throwable::printStackTrace));
    }

    private List<DataType> getDatatype(@Type int type) {
        List<DataType> dataTypes = new ArrayList<>();

        if (dataTypesCache != null) {
            for (int i = 0; i < dataTypesCache.size(); i++) {
                DataType dataType = dataTypesCache.get(i);

                if (dataType.getDataType() == type) {
                    dataTypes.add(dataType);
                }
            }
        }

        return dataTypes;
    }

    @Override
    public void onFilterClick(int position) {
        filter = position;

        new Thread(() -> {
            List<DataType> dataTypes = new ArrayList<>();

            if (position == 1) {
                dataTypes.addAll(getDatatype(Type.FOOD));
            } else if (position == 2) {
                dataTypes.addAll(getDatatype(Type.EXERCISE));
            } else if (position == 3) {
                dataTypes.addAll(getDatatype(Type.WIGHT));
            } else if (position == 4) {
                dataTypes.addAll(getDatatype(Type.GLUCOSE));
            } else {
                if (dataTypesCache != null) {
                    dataTypes.addAll(dataTypesCache);
                }
            }
            showDataList(dataTypes);

        }).start();
    }

    private void showDataList(List<DataType> dataTypes) {

        UserProfile userProfile = userRepository.getUserProfileFromPrefs();

        if (userProfile == null) {
            userProfile = new UserProfile();
        }

        List<Object> list = new ArrayList<>();

        Collections.sort(dataTypes, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        Calendar currentCalendar = null;

        List<Object> listDayData = new ArrayList<>();

        int size = dataTypes.size();

        Date old_dataTypeDate = null;

        for (int i = 0; i < size; i++) {
            DataType dataType = dataTypes.get(i);
            Date dataTypeDate = dataType.getDate();

            Calendar dataTypeCalendar = Calendar.getInstance();
            dataTypeCalendar.setTime(dataTypeDate);

            if (currentCalendar == null) {
                old_dataTypeDate = dataTypeDate;
                listDayData.add(new JournalHeader(dataTypeDate));
                currentCalendar = dataTypeCalendar;
            }

            boolean sameDay =
                    currentCalendar.get(Calendar.DAY_OF_YEAR)
                            == dataTypeCalendar.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {

                listDayData.add(dataType);

                if (i == size - 1) {
                    addItems(listDayData, userProfile, list, dataTypeDate);
                }

            } else {
                addItems(listDayData, userProfile, list, old_dataTypeDate);

                currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(dataTypeDate);
                old_dataTypeDate = dataTypeDate;
                listDayData.add(new JournalHeader(dataTypeDate));
                listDayData.add(dataType);

                if (i == size - 1) {
                    addItems(listDayData, userProfile, list, dataTypeDate);
                }
            }
        }

        if (view != null) {
            view.showDataList(list);
        }
    }

    private void addItems(List<Object> dayData,
                          @NonNull UserProfile userProfile,
                          List<Object> list,
                          Date date) {

        if (!dayData.isEmpty()) {
            int news, advice;

            if (dayData.size() >= 2) {
                advice = 2;
            } else {
                advice = 1;
            }
            news = dayData.size();

            if (dateNewsCache != null && !dateNewsCache.isEmpty()) {
                DateNews dateNews = getDateNews(date);

                if (dateNews != null) {
                    if (userProfile.getEnableNews() == 1) {
                        dayData.add(news, new JournalNewsWrapper(dateNews.getNews()));
                    }

                    if (userProfile.getEnableTips() == 1) {
                        dayData.add(
                                advice,
                                new JournalAdvice(
                                        dateNews.getAdvice_header(),
                                        dateNews.getAdvice_description()));
                    }
                }
            }

            list.addAll(dayData);
            dayData.clear();
        }
    }

    @Nullable
    private DateNews getDateNews(Date date) {
        String sdate = apiStringToDate(date);
        return dateNewsCache.get(sdate);
    }

    private String apiStringToDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return format.format(date);
    }

    @Override
    public void resume() {
        if (firstNameCache != null && view != null) {
            if (dayAdviceResponseCache != null) {
                view.showAdvice(firstNameCache, dayAdviceResponseCache);
            }
        }
    }

    @Override
    public void destroy() {
        compositeDisposable.dispose();
        view = null;
    }

    @Override
    public void updateWalk(float value) {
        Exercise exercise;

        if (userRepository.isFirstRun()) {
            exercise = new Exercise();
            exercise.value = value;
            exercise.setDate(System.currentTimeMillis());
            exercise.exercise = "Walking";
            userRepository.setRunned();
        } else {
            Date curDate = journalRepository.getLastWalkingUpdateDate();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(curDate);

            Calendar calendar = Calendar.getInstance();

            @SuppressLint("WrongConstant")
            boolean sameDay = calendar.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                exercise = exerciseRepository.getWalkingExercise();
                exercise.value = value;
                exercise.setDate(System.currentTimeMillis());

            } else {
                exercise = new Exercise();
                exercise.exercise = "Walking";
                exercise.value = value;
                exercise.setDate(System.currentTimeMillis());
            }
        }

        journalRepository.setLastWalkingUpdate(System.currentTimeMillis());

        exerciseRepository.saveExercise(exercise);
    }
}
