package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;

public class HandBuilderStart extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_handbuilder_1start, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Load tile images now
        ((MainActivity)getActivity()).getImageCache();
    }
}
