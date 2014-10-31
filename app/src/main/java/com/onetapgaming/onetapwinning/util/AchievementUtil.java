package com.onetapgaming.onetapwinning.util;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.onetapgaming.onetapwinning.R;

/**
 * Created by Jason on 10/25/2014.
 */
public class AchievementUtil {

    GoogleApiClient client;
    Activity activity;

    public AchievementUtil(GoogleApiClient client, Activity activity)
    {
        this.client = client;
        this.activity = activity;
    }

    public void unlockAchievements(int score)
    {
        Games.Achievements.unlock(client, activity.getString(R.string.oneWinAcheivement));
        if(score >= 5)
        {
            Games.Achievements.unlock(client, activity.getString(R.string.fiveWinAcheivement));
        }
        if(score >= 50)
        {
            Games.Achievements.unlock(client, activity.getString(R.string.fiftyWinAcheivement));
        }
        if(score >= 100)
        {
            Games.Achievements.unlock(client, activity.getString(R.string.hundredWinAcheivement));
        }
        Bundle extras = activity.getIntent().getExtras();
        long rank = -1;
        if (extras != null) {
            rank = extras.getLong("currentRank");
        }
        if(rank == 1)
        {
            Games.Achievements.unlock(client, activity.getString(R.string.topAcheivement));
        }
    }
}
