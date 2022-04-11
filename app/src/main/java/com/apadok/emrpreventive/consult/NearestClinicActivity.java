package com.apadok.emrpreventive.consult;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.squareup.picasso.Picasso;

public class NearestClinicActivity extends AppCompatActivity {

    // Res/Layout Variables
    private TextView title_consult, tv_subtitle_consult;
    private ImageView iv_image_consult;
    private Button btn_nearclinic, btn_apadokclinic;

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
        String url = "http://178.128.25.139:8080/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        title_consult = (TextView) findViewById(R.id.title_consult);
        tv_subtitle_consult = (TextView) findViewById(R.id.tv_subtitle_consult);
        btn_nearclinic = (Button) findViewById(R.id.btn_nearclinic);
        btn_apadokclinic = (Button) findViewById(R.id.btn_apadokclinic);
        btn_nearclinic.setOnClickListener(openNearestClinic);
        title_consult.setTypeface(helvetica_font);
        tv_subtitle_consult.setTypeface(helvetica_font);
        btn_nearclinic.setTypeface(helvetica_font);
        btn_apadokclinic.setTypeface(helvetica_font);
        btn_apadokclinic.setOnClickListener(openApadokClinic);

        iv_image_consult = (ImageView) findViewById(R.id.iv_image_consult);
        iv_image_consult.setImageResource(R.drawable.ic_nearest_clinic);
        iv_image_consult.setVisibility(View.VISIBLE);
    }

    private void setupItemData() {

    }


    private View.OnClickListener openNearestClinic = v -> {
        PackageManager packageManager = NearestClinicActivity.this.getPackageManager();
        // Search for Clinics nearby
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Klinik");
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
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Klinik Permata Medika Insani");
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
}