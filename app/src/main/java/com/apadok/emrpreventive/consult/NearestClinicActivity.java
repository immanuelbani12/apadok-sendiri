package com.apadok.emrpreventive.consult;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class NearestClinicActivity extends AppCompatActivity {

    // Res/Layout Variables
    private TextView tv_title, tv_subtitle_consult;
    private ImageView iv_image_nearest_clinic;
    private Button btn_nearest_clinic, btn_apadok_clinic;

    // Intent Variables
    private String ClinicName, ClinicLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_clinic);
        setupItemView();
        setupItemData();
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        ClinicName = getIntent().getStringExtra("clinicname");
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(ClinicName);
        // Init Logo RS
        ClinicLogo = getIntent().getStringExtra("cliniclogo");
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://apadok.com/media/institusi/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_subtitle_consult = (TextView) findViewById(R.id.tv_subtitle_consult);
        btn_nearest_clinic = (Button) findViewById(R.id.btn_nearest_clinic);
        btn_apadok_clinic = (Button) findViewById(R.id.btn_apadok_clinic);
        btn_nearest_clinic.setOnClickListener(openNearestClinic);
        tv_title.setTypeface(helvetica_font);
        btn_nearest_clinic.setTypeface(helvetica_font);
        btn_apadok_clinic.setTypeface(helvetica_font);
        btn_apadok_clinic.setOnClickListener(openApadokClinic);

        iv_image_nearest_clinic = (ImageView) findViewById(R.id.iv_image_nearest_clinic);
        iv_image_nearest_clinic.setImageResource(R.drawable.ic_nearest_clinic);
        iv_image_nearest_clinic.setVisibility(View.VISIBLE);

    }

    private void setupItemData() {

    }


    private final View.OnClickListener openNearestClinic = v -> {
        PackageManager packageManager = NearestClinicActivity.this.getPackageManager();
        // Search for Clinics nearby
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Hospitals%20%26%20clinics");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent);
        } else {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
            startActivity(viewIntent);
//                KToast.errorToast(StrokeResultActivity.this, getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
        }
    };

    private final View.OnClickListener openApadokClinic = v -> {
        PackageManager packageManager = NearestClinicActivity.this.getPackageManager();
        // Search for Clinics nearby
        Uri gmmIntentUri = Uri.parse("https://goo.gl/maps/K3G9TED4MCtPWct18");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent);
        } else {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
            startActivity(viewIntent);
//                KToast.errorToast(StrokeResultActivity.this, getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
        }
    };

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.option_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                DialogFragment newFragment = new ConfirmLogOut();
                newFragment.show(getSupportFragmentManager(), "");
            default:
                return false;
        }
    }

}