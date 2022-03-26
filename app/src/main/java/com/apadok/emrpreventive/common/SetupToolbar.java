package com.apadok.emrpreventive.common;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.apadok.emrpreventive.R;

public class SetupToolbar {
    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }

    private static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(ResourcesCompat.getFont(context.getApplicationContext(), R.font.helvetica_neue));
    }
}
