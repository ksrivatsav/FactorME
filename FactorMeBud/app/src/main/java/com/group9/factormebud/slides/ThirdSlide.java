package com.group9.factormebud.slides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group9.factormebud.R;

/**
 * Created by NaveenJetty on 11/7/2015.
 */
public class ThirdSlide extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.intro3, container, false);
        return v;
    }
}
