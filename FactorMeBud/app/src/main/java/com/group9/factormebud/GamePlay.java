package com.group9.factormebud;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GamePlay extends AppCompatActivity {

    private MainMap mMainMapView;
    private static String ICICLE_KEY = "tetris-blast-view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
		int lcurlvlid = sharedPrefs.getInt(getString(R.string.curLvlId), 1);
        boolean lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme), false);
        if (!lThemeChg){
            getWindow().setBackgroundDrawableResource(R.drawable.aqua_bg);//Draw background
        }else{
            getWindow().setBackgroundDrawableResource(R.drawable.orange_bg);//Draw background
        }

        mMainMapView = (MainMap) findViewById(R.id.gameplay);
        mMainMapView.initNewGame();
        mMainMapView.LEVEL=lcurlvlid;
        if (lcurlvlid == 1 || lcurlvlid == 3){
            mMainMapView.mMoveDelay = 1200;
        }else{
            mMainMapView.mMoveDelay = 600;
        }
        if (lcurlvlid == 3 || lcurlvlid == 4){
            mMainMapView.ranRange = 100;
        }else{
            mMainMapView.ranRange = 60;
        }
//
//        mMainMapView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchPauseMenu();
//            }
//        });

        if (savedInstanceState == null) {
            // We were just launched -- set up a new game
            mMainMapView.setMode(MainMap.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
                mMainMapView.restoreState(map);
            } else {
                mMainMapView.setMode(MainMap.PAUSE);
            }
        }
    }

    private void launchPauseMenu(){

    }

    @Override
    protected void onPause() {
        super.onPause();
        //logic to update the score for the playing level.
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if (playMusic){
            MusicManager.pause();
        }
        // Pause the game along with the activity
        mMainMapView.setMode(MainMap.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onPause();

        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);
        ContentValues lvlData = new ContentValues();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor ed;
        int lcurlvlHScr = sharedPrefs.getInt(getString(R.string.curLvlHScr), 0);
        int lcurlvlid = sharedPrefs.getInt(getString(R.string.curLvlId), 1);
        if (mMainMapView.score > lcurlvlHScr){
            ed = sharedPrefs.edit();
            ed.putInt(getString(R.string.curLvlHScr), mMainMapView.score);
            ed.commit();
            lvlData.put("HighScore", mMainMapView.score);

            //update the current highscore in the Levels file

            if (lcurlvlid == 1 && lcurlvlHScr > 300 ){
                //Unlock the level 2
            }

            if (lcurlvlid == 2 && lcurlvlHScr > 450 ){
                //Unlock the level 3
            }

            if (lcurlvlid == 3 && lcurlvlHScr > 750 ){
                //Unlock the level 4
            }

        }

        // Pause the game along with the activity
        mMainMapView.setMode(MainMap.PAUSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(ICICLE_KEY, mMainMapView.saveState());
    }

    private void startMusic(){
        MusicManager.start(this.getApplicationContext());
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if (playMusic){
            startMusic();
        }
    }
}
