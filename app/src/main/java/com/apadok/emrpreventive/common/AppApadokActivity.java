package com.apadok.emrpreventive.common;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

public class AppApadokActivity extends AppCompatActivity {

    // Override Font System Setting
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        if (override.fontScale > 1.0) {
            override.fontScale = 1.0f;
            applyOverrideConfiguration(override);
        }
        super.attachBaseContext(newBase);
    }
}
