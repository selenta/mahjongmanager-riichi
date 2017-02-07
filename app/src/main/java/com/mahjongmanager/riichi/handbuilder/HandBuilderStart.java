package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.utils.AppSettings;

public class HandBuilderStart extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_1start, container, false);

        int hanRecord = AppSettings.getHandBuilderHanRecord();
        int fuRecord = AppSettings.getHandBuilderFuRecord();

        String highScoreLabel = "Current Record: "+hanRecord+" han "+fuRecord+" fu";

        TextView handBuilderHighScoreLabel = (TextView) myInflatedView.findViewById(R.id.handBuilderHighScore);
        handBuilderHighScoreLabel.setText(highScoreLabel);

        return myInflatedView;
    }

    @Override
    public void onStart(){
        super.onStart();

        //Load tile images now
        ((MainActivity)getActivity()).getImageCache();
    }
}
