package com.mahjongmanager.riichi.simplefragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;

public class MainMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_mainmenu, container, false);

        //Update title to fit smaller screens
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenSize = metrics.widthPixels;
        if( screenSize < 1024 ){
            TextView appTitleLabel = (TextView) myInflatedView.findViewById(R.id.appTitleLabel);
            appTitleLabel.setTextSize(appTitleLabel.getTextSize()*0.55f);
        }

        return myInflatedView;
    }
}
