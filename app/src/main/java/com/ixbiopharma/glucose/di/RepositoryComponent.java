package com.ixbiopharma.glucose.di;

import com.ixbiopharma.glucose.repository.ExerciseRepository;
import com.ixbiopharma.glucose.repository.ExerciseRepositoryImpl;
import com.ixbiopharma.glucose.repository.FoodRepository;
import com.ixbiopharma.glucose.repository.FoodRepositoryImpl;
import com.ixbiopharma.glucose.repository.GlucoseRepository;
import com.ixbiopharma.glucose.repository.GlucoseRepositoryImpl;
import com.ixbiopharma.glucose.repository.JournalRepository;
import com.ixbiopharma.glucose.repository.JournalRepositoryImpl;
import com.ixbiopharma.glucose.repository.NewsRepository;
import com.ixbiopharma.glucose.repository.NewsRepositoryImpl;
import com.ixbiopharma.glucose.repository.SyncAdapter;
import com.ixbiopharma.glucose.repository.UserRepository;
import com.ixbiopharma.glucose.repository.UserRepositoryImpl;
import com.ixbiopharma.glucose.repository.WeightRepository;
import com.ixbiopharma.glucose.repository.WeightRepositoryImpl;

/**
 * RepositoryComponent
 * <p>
 * Created by ivan on 14.05.17.
 */

public class RepositoryComponent {

    private static NewsRepository newsRepository;
    private static UserRepository userRepository;
    private static JournalRepository journalRepository;
    private static ExerciseRepository exerciseRepository;
    private static FoodRepository foodRepository;
    private static WeightRepository weightRepository;
    private static GlucoseRepository glucoseRepository;

    public static GlucoseRepository provideGlucoseRepository(){
        if (glucoseRepository == null){
            glucoseRepository = new GlucoseRepositoryImpl(
                    provideSyncAdapter(),
                    ApplicationComponent.provideRealmHelper());
        }

        return glucoseRepository;
    }

    public static WeightRepository provideFWeightRepository(){
        if (weightRepository == null){
            weightRepository = new WeightRepositoryImpl(
                    provideSyncAdapter(),
                    ApplicationComponent.provideRealmHelper());
        }

        return weightRepository;
    }

    public static FoodRepository provideFoodRepository(){
        if (foodRepository == null){
            foodRepository = new FoodRepositoryImpl(
                    ApplicationComponent.provideRealmHelper(),
                    provideSyncAdapter());
        }

        return foodRepository;
    }

    private static SyncAdapter provideSyncAdapter() {
        return new SyncAdapter(ApplicationComponent.provideRealmHelper(),
                ApplicationComponent.provideApi(),
                ApplicationComponent.providePrefs());
    }

    public static ExerciseRepository provideExerciseRepository(){
        if (exerciseRepository == null){
            exerciseRepository = new ExerciseRepositoryImpl(
                    ApplicationComponent.provideRealmHelper(),
                    ApplicationComponent.providePrefs(),
                    provideSyncAdapter()
            );
        }
        return exerciseRepository;
    }
    public static JournalRepository provideJournalRepository() {
        if (journalRepository == null) {
            journalRepository = new JournalRepositoryImpl(
                    ApplicationComponent.provideRealmHelper(),
                    ApplicationComponent.provideApi(),
                    ApplicationComponent.providePrefs(),
                    provideSyncAdapter());
        }

        return journalRepository;
    }

    public static NewsRepository provideNewsRepository() {
        if (newsRepository == null) {
            newsRepository = new NewsRepositoryImpl(
                    ApplicationComponent.provideApi(),
                    ApplicationComponent.providePrefs());
        }
        return newsRepository;
    }

    public static UserRepository provideUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl(
                    ApplicationComponent.provideApi(),
                    ApplicationComponent.providePrefs(),
                    ApplicationComponent.provideRealmHelper());
        }

        return userRepository;
    }
}
