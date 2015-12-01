package com.group9.factormebud;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btPlay, btLevels, btSettings;
    private int lCurLvlHSc;
    private TextView sCurLvlHsc;
    private boolean lThemeChg, playMusic, gLocale;
    public RelativeLayout mainLayout;
    Locale myLocale;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if (!sharedPrefs.contains(getString(R.string.isInitialized))) {
            //This is the first time, shared pref doesn't exist
            ed = sharedPrefs.edit();
            //Add the required configuration.
            ed.putBoolean(getString(R.string.isSoundOn), true);
            ed.putBoolean(getString(R.string.isInitialized), true);
            ed.putBoolean(getString(R.string.isFirstTime), true);
            ed.putInt(getString(R.string.curLvlId), 1);
            ed.putInt(getString(R.string.curLvlHScr), 0);
            ed.putBoolean(getString(R.string.curTheme), false);
            ed.putBoolean(getString(R.string.curLang), false);
            ed.commit();
        }

        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        // Get the High score for the selected level that is going to be displayed in the main menu.
        lCurLvlHSc = sharedPrefs.getInt(getString(R.string.curLvlHScr), 100);
        sCurLvlHsc = (TextView) findViewById(R.id.sCurLvlHsc);
        sCurLvlHsc.setText(String.valueOf(lCurLvlHSc));

        playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn), false);
        if (playMusic) {
            startMusic();
        }

        gLocale = sharedPrefs.getBoolean(getString(R.string.curLang),false);

        btPlay = (Button) findViewById(R.id.btPlay);
        btLevels = (Button) findViewById(R.id.btLevels);
        btSettings = (Button) findViewById(R.id.btSettings);

        lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme), false);
        if (lThemeChg) {
            mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
            mainLayout.setBackground(getDrawable(R.drawable.app_back_orange));
            btPlay.setBackground(getDrawable(R.drawable.enter_back_orange));
            btLevels.setBackground(getDrawable(R.drawable.enter_back_orange));
            btSettings.setBackground(getDrawable(R.drawable.enter_back_orange));
        }

        btPlay.setOnClickListener(this);
        btLevels.setOnClickListener(this);
        btSettings.setOnClickListener(this);
    }


    private void startMusic() {
        MusicManager.start(this.getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btPlay:
                startActivity(new Intent(this, GamePlay.class ));
                break;
            case R.id.btLevels:
                startActivity(new Intent(this, LevelsActivity.class));
                break;
            case R.id.btSettings:
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
                boolean lCurLang  = sharedPrefs.getBoolean(getString(R.string.curLang),false);
                startActivity(new Intent(this, Settings.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor ed;
        lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme), false);
        mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        if (lThemeChg) {
            mainLayout.setBackground(getDrawable(R.drawable.app_back_orange));
            btPlay.setBackground(getDrawable(R.drawable.enter_back_orange));
            btLevels.setBackground(getDrawable(R.drawable.enter_back_orange));
            btSettings.setBackground(getDrawable(R.drawable.enter_back_orange));
        } else {
            mainLayout.setBackground(getDrawable(R.drawable.app_back));
            btPlay.setBackground(getDrawable(R.drawable.enter_back));
            btLevels.setBackground(getDrawable(R.drawable.enter_back));
            btSettings.setBackground(getDrawable(R.drawable.enter_back));
        }
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn), false);
        if (playMusic) {
            startMusic();
        }
        lCurLvlHSc = sharedPrefs.getInt(getString(R.string.curLvlHScr), 0);
        sCurLvlHsc = (TextView) findViewById(R.id.sCurLvlHsc);
        sCurLvlHsc.setText(String.valueOf(lCurLvlHSc));

        boolean lLocale = sharedPrefs.getBoolean(getString(R.string.curLang),false);
        if (gLocale != lLocale){
            this.recreate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor ed;
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn), false);
        if (playMusic) {
            MusicManager.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicManager.release();
    }
}