package com.apadok.emrpreventive.encyclopedia;

import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.squareup.picasso.Picasso;

public class EncyclopediaDetailActivity extends AppApadokActivity {

    private TextView tv_title, tv_diabetes, tv_cardiovascular, tv_stroke, tv_kebugaran, tv_result;
    private ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_detail);
        setupItemView();
        // setupJson();
    }

//    private void setupJson() {
//        // Send JSON ke API & Parse Respons di createcall
//        // Parse JSON Respons di createcall
//        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
//        createCalls("",new VolleyCallBack() {
//
//            @Override
//            public void onSuccess() {
//                // here you have the response from the volley.
//                String hasil_diabet = returnvalue.get("hasil_diabetes").isJsonNull() ? "" : returnvalue.get("hasil_diabetes").getAsString();
//                String hasil_stroke = returnvalue.get("hasil_stroke").isJsonNull() ? "" : returnvalue.get("hasil_stroke").getAsString();
//                String hasil_koles = returnvalue.get("hasil_kolesterol").isJsonNull() ? "" : returnvalue.get("hasil_kolesterol").getAsString();
//                hasil = "Anda Memiliki\n"+hasil_diabet+" Penyakit Diabetes\n" + hasil_stroke+" Penyakit Stroke\n" + hasil_koles + " Penyakit Kardivoaskular";
//                tv_score.setText(hasil);
//                SET IMAGE
//                String image = returnvalue.get("gambar_artikel").isJsonNull() ? "" : returnvalue.get("hasil_kolesterol").getAsString();
//                if(image.isEmpty()){
//                    iv_image.setVisibility(View.GONE)
//                }
//                else{
//                    String url = "http://apadok.com/media/klinik/" + logo;
//                    Picasso.get().load(url).into(iv_image);
//                    iv_image.setVisibility(View.VISIBLE)
//                }
//
//
//                tv_informasi.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onError() {
//                tv_score.setText(hasil);
//            }
//        });
//
//        VolleyLog.DEBUG = true;
//    }

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
        tv_result = (TextView) findViewById(R.id.result);
        tv_diabetes = (TextView) findViewById(R.id.diabetes_title);
        tv_stroke = (TextView) findViewById(R.id.stroke_title);
        tv_cardiovascular = (TextView) findViewById(R.id.cardiovascular_title);
        tv_kebugaran = (TextView) findViewById(R.id.kebugaran_title);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_title.setTypeface(helvetica_font);
        tv_result.setTypeface(helvetica_font);
        tv_diabetes.setTypeface(helvetica_font);
        tv_stroke.setTypeface(helvetica_font);
        tv_cardiovascular.setTypeface(helvetica_font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tv_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        int position = getIntent().getIntExtra("position", 0);
        tv_title.setText(getIntent().getStringExtra("judul_artikel"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_result.setText(Html.fromHtml(getIntent().getStringExtra("isi_artikel"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_result.setText(Html.fromHtml(getIntent().getStringExtra("isi_artikel")));
        }
        String kategori = getIntent().getStringExtra("kategori_artikel");
        int kategoriint = Integer.parseInt(kategori);

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
