package com.group9.factormebud;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class PauseGame extends AppCompatActivity implements View.OnClickListener {
    private Button btResume,btRestart, btQuit;
    private Switch swPSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_game);

        btResume  = (Button) findViewById(R.id.btResume);
        btRestart = (Button) findViewById(R.id.btRestart);
        btQuit = (Button) findViewById(R.id.btQuit);
        swPSound = (Switch) findViewById(R.id.swPSound);

        btResume.setOnClickListener(this);
        btRestart.setOnClickListener(this);
        btQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btResume:
                break;
            case R.id.btRestart:
                break;
            case R.id.btQuit:
                finish();
                System.exit(0);
                break;
        }
    }
}
