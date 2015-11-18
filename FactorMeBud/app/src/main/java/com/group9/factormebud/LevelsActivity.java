package com.group9.factormebud;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btLvlBeg, btLvlInt, btLvlAdv, btLvlExp;
    private boolean lvlBegLock, lvlIntLock, lvlAdvLock, lThemeChg, lvlExpLock;
    private String lBtText;
    private RelativeLayout levelLayout;
    int tBegScr, tIntScr, tAdvScr, tExpScr;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        levelLayout = (RelativeLayout) findViewById(R.id.levelLayout);
        btLvlBeg = (Button) findViewById(R.id.btLvlBeg);
        btLvlInt = (Button) findViewById(R.id.btLvlInt);
        btLvlAdv = (Button) findViewById(R.id.btLvlAdv);
        btLvlExp = (Button) findViewById(R.id.btLvlExp);

        applyTheme();

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

        ArrayList<ArrayList<Object>> data = myDbHelper.getAllRowsAsArrays();
        for (int position = 0; position < data.size(); position++){
            ArrayList<Object> dataRow = data.get(position);
            int tHighScr = (int) dataRow.get(2);
            String tIsLckd = (String) dataRow.get(3);

            switch((int)dataRow.get(0)) {
                case 1:
                    lvlBegLock = tIsLckd.equals("Y")? true:false;
//                    lvlBegLock = (String.valueOf(tIsLckd) == "Y")? true:false;
                    tBegScr = tHighScr;
                    break;

                case 2:
                    lvlIntLock = tIsLckd.equals("Y")? true:false;
                    tIntScr= tHighScr;
                    break;

                case 3:
                    lvlAdvLock = tIsLckd.equals("Y")? true:false;
                    tAdvScr = tHighScr;
                    break;

                case 4:
                    lvlExpLock = tIsLckd.equals("Y")? true:false;
                    tExpScr = tHighScr;
                    break;
            }
        }

        myDbHelper.close();

//        lvlBegLock = false;
//        lvlIntLock = true;
//        lvlAdvLock = true;
//        lvlExpLock = true;

        if (lvlBegLock) {
            lBtText = btLvlBeg.getText() + " : Locked";
            btLvlBeg.setText(lBtText);
        } else{
            lBtText = btLvlBeg.getText() + " : " + tBegScr;
            btLvlBeg.setText(lBtText);
            btLvlBeg.setOnClickListener(this);
        }

        if (lvlIntLock){
            lBtText = btLvlInt.getText() + " : Locked";
            btLvlInt.setText(lBtText);
            btLvlInt.setClickable(false);
        } else {
            lBtText = btLvlInt.getText() + " : " + tIntScr;
            btLvlInt.setText(lBtText);
            btLvlInt.setOnClickListener(this);
        }

        if (lvlAdvLock){
            lBtText = btLvlAdv.getText() + " : Locked";
            btLvlAdv.setText(lBtText);
            btLvlAdv.setClickable(false);
        } else{
            lBtText = btLvlAdv.getText() + " : " + tAdvScr;
            btLvlAdv.setText(lBtText);
            btLvlAdv.setOnClickListener(this);
        }

        if (lvlExpLock){
            lBtText = btLvlExp.getText() + " : Locked";
            btLvlExp.setText(lBtText);
            btLvlExp.setClickable(false);
        } else {
            lBtText = btLvlExp.getText() + " : " + tExpScr;
            btLvlExp.setText(lBtText);
            btLvlExp.setOnClickListener(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyTheme() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor ed;

        lThemeChg = sharedPrefs.getBoolean(getString(R.string.curTheme), false);
        if(lThemeChg){
            levelLayout.setBackground(getDrawable(R.drawable.app_back_orange));
            btLvlBeg.setBackground(getDrawable(R.drawable.enter_back_orange));
            btLvlInt.setBackground(getDrawable(R.drawable.enter_back_orange));
            btLvlAdv.setBackground(getDrawable(R.drawable.enter_back_orange));
            btLvlExp.setBackground(getDrawable(R.drawable.enter_back_orange));
        }else{
            levelLayout.setBackground(getDrawable(R.drawable.app_back));
            btLvlBeg.setBackground(getDrawable(R.drawable.enter_back));
            btLvlInt.setBackground(getDrawable(R.drawable.enter_back));
            btLvlAdv.setBackground(getDrawable(R.drawable.enter_back));
            btLvlExp.setBackground(getDrawable(R.drawable.enter_back));

        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor ed;
        ed = sharedPrefs.edit();
        switch (v.getId()) {
            case R.id.btLvlBeg:
                if (!lvlBegLock) {
                    Toast.makeText(getApplicationContext(), "Choice : Beginner", Toast.LENGTH_SHORT).show();
                    ed.putInt(getString(R.string.curLvlId), 1);
                    ed.putInt(getString(R.string.curLvlHScr),tBegScr);
                }else {
                    Toast.makeText(getApplicationContext(), "Level: Locked", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btLvlInt:
                if (!lvlIntLock){
                    Toast.makeText(getApplicationContext(), "Choice : Intermediate", Toast.LENGTH_SHORT).show();
                    ed.putInt(getString(R.string.curLvlId), 2);
                    ed.putInt(getString(R.string.curLvlHScr),tIntScr);
                } else {
                    Toast.makeText(getApplicationContext(), "Level: Locked", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btLvlAdv:
                if (!lvlAdvLock) {
                    Toast.makeText(getApplicationContext(), "Choice : Advanced", Toast.LENGTH_SHORT).show();
                    ed.putInt(getString(R.string.curLvlId), 3);
                    ed.putInt(getString(R.string.curLvlHScr),tAdvScr);
                } else {
                    Toast.makeText(getApplicationContext(), "Level: Locked", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btLvlExp:
                if (!lvlExpLock){
                    Toast.makeText(getApplicationContext(), "Choice : Expert", Toast.LENGTH_SHORT).show();
                    ed.putInt(getString(R.string.curLvlId), 4);
                    ed.putInt(getString(R.string.curLvlHScr),tExpScr);
                } else{
                    Toast.makeText(getApplicationContext(), "Level: Locked", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        ed.commit();
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
