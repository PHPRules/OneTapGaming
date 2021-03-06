package com.onetapgaming.onetapwinning;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.onetapgaming.onetapwinning.util.AchievementUtil;
import com.onetapgaming.onetapwinning.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.onetapgaming.onetapwinning.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TryActivity extends BaseGameActivity implements View.OnClickListener{

    CustomDrawableView mCustomDrawableView;
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
    private static final int HIDER_FLAGS = 0;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private Intent backIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        backIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_try);
        //setupActionBar();

        //final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.tryFullScreenContent);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this.getParent(), contentView, HIDER_FLAGS);
       // mSystemUiHider.setup();
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
                            //    mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            //controlsView.animate()
                             //       .translationY(visible ? 0 : mControlsHeight)
                              //      .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            //controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible) {
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
        //setContentView(mCustomDrawableView);

        //MAKE THESE GLOBAL VARIABLES OR RESOURCES
        // 30000
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView)findViewById(R.id.tvTimer)).setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                if(((FrameLayout)findViewById(R.id.tryFullScreenContent)).getChildCount() > 1) {
                    ((TextView) findViewById(R.id.tvTimer)).setText("YOU LOSE");
                    //Lose Achievement
                    if(isSignedIn()) {
                        Games.Achievements.unlock(getApiClient(), getString(R.string.loseAcheivement));
                    }
                    // Need to add a WIN button

                } else {
                    ((TextView) findViewById(R.id.tvTimer)).setText("YOU WIN");
                    Bundle extras = getIntent().getExtras();
                    long score = -1;
                    if (extras != null) {
                         score = extras.getLong("currentScore");
                    }
                    if(score >= 0)
                    {
                        score++;
                        Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_MostWins), score);
                        new AchievementUtil(getApiClient(), TryActivity.this).unlockAchievements(((int) score));
                        backIntent.putExtra("newScore", score);

                    }
                }

                // Should add a go back button
            }
        }.start();
        // 28000
        new CountDownTimer(28000, 200) {

            Handler handler = new Handler(){
                public void handleMessage(android.os.Message msg) {
                    findViewById(R.id.tryFullScreenContent).invalidate();
                }
            };

            Random random = new Random();

            public void onTick(long millisUntilFinished) {
                double test = random.nextDouble();
                if(test > .84) {
                    int delay = random.nextInt(1500);
                    handler.postDelayed(new DrawShapes(findViewById(R.id.tryFullScreenContent)), Long.valueOf(delay));
                }
            }

            public void onFinish() {
               //nothing for this one.
            }
        }.start();
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
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onClick(View v) {
        System.out.println("CLICKY CLICK CLICK");
        if (((CustomDrawableView)v).numTapsLeft > 0) {
            ((CustomDrawableView)v).numTapsLeft--;
            v.invalidate();
        } else {
            // mDrawable.setAlpha(0);
        }
    }

    @Override
    public void onSignInFailed() {
        //End App
    }

    @Override
    public void onSignInSucceeded() {

    }

   /* @Override
    public void onBackPressed() {
        startActivity(backIntent);
        super.onBackPressed();
        this.finish();
    }*/
    

}