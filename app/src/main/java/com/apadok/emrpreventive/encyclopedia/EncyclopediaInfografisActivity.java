package com.apadok.emrpreventive.encyclopedia;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EncyclopediaInfografisActivity extends AppApadokActivity {

    private final Gson gson = new Gson();
    private TextView tv_title, tv_diabetes, tv_cardiovascular, tv_stroke, tv_kebugaran;
    private ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_infografis);
        setupItemView();
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        String clinicname = getIntent().getStringExtra("clinicname");
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(clinicname);

        // Init Logo RS
        String logo = getIntent().getStringExtra("cliniclogo");
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://apadok.com/media/institusi/" + logo;
        Picasso.get().load(url).into(cliniclogo);

        tv_title = (TextView) findViewById(R.id.title_result);
        tv_diabetes = (TextView) findViewById(R.id.diabetes_title);
        tv_stroke = (TextView) findViewById(R.id.stroke_title);
        tv_cardiovascular = (TextView) findViewById(R.id.cardiovascular_title);
        tv_kebugaran = (TextView) findViewById(R.id.kebugaran_title);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_title.setTypeface(helvetica_font);
        tv_diabetes.setTypeface(helvetica_font);
        tv_stroke.setTypeface(helvetica_font);
        tv_cardiovascular.setTypeface(helvetica_font);

        int position = getIntent().getIntExtra("position", 0);
        tv_title.setText(getIntent().getStringExtra("judul_artikel"));
        String kategori = getIntent().getStringExtra("kategori_artikel");
        String image = getIntent().getStringExtra("gambar_artikel");
        String urlimage = "http://apadok.com/media/artikel/" + image;
        int kategoriint = Integer.parseInt(kategori);

        Picasso.get().load(urlimage).into(iv_image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Log.e("err",urlimage);
                iv_image.setImageResource(R.drawable.ic_doctor);
            }

        });


        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EncyclopediaInfografisActivity.this, EncyclopediaInfografisPhotoViewActivity.class);
                intent.putExtra("urlimage", urlimage);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(EncyclopediaInfografisActivity.this, view, getString(R.string.transition_test));
                startActivity(intent,options.toBundle());
            }
        });

        if (kategoriint == 1) {
            tv_diabetes.setVisibility(View.GONE);
            tv_cardiovascular.setVisibility(View.GONE);
            tv_kebugaran.setVisibility(View.GONE);
        }

        if (kategoriint == 2) {
            tv_stroke.setVisibility(View.GONE);
            tv_cardiovascular.setVisibility(View.GONE);
            tv_kebugaran.setVisibility(View.GONE);
        }

        if (kategoriint == 3) {
            tv_stroke.setVisibility(View.GONE);
            tv_diabetes.setVisibility(View.GONE);
            tv_kebugaran.setVisibility(View.GONE);
        }

        if (kategoriint == 4) {
            tv_stroke.setVisibility(View.GONE);
            tv_diabetes.setVisibility(View.GONE);
            tv_cardiovascular.setVisibility(View.GONE);
        }
    }
}
