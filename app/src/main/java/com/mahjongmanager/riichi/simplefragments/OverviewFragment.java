package com.mahjongmanager.riichi.simplefragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;

public class OverviewFragment extends Fragment implements View.OnClickListener {

    TextView japaneseMahjongLink;
    TextView japaneseMahjongScoringLink;

    private String RIICHI_MAHJONG_WIKI = "https://en.wikipedia.org/wiki/Japanese_Mahjong";
    private String RIICHI_MAHJONG_SCORING_WIKI = "https://en.wikipedia.org/wiki/Japanese_Mahjong_scoring_rules";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_overview, container, false);

        registerButtons(myInflatedView);

        return myInflatedView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.japaneseMahjongLink:
                openWebPage(RIICHI_MAHJONG_WIKI);
                break;
            case R.id.japaneseMahjongScoringLink:
                openWebPage(RIICHI_MAHJONG_SCORING_WIKI);
                break;
        }
    }


    /**
     * Open a web page of a specified URL
     *
     * @param url URL to open
     */
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void registerButtons(View myInflatedView){
        japaneseMahjongLink = (TextView) myInflatedView.findViewById(R.id.japaneseMahjongLink);
        japaneseMahjongLink.setOnClickListener(this);

        japaneseMahjongScoringLink = (TextView) myInflatedView.findViewById(R.id.japaneseMahjongScoringLink);
        japaneseMahjongScoringLink.setOnClickListener(this);
    }
}
