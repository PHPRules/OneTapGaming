package com.onetapgaming.onetapwinning;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.internal.api.LeaderboardsImpl;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.gson.Gson;
import com.onetapgaming.onetapwinning.otwendpoints.playerEndpoint.PlayerEndpoint;
import com.onetapgaming.onetapwinning.otwendpoints.playerEndpoint.model.Player;
import com.onetapgaming.onetapwinning.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onetapgaming.onetapwinning.R;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends BaseGameActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    //private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION | SystemUiHider.FLAG_FULLSCREEN;
    private static final int HIDER_FLAGS = 0;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    //final boolean ENABLE_DEBUG = true;
    //final String TAG = "OTW";

    private GoogleAccountCredential credential;
    private PlayerEndpoint service;
    private ImageButton btnWin;
    private long score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                               // mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                           // controlsView.animate()
                            //        .translationY(visible ? 0 : mControlsHeight)
                              //      .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            //controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        findViewById(R.id.btnWin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(arg0.getContext(), WinActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnTry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(arg0.getContext(), TryActivity.class);
                intent.putExtra("currentScore", score);
                startActivity(intent);
            }
        });
        credential = GoogleAccountCredential.usingAudience(this, "server:client_id:" + "117914753245-1h4gfgmu5lnr2b20bqb8l5l3urltvvkv.apps.googleusercontent.com");
        findViewById(R.id.tvRanking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),
                        getString(R.string.leaderboard_MostWins)), 1337);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };



    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {
        //PendingResult result =
            ((TextView) findViewById(R.id.tvUsername)).setText(Games.Players.getCurrentPlayer(getApiClient()).getDisplayName());
                Games.Leaderboards.loadCurrentPlayerLeaderboardScore(getApiClient(),getString(R.string.leaderboard_MostWins), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC )
                        .setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                            @Override
                            public void onResult(Leaderboards.LoadPlayerScoreResult result) {
                                // Success! Handle the query result.
                                if(result.getScore() != null) {
                                    ((TextView) findViewById(R.id.tvRanking)).setText(result.getScore().getDisplayRank());
                                    ((TextView) findViewById(R.id.tvScore)).setText(result.getScore().getDisplayScore());
                                    score = result.getScore().getRawScore();
                                }
                                else {
                                    ((TextView) findViewById(R.id.tvRanking)).setText("No Rank");
                                    ((TextView) findViewById(R.id.tvScore)).setText("0");
                                }

                            }
                        });
        new insertPlayerAsyncTask().execute();
    }

    private class insertPlayerAsyncTask extends AsyncTask<String,Void,Player>
    {
        Player response;
        @Override
        protected Player doInBackground(String... params) {
            System.out.println(Games.getCurrentAccountName(getApiClient()));
            credential.setSelectedAccountName(Games.getCurrentAccountName(getApiClient()));
            PlayerEndpoint.Builder builder = new PlayerEndpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
            builder.setApplicationName("OneTapWinning-Web");
            service = builder.build();

            try {
                System.out.println("TESTING LOGIN?");
                Player p = new Player();
                p.setKeyName("TESTINGTESTINGTESTING");
                p.setScore("1275");
                //DONT FORGET TO UNCOMMENT AND DO APPROPRIATELY.
                response = service.insertPlayer(p).execute();
                System.out.println(response.getUserID());
                //System.out.println(p.toString());
                //String p = ((Player)service.getPlayer("testingkeyyy")).getScore();
                // System.out.println(p.getScore());
            } catch (IOException e) {
                System.out.println("FAILLLLLLL");
                e.printStackTrace();
            }

            return response;
        }
    }
}
