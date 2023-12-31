package com.videostream.wallpaperdiscover.fasttoolsdownload.livewallpaper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pesonal.adsdk.AppManage;
import com.videostream.wallpaperdiscover.fasttoolsdownload.R;

public class NewHomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);
        TextView changeWallpaper = view.findViewById(R.id.change_wallpaper_button);
        AppManage.getInstance(getActivity()).loadInterstitialAd(getActivity());
        changeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManage.getInstance(getActivity()).showInterstitialAd(getActivity(), new AppManage.MyCallback() {
                    public void callbackCall() {
                        Intent intent = new Intent(getContext(), ChangeWallpaperActivity.class);
                        startActivity(intent);
                    }
                }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);

            }
        });


        TextView wallpaperSettings = view.findViewById(R.id.wallpaper_settings_button);
        wallpaperSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManage.getInstance(getActivity()).showInterstitialAd(getActivity(), new AppManage.MyCallback() {
                    public void callbackCall() {
                        Intent intent = new Intent(getContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.getRootView().setBackgroundColor(Color.argb(153, 35, 35, 35));
    }
}
