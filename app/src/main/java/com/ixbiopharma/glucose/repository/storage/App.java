package com.ixbiopharma.glucose.repository.storage;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

/**
 * Prefs
 * <p>
 * Created by ivan on 14.05.17.
 */

@Preferences
class App {

    @Key
    String authToken = "";

    @Key
    String userInfoJson = "";

    @Key
    String userProfleJson = "";

    @Key
    long lastWalking = System.currentTimeMillis();

    @Key
    int walking_id = 0;

    @Key
    boolean isFirstRun = true;

    @Key
    float ratingGoalGlucose = 0;

    @Key
    float ratingGoalActivity = 0;

    @Key
    float ratingGoalWeight = 0;

    @Key
    String modifyDate = "2016-04-03T17:55:00+0000";

}


