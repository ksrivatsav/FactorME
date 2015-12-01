package com.group9.factormebud;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;

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
            mMainMapView.mMoveDelay = 1000;
        }
        if (lcurlvlid == 3 || lcurlvlid == 4){
            mMainMapView.ranRange = 100;
            mMainMapView.randomArray=mMainMapView.getRandomFromArray();
        }else{
            mMainMapView.ranRange = 60;
        }

        mMainMapView.setPENALTY(lcurlvlid);
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

        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        if (playMusic){
            MusicManager.pause();
        }

        int lcurlvlHScr = sharedPrefs.getInt(getString(R.string.curLvlHScr), 0);
        int lcurlvlid = sharedPrefs.getInt(getString(R.string.curLvlId), 1);
        if (mMainMapView.score > lcurlvlHScr){
            ed = sharedPrefs.edit();
            ed.putInt(getString(R.string.curLvlHScr), mMainMapView.score);
            ed.commit();
            //lvlData.put("HighScore", mMainMapView.score);

            //update the current highscore in the Levels file
            int curHscr = mMainMapView.score;
            myDbHelper.updateScr(lcurlvlid, curHscr);

            if ((lcurlvlid == 1 && curHscr > 300) || (lcurlvlid == 2 && curHscr > 450 ) ||  (lcurlvlid == 3 && curHscr > 750 ) ){
                //Unlock the next levels
                int lnxtlvlid = lcurlvlid + 1;
                myDbHelper.unlckLvl(lnxtlvlid);

            }

        }

        myDbHelper.close();

        // Pause the game along with the activity
        mMainMapView.setMode(MainMap.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onPause();

//        ContentValues lvlData = new ContentValues();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor ed;
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        int lcurlvlHScr = sharedPrefs.getInt(getString(R.string.curLvlHScr), 0);
        int lcurlvlid = sharedPrefs.getInt(getString(R.string.curLvlId), 1);
        if (mMainMapView.score > lcurlvlHScr){
            ed = sharedPrefs.edit();
            ed.putInt(getString(R.string.curLvlHScr), mMainMapView.score);
            ed.commit();
            //lvlData.put("HighScore", mMainMapView.score);

            //update the current highscore in the Levels file
            int curHscr = mMainMapView.score;
            myDbHelper.updateScr(lcurlvlid,curHscr);

            if ((lcurlvlid == 1 && curHscr > 300) || (lcurlvlid == 2 && curHscr > 450 ) ||  (lcurlvlid == 3 && curHscr > 750 ) ){
                //Unlock the next levels
                int lnxtlvlid = lcurlvlid + 1;
                myDbHelper.unlckLvl(lnxtlvlid);

            }

        }

        myDbHelper.close();

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
        mMainMapView.setMode(MainMap.READY);
        mMainMapView.mRedrawHandler.resume();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if (playMusic){
            startMusic();
        }
    }
}
