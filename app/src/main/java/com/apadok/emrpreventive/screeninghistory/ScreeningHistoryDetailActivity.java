package com.apadok.emrpreventive.screeninghistory;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.consult.ConsultActivity;
import com.apadok.emrpreventive.consult.NearestClinicActivity;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.encyclopedia.EncyclopediaActivity;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ScreeningHistoryDetailActivity extends AppCompatActivity {

    // Gson related
//    private Gson gson = new Gson();
    // API return variables
//    private JsonObject returnvalue;
//    private String hasil = "";

    // Res/Layout Variables
    private TextView title_result, time_result, diabetes_result, stroke_result, cardiovascular_result, dangerous_result, safe_result, stroke_details;
    private Button btn_consult, btn_education;
    // Intent Variables
    private int diabetval, strokeval, cardioval;
    private String ClinicName, ClinicLogo, Role;
    private PemeriksaanEntity sch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_result);
        setupItemView();
        setupItemData();
//        setupJson();
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
        String url = "http://apadok.com/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        diabetes_result = (TextView) findViewById(R.id.diabetes_result);
        stroke_result = (TextView) findViewById(R.id.stroke_result);
        stroke_details = (TextView) findViewById(R.id.stroke_details);
        cardiovascular_result = (TextView) findViewById(R.id.cardiovascular_result);
        dangerous_result = (TextView) findViewById(R.id.dangerous_result);
        safe_result = (TextView) findViewById(R.id.safe_result);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        btn_education = (Button) findViewById(R.id.btn_education);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        title_result.setTypeface(helvetica_font);
        time_result.setTypeface(helvetica_font);
        diabetes_result.setTypeface(helvetica_font);
        stroke_result.setTypeface(helvetica_font);
        cardiovascular_result.setTypeface(helvetica_font);
        dangerous_result.setTypeface(helvetica_font);
        safe_result.setTypeface(helvetica_font);
        btn_consult.setTypeface(helvetica_font);
        btn_education.setTypeface(helvetica_font);

        int position = getIntent().getIntExtra("position", 0);
        title_result.setText("Riwayat Skrining " + position);
        time_result.setText("Mengambil Data....");
        diabetes_result.setVisibility(View.GONE);
        stroke_result.setVisibility(View.GONE);
        stroke_details.setVisibility(View.GONE);
        cardiovascular_result.setVisibility(View.GONE);
        dangerous_result.setVisibility(View.GONE);
        safe_result.setVisibility(View.GONE);
        btn_consult.setVisibility(View.GONE);
        btn_consult.setOnClickListener(RedirectToConsult);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);

        Role = getIntent().getStringExtra("role");
        if (Role != null) {
            if (Role.equals("N")) {
                btn_consult.setText("Pencarian Klinik");
                btn_consult.setOnClickListener(RedirectToNearestClinic);
            }
        }
    }

    private void setupItemData() {
        sch = getIntent().getParcelableExtra("data");
        String hasil_diabet = sch.getHasil_diabetes() == null ? "" : sch.getHasil_diabetes();
        String hasil_kardio = sch.getHasil_kolesterol() == null ? "" : sch.getHasil_kolesterol();
        String hasil_stroke = sch.getHasil_stroke() == null ? "" : sch.getHasil_stroke();
        String timestamp = sch.getUpdated_at() == null ? sch.getCreated_at() : sch.getUpdated_at();

        time_result.setText(timestamp);
        diabetes_result.setText(hasil_diabet);
        stroke_result.setText(hasil_stroke);
        cardiovascular_result.setText(hasil_kardio);
        diabetes_result.setVisibility(View.VISIBLE);
        stroke_result.setVisibility(View.VISIBLE);
        cardiovascular_result.setVisibility(View.VISIBLE);

        //Handling IF Else results in here
        // Need to Change SetTextColor 1=Merah, 2=Menengah(Kuning/Itam), 3=Hijau
        String dangtext = "";
        String safetext = "";
        if (hasil_diabet.contains("Tinggi")) {
            diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
            dangtext = "penyakit diabetes";
            diabetval = 3;
        } else if (hasil_diabet.contains("Rendah")) {
            diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
            safetext = "penyakit diabetes";
            diabetval = 1;
        } else {
            diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
            diabetval = 2;
            safetext = "penyakit diabetes";
        }
        if (hasil_stroke.contains("Tinggi")) {
            stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
            if (dangtext == "") {
                dangtext = "penyakit stroke";
            } else {
                dangtext += ", penyakit stroke";
            }
            strokeval = 3;
        } else if (hasil_stroke.contains("Rendah")) {
            stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
            strokeval = 1;
            if (safetext == "") {
                safetext = "penyakit stroke";
            } else {
                safetext += ", penyakit stroke";
            }
        } else {
            stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
            strokeval = 2;
            if (safetext == "") {
                safetext = "penyakit stroke";
            } else {
                safetext += ", penyakit stroke";
            }
        }
        if (hasil_kardio.contains("Tinggi")) {
            cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
            if (safetext == "") {
                safetext = "penyakit kardiovaskular";
            } else {
                safetext += ", penyakit kardiovaskular";
            }
            cardioval = 3;
        } else if (hasil_kardio.contains("Rendah")) {
            cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
            cardioval = 1;
            if (safetext == "") {
                safetext = "penyakit kardiovaskular";
            } else {
                safetext += ", penyakit kardiovaskular";
            }
        } else if (hasil_kardio.contains("Tidak")) {
            cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_green_font));
            cardioval = 1;
            if (safetext == "") {
                safetext = "penyakit kardiovaskular";
            } else {
                safetext += ", penyakit kardiovaskular";
            }
        } else {
            cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
            cardioval = 2;
            if (safetext == "") {
                safetext = "penyakit kardiovaskular";
            } else {
                safetext += ", penyakit kardiovaskular";
            }
        }

        // Saran
        if (diabetval <= 2 || strokeval <= 2 || cardioval <= 2) {
            safe_result.setText("Untuk " + safetext + " pada tubuh anda memiliki risiko yang tidak terlalu membahayakan. Silahkan melihat edukasi pencegahan penyakit tersebut berikut untuk mempertahankan capaian anda tersebut.");
            safe_result.setVisibility(View.VISIBLE);
            btn_education.setVisibility(View.VISIBLE);
        }

        // Add Penjelasan Kenapa Risiko Muncul
        if (strokeval >= 2) {
            String kadar_gula_tidakdiketahui = sch.getKadar_gula_tidakdiketahui() == null ? "" : sch.getKadar_gula_tidakdiketahui();
            String tekanan_darah_tidakdiketahui = sch.getTekanan_darah_tidakdiketahui() == null ? "" : sch.getTekanan_darah_tidakdiketahui();
            String kadar_kolesterol_tidakdiketahui = sch.getKadar_kolesterol_tidakdiketahui() == null ? "" : sch.getKadar_kolesterol_tidakdiketahui();
            String stroke_warning = "";
            if (Objects.equals(kadar_gula_tidakdiketahui, "1") || Objects.equals(tekanan_darah_tidakdiketahui, "1") || Objects.equals(kadar_kolesterol_tidakdiketahui, "1")){
                if (kadar_gula_tidakdiketahui.contains("1")) {
                    stroke_warning = "kadar gula darah";
                }
                if (tekanan_darah_tidakdiketahui.contains("1")) {
                    if (stroke_warning.equals("")) {
                        stroke_warning = "tekanan darah";
                    } else {
                        stroke_warning += ", tekanan darah";
                    }
                }
                if (kadar_kolesterol_tidakdiketahui.contains("1")) {
                    if (stroke_warning.equals("")) {
                        stroke_warning = "kadar kolesterol";
                    } else {
                        stroke_warning += ", kadar kolesterol";
                    }
                }
                stroke_details.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
                if (strokeval == 3){
                    stroke_details.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
                }
                stroke_details.setText(hasil_stroke + " muncul karena anda mengisi tidak diketahui pada bagian " + stroke_warning);
                stroke_details.setVisibility(View.VISIBLE);
            }
        }

        // Skip Consultation for Past Histories
        int position = getIntent().getIntExtra("position", 0);
        if (position > 1) {
            String text = "Konsultasi hanya tersedia pada riwayat skrining terbaru";
            if (Role != null) {
                if (Role.equals("N")) {
                    text = "Pencarian Klinik hanya tersedia pada riwayat skrining terbaru";
                }
            }
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(getBaseContext(),R.color.orange_dark));
            snackbar.show();
            return;
        }

        if (diabetval == 3 || strokeval == 3 || cardioval == 3) {
            dangerous_result.setText("Tubuh anda memiliki resiko tinggi untuk " + dangtext + " sehingga membutuhkan konsultasi secara offline ke dokter");
            dangerous_result.setVisibility(View.VISIBLE);
            btn_consult.setVisibility(View.VISIBLE);
        }
        if (diabetval != 3 && strokeval != 3 && cardioval != 3) {
            dangerous_result.setText("Jika anda memiliki keluhan terkait dengan " + safetext + " silahkan melakukan konsultasi secara offline ke dokter");
            dangerous_result.setVisibility(View.VISIBLE);
            btn_consult.setVisibility(View.VISIBLE);
        }
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
//            }
//
//            @Override
//            public void onError() {
//                // here you have the error response from the volley.
//            }
//        });
//
//        VolleyLog.DEBUG = true;
//    }


//    private void createCalls(String json, final VolleyCallBack callback) {
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        int id_history = getIntent().getIntExtra("history", 0);
//        String URL = "http://apadok.com/pemeriksaan/show/"+id_history;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("VOLLEY", response);
//                returnvalue = gson.fromJson(response, JsonObject.class);
//                callback.onSuccess();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VOLLEY", error.toString());
//                if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//                    hasil = "Aplikasi gagal terhubung ke Internet";
//                } else if (error instanceof ServerError) {
//                    hasil = "Server sedang bermasalah";
//                }  else if (error instanceof ParseError) {
//                    hasil = "Ada masalah di aplikasi Apadok";
//                }
//                callback.onError();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return json == null ? null : json.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
//                    return null;
//                }
//            }
//
//        };
//
//        requestQueue.add(stringRequest);
//    }


    private final View.OnClickListener RedirectToEducation = v -> {
        Intent intent = new Intent(ScreeningHistoryDetailActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(ScreeningHistoryDetailActivity.this, ConsultActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);
        intent.putExtra("data", sch);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToNearestClinic = v -> {
        Intent intent = new Intent(ScreeningHistoryDetailActivity.this, NearestClinicActivity.class);
        //Pass the Category to next activity (Unused)
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);

        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };

}
