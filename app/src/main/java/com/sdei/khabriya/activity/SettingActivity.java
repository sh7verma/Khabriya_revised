package com.sdei.khabriya.activity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sdei.khabriya.R;
import com.sdei.khabriya.utils.Utilities;

public class SettingActivity extends AppCompatActivity {

    TextView versionTv;
    Switch notificationSwitch;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView backImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setting_layout);
        setIds();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionTv.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!preferences.getBoolean(Utilities.ENABLE_NOTI,false)){
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
        }

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                editor.putBoolean(Utilities.ENABLE_NOTI,false).commit();
                Log.e("noti","true");
            } else {
                editor.putBoolean(Utilities.ENABLE_NOTI,true).commit();
                Log.e("noti","false");
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void setIds() {
        versionTv = (TextView) findViewById(R.id.version_tv);
        notificationSwitch = (Switch) findViewById(R.id.notification_switch);
        backImg = (ImageView) findViewById(R.id.back_img);
        preferences = getSharedPreferences(Utilities.PREFS,MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
