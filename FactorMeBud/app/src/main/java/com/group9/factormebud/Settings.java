package com.group9.factormebud;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    private Switch swTheme, swSound;
    private Button btViewTut;
    boolean lThemeChg, lPrvTheme, playMusic;
    RelativeLayout settingLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;

        swTheme = (Switch) findViewById(R.id.swTheme);
        swSound = (Switch) findViewById(R.id.swSound);
        btViewTut = (Button) findViewById(R.id.btViewTut);
        settingLayout = (RelativeLayout) findViewById(R.id.settingLayout);
        ed = sharedPrefs.edit();

        lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme),false);
        lPrvTheme = lThemeChg;
        if(lThemeChg){
            swTheme.setChecked(true);
            settingLayout.setBackground(getDrawable(R.drawable.app_back_orange));
            swTheme.setBackground(getDrawable(R.drawable.enter_back_orange));
            swSound.setBackground(getDrawable(R.drawable.enter_back_orange));
            btViewTut.setBackground(getDrawable(R.drawable.enter_back_orange));
        }else{
            swTheme.setChecked(false);
            settingLayout.setBackground(getDrawable(R.drawable.app_back));
            swTheme.setBackground(getDrawable(R.drawable.enter_back));
            swSound.setBackground(getDrawable(R.drawable.enter_back));
            btViewTut.setBackground(getDrawable(R.drawable.enter_back));
        }

        playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if(playMusic){
            swSound.setChecked(true);
        }
        else {
            swSound.setChecked(false);
        }

        swTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed = sharedPrefs.edit();
                if (isChecked) {
                    settingLayout.setBackground(getDrawable(R.drawable.app_back_orange));
                    swTheme.setBackground(getDrawable(R.drawable.enter_back_orange));
                    swSound.setBackground(getDrawable(R.drawable.enter_back_orange));
                    btViewTut.setBackground(getDrawable(R.drawable.enter_back_orange));
                    getParent();
                    ed.putBoolean(getString(R.string.curTheme), true);
                    ed.commit();
                } else {
                    settingLayout.setBackground(getDrawable(R.drawable.app_back));
                    swTheme.setBackground(getDrawable(R.drawable.enter_back));
                    swSound.setBackground(getDrawable(R.drawable.enter_back));
                    btViewTut.setBackground(getDrawable(R.drawable.enter_back));
                    ed.putBoolean(getString(R.string.curTheme), false);
                    ed.commit();
                }
            }
        });

        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed = sharedPrefs.edit();
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Sound : On", Toast.LENGTH_SHORT).show();
                    ed.putBoolean(getString(R.string.isSoundOn), true);
                    ed.commit();
                    startMusic();
                } else{
                    Toast.makeText(getApplicationContext(), "Sound : Off", Toast.LENGTH_SHORT).show();
                    ed.putBoolean(getString(R.string.isSoundOn), false);
                    ed.commit();
                    MusicManager.pause();
                }
            }
        });
//          swTheme.setOnClickListener(this);
          btViewTut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btViewTut:
                startActivity(new Intent(this, ViewTutorial.class ));
                break;
        }
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

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;
        boolean playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if (playMusic){
            MusicManager.pause();
        }
    }
}