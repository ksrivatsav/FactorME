package com.group9.factormebud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class PauseGame extends AppCompatActivity implements View.OnClickListener {
    private Button btResume,btRestart, btQuit;
    private Switch swPSound;
    private RelativeLayout pauseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_game);
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;
        boolean playMusic;

        btResume  = (Button) findViewById(R.id.btResume);
        btRestart = (Button) findViewById(R.id.btRestart);
        btQuit = (Button) findViewById(R.id.btQuit);
        swPSound = (Switch) findViewById(R.id.swPSound);
        pauseLayout = (RelativeLayout) findViewById(R.id.pauseLayout);
        applyTheme();

        playMusic = sharedPrefs.getBoolean(getString(R.string.isSoundOn),false);
        if(playMusic){
            swPSound.setChecked(true);
        }
        else {
            swPSound.setChecked(false);
        }

        btResume.setOnClickListener(this);
        btRestart.setOnClickListener(this);

        swPSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed = sharedPrefs.edit();
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Sound : On", Toast.LENGTH_SHORT).show();
                    ed.putBoolean(getString(R.string.isSoundOn), true);
                    ed.commit();
                    startMusic();
                } else {
                    Toast.makeText(getApplicationContext(), "Sound : Off", Toast.LENGTH_SHORT).show();
                    ed.putBoolean(getString(R.string.isSoundOn), false);
                    ed.commit();
                    MusicManager.pause();
                }
            }
        });

        //Quit the application.
        btQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), MainActivity.class); //change it to your main class
                //the following 2 tags are for clearing the backStack and start fresh
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i1);
            }
        });

    }

    private void applyTheme() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;

        boolean lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme), false);
        if(lThemeChg){
            pauseLayout.setBackground(getDrawable(R.drawable.app_back_orange));
            btResume.setBackground(getDrawable(R.drawable.enter_back_orange));
            btRestart.setBackground(getDrawable(R.drawable.enter_back_orange));
            btQuit.setBackground(getDrawable(R.drawable.enter_back_orange));
            swPSound.setBackground(getDrawable(R.drawable.enter_back_orange));
        }else{
            pauseLayout.setBackground(getDrawable(R.drawable.app_back));
            btResume.setBackground(getDrawable(R.drawable.enter_back));
            btRestart.setBackground(getDrawable(R.drawable.enter_back));
            btQuit.setBackground(getDrawable(R.drawable.enter_back));
            swPSound.setBackground(getDrawable(R.drawable.enter_back));

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btResume:
                this.onBackPressed();
                break;
            case R.id.btRestart:
                Intent i = new Intent(this, GamePlay.class); //change it to your main class
                //the following 2 tags are for clearing the backStack and start fresh
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i);
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
