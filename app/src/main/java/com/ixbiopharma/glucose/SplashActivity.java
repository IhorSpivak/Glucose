package com.ixbiopharma.glucose;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.journal.JournalActivity;
import com.ixbiopharma.glucose.login.login.LoginActivity;
import com.ixbiopharma.glucose.model.UserProfile;
import com.ixbiopharma.glucose.repository.UserRepository;
import com.ixbiopharma.glucose.utils.DataTypeUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserRepository userRepository = RepositoryComponent.provideUserRepository();
        UserProfile userProfile = userRepository.getUserProfileFromPrefs();

        if (userProfile == null) {
            DataTypeUtils.glucoseMeasureType = 0;
        } else {
            DataTypeUtils.glucoseMeasureType = userProfile.getGlucoseUnit();
        }

        boolean isLogged = userRepository.isUserLogged();
        new Handler().postDelayed(() -> {

            if (!isLogged) {
                LoginActivity.start(this, false);
            } else {
                JournalActivity.start(this);
            }

            finish();
        }, 500);
    }
}