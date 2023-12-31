package com.videostream.wallpaperdiscover.fasttoolsdownload.livewallpaper;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pesonal.adsdk.AppManage;
import com.videostream.wallpaperdiscover.fasttoolsdownload.R;
import com.videostream.wallpaperdiscover.fasttoolsdownload.livewallpaper.live_wallpaper.LiveWallpaperService;

import java.util.Objects;

public class ActivateFragment extends Fragment {

    private static final String TAG = ActivateFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activate, container, false);

        TextView buttonActivate = view.findViewById(R.id.activate_button);
        buttonActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManage.getInstance(getActivity()).showInterstitialAd(getActivity(), new AppManage.MyCallback() {
                    public void callbackCall() {
                        try {
                            startActivity(new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                                    .putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                            new ComponentName(Objects.requireNonNull(getContext()), LiveWallpaperService.class))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } catch (ActivityNotFoundException e) {
                            Log.d(TAG, "onClick: ",e);
                            try {
                                startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } catch (ActivityNotFoundException e2) {
                                Log.d(TAG, "onClick: ",e);
                                Toast.makeText(getContext(), R.string
                                        .toast_failed_launch_wallpaper_chooser, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.getRootView().setBackgroundColor(Color.argb(153, 35, 35, 35));
    }
}
