package com.videostream.wallpaperdiscover.fasttoolsdownload.SocialMediaPages;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;

import static com.videostream.wallpaperdiscover.fasttoolsdownload.utils.Utils.RootDirectoryRoposo;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.videostream.wallpaperdiscover.fasttoolsdownload.R;
import com.videostream.wallpaperdiscover.fasttoolsdownload.activity.StatusSaverOfAllAppActivity;
import com.videostream.wallpaperdiscover.fasttoolsdownload.api.CommonClassForAPI;
import com.videostream.wallpaperdiscover.fasttoolsdownload.databinding.ActivityChingariBinding;
import com.videostream.wallpaperdiscover.fasttoolsdownload.utils.AppLangSessionManager;
import com.videostream.wallpaperdiscover.fasttoolsdownload.utils.SharePrefs;
import com.videostream.wallpaperdiscover.fasttoolsdownload.utils.Utils;
import com.pesonal.adsdk.AppManage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class ReposeActivity extends AppCompatActivity {
    private ActivityChingariBinding binding;
    ReposeActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chingari);
        activity = this;
        AppManage.getInstance(ReposeActivity.this).loadInterstitialAd(this);
        AppManage.getInstance(ReposeActivity.this).showNativeBanner((ViewGroup) findViewById(R.id.native_ads), ADMOB_N[0], "");
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

        binding.imAppIcon.setImageDrawable(getResources().getDrawable(R.drawable.repose_icon));
        binding.tvAppName.setText(getResources().getString(R.string.roposo_app_name));
        binding.appName.setText(getResources().getString(R.string.roposo_app_name));


        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());


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
                AppManage.getInstance(ReposeActivity.this).showInterstitialAd(ReposeActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        Intent intent = new Intent(ReposeActivity.this, StatusSaverOfAllAppActivity.class);
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
                .load(R.drawable.r1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.r2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.r1)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.r2)
                .into(binding.layoutHowTo.imHowto4);
        binding.layoutHowTo.tvHowToHeadOne.setVisibility(View.GONE);
        binding.layoutHowTo.LLHowToOne.setVisibility(View.GONE);
        binding.layoutHowTo.tvHowToHeadTwo.setText(getResources().getString(R.string.how_to_download));

        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_roposo));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.cop_link_from_roposo));


        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOROPOSO)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOROPOSO, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }


        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                Utils.showProgressDialog(activity);
                GetRoposoData();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.imAppIcon.setOnClickListener(v -> {
            Utils.OpenApp(activity, "com.roposo.android");
        });
    }

    @Override
    public void onBackPressed() {
        AppManage.getInstance(ReposeActivity.this).showInterstitialAd(ReposeActivity.this, new AppManage.MyCallback() {
            public void callbackCall() {
                Intent intent = new Intent(ReposeActivity.this, StatusSaverOfAllAppActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
            }
        }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);
    }

    private void GetRoposoData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("roposo")) {
                Utils.showProgressDialog(activity);
                new callGetRoposoData().execute(binding.etText.getText().toString());
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("roposo")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("roposo")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("roposo")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetRoposoData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                RoposoDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return RoposoDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                VideoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                if (VideoUrl == null || VideoUrl.equals("")) {
                    VideoUrl = result.select("meta[property=\"og:video:url\"]").last().attr("content");
                }
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        startDownload(VideoUrl, RootDirectoryRoposo, activity, "roposo_" + System.currentTimeMillis() + ".mp4");
                        VideoUrl = "";
                        binding.etText.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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