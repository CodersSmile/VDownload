package com.videostream.wallpaperdiscover.fasttoolsdownload.SocialMediaPages;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

import static com.videostream.wallpaperdiscover.fasttoolsdownload.utils.Utils.RootDirectoryTikTok;
import static com.videostream.wallpaperdiscover.fasttoolsdownload.utils.Utils.createFileFolder;
import static com.videostream.wallpaperdiscover.fasttoolsdownload.utils.Utils.startDownload;
import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.videostream.wallpaperdiscover.fasttoolsdownload.R;
import com.videostream.wallpaperdiscover.fasttoolsdownload.activity.StatusSaverOfAllAppActivity;
import com.videostream.wallpaperdiscover.fasttoolsdownload.api.CommonClassForAPI;
import com.videostream.wallpaperdiscover.fasttoolsdownload.databinding.ActivityChingariBinding;
import com.videostream.wallpaperdiscover.fasttoolsdownload.model.TiktokModel;
import com.videostream.wallpaperdiscover.fasttoolsdownload.utils.AppLangSessionManager;
import com.videostream.wallpaperdiscover.fasttoolsdownload.utils.SharePrefs;
import com.videostream.wallpaperdiscover.fasttoolsdownload.utils.Utils;
import com.pesonal.adsdk.AppManage;

import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

public class TikTokActivity extends AppCompatActivity {
    private ActivityChingariBinding binding;
    TikTokActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chingari);
        activity = this;
        AppManage.getInstance(TikTokActivity.this).loadInterstitialAd(this);
        AppManage.getInstance(TikTokActivity.this).showNativeBanner((ViewGroup) findViewById(R.id.native_ads), ADMOB_N[0], "");
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

        binding.imAppIcon.setImageDrawable(getResources().getDrawable(R.drawable.tiktok_icon));
        binding.tvAppName.setText(getResources().getString(R.string.tiktok_app_name));
        binding.appName.setText(getResources().getString(R.string.tiktok_app_name));


    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        LinearLayout information = findViewById(R.id.information);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManage.getInstance(TikTokActivity.this).showInterstitialAd(TikTokActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        Intent intent = new Intent(TikTokActivity.this, StatusSaverOfAllAppActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
                    }
                }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                information.setVisibility(View.VISIBLE);
                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);


            }
        });

        Glide.with(activity)
                .load(R.drawable.tt1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.tt2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.tt3)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.tt4)
                .into(binding.layoutHowTo.imHowto4);


        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_tiktok));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.open_tiktok));
        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOTT)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOTT, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetTikTokData();
            }
        });

        binding.imAppIcon.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, getResources().getString(R.string.app_not_available));
            }

        });
    }

    @Override
    public void onBackPressed() {
        AppManage.getInstance(TikTokActivity.this).showInterstitialAd(TikTokActivity.this, new AppManage.MyCallback() {
            public void callbackCall() {
                Intent intent = new Intent(TikTokActivity.this, StatusSaverOfAllAppActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
            }
        }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);
    }

    private void GetTikTokData() {
        try {
            createFileFolder();
            String host = binding.etText.getText().toString();
            if (host.contains("tiktok")) {
                Utils.showProgressDialog(activity);
                callVideoDownload(binding.etText.getText().toString());


            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVideoDownload(String Url) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    commonClassForAPI.callTiktokVideo(tiktokObserver, Url);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<TiktokModel> tiktokObserver = new DisposableObserver<TiktokModel>() {
        @Override
        public void onNext(TiktokModel tiktokModel) {
            Utils.hideProgressDialog(activity);
            try {
                if (tiktokModel.getResponsecode().equals("200")) {
                    startDownload(tiktokModel.getData().getMainvideo(),
                            RootDirectoryTikTok, activity, "tiktok_" + System.currentTimeMillis() + ".mp4");
                    binding.etText.setText("");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("tiktok")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

}