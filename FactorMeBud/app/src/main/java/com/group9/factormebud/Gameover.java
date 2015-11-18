package com.group9.factormebud;

/**
 * Created by srivatsav on 11/14/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Gameover extends Activity implements View.OnClickListener {
    private Button tryagain,mainmenu;
    private RelativeLayout overLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        tryagain  = (Button) findViewById(R.id.bttryagain);
        mainmenu = (Button) findViewById(R.id.btmain);
        overLayout = (RelativeLayout) findViewById(R.id.overLayout);

        applyTheme();

        tryagain.setOnClickListener(this);
        mainmenu.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bttryagain:
                Intent i = new Intent(this, GamePlay.class); //change it to your main class
                //the following 2 tags are for clearing the backStack and start fresh
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i);
                break;
            case R.id.btmain:
                Intent i1 = new Intent(this, MainActivity.class); //change it to your main class
                //the following 2 tags are for clearing the backStack and start fresh
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i1);
                break;
        }
    }

    private void applyTheme() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;

        boolean lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme), false);
        if(lThemeChg){
            overLayout.setBackground(getDrawable(R.drawable.app_back_orange));
            tryagain.setBackground(getDrawable(R.drawable.enter_back_orange));
            mainmenu.setBackground(getDrawable(R.drawable.enter_back_orange));
        }else{
            overLayout.setBackground(getDrawable(R.drawable.app_back));
            tryagain.setBackground(getDrawable(R.drawable.enter_back));
            mainmenu.setBackground(getDrawable(R.drawable.enter_back));
        }
    }

    private void startMusic(){
        MusicManager.start(this.getApplicationContext());
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if (playMusic){
            startMusic();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if (playMusic){
            MusicManager.pause();
        }
    }
}

