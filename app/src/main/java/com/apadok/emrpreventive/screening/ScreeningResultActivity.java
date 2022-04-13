package com.apadok.emrpreventive.screening;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.apadok.emrpreventive.consult.ConsultActivity;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.encyclopedia.EncyclopediaActivity;
import com.apadok.emrpreventive.user.LogOutAuthError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreeningResultActivity extends AppCompatActivity {

    // Gson related
    // API return variables
    private Gson gson = new Gson();
    private JsonObject returnvalue;
    private PemeriksaanEntity sch;
    private String hasil = "";
    // API input Variables
    private FormAnswer[] answer = new FormAnswer[17];
    private List<FormAnswer> answers;

    // Res/Layout Variables
    private TextView title_result, time_result, diabetes_result, stroke_result, cardiovascular_result, dangerous_result, safe_result;
    private Button btn_consult, btn_education;

    // Intent Variables
    private int diabetval,strokeval,cardioval;
    private String ClinicName,ClinicLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_result);
        setupItemView();
        setupJson();
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

        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        diabetes_result = (TextView) findViewById(R.id.diabetes_result);
        stroke_result = (TextView) findViewById(R.id.stroke_result);
        cardiovascular_result = (TextView) findViewById(R.id.cardiovascular_result);
        dangerous_result = (TextView) findViewById(R.id.dangerous_result);
        safe_result = (TextView) findViewById(R.id.safe_result);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        btn_education = (Button) findViewById(R.id.btn_education);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        title_result.setTypeface(helvetica_font);
        time_result.setTypeface(helvetica_font);
        diabetes_result.setTypeface(helvetica_font);
        stroke_result.setTypeface(helvetica_font);
        cardiovascular_result.setTypeface(helvetica_font);
        dangerous_result.setTypeface(helvetica_font);
        safe_result.setTypeface(helvetica_font);
        btn_consult.setTypeface(helvetica_font);
        btn_education.setTypeface(helvetica_font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            safe_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dangerous_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        title_result.setText("Hasil Skrining");
        time_result.setText("Mengolah Data....");
        diabetes_result.setVisibility(View.GONE);
        stroke_result.setVisibility(View.GONE);
        cardiovascular_result.setVisibility(View.GONE);
        dangerous_result.setVisibility(View.GONE);
        safe_result.setVisibility(View.GONE);
        btn_consult.setVisibility(View.GONE);
        btn_consult.setOnClickListener(RedirectToConsult);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);
    }

    private void setupJson() {
        answers = getIntent().getParcelableArrayListExtra("Answers");
        //Ubah Answers ke string trus ke JSON
        Type answerstype = new TypeToken<List<FormAnswer>>() {}.getType();
        String json = gson.toJson(answers, answerstype);
        Log.e("JSON Body", json);

        // Send JSON ke API & Parse Respons di createcall
        // Parse JSON Respons di createcall
        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
        createCalls(json,new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
                String hasil_diabet = returnvalue.get("hasil_diabetes").isJsonNull() ? "" : returnvalue.get("hasil_diabetes").getAsString();
                String hasil_stroke = returnvalue.get("hasil_stroke").isJsonNull() ? "" : returnvalue.get("hasil_stroke").getAsString();
                String hasil_kardio = returnvalue.get("hasil_kolesterol").isJsonNull() ? "" : returnvalue.get("hasil_kolesterol").getAsString();
                String timestamp = returnvalue.get("updated_at").isJsonNull() ? returnvalue.get("created_at").getAsString() : returnvalue.get("updated_at").getAsString();

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
                    if (dangtext == ""){
                        dangtext = "penyakit stroke";
                    } else  {
                        dangtext += ", penyakit stroke";
                    }
                    strokeval = 3;
                } else if (hasil_stroke.contains("Rendah")) {
                    stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
                    strokeval = 1;
                    if (safetext == ""){
                        safetext = "penyakit stroke";
                    } else  {
                        safetext += ", penyakit stroke";
                    }
                } else {
                    stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
                    strokeval = 2;
                    if (safetext == ""){
                        safetext = "penyakit stroke";
                    } else  {
                        safetext += ", penyakit stroke";
                    }
                }
                if (hasil_kardio.contains("Tinggi")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
                    if (safetext == ""){
                        safetext = "penyakit kardiovaskular";
                    } else {
                        safetext += ", penyakit kardiovaskular";
                    }
                    cardioval = 3;
                } else if (hasil_kardio.contains("Rendah")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
                    cardioval = 1;
                    if (safetext == ""){
                        safetext = "penyakit kardiovaskular";
                    } else  {
                        safetext += ", penyakit kardiovaskular";
                    }
                }
                else if (hasil_kardio.contains("Tidak")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_green_font));
                    cardioval = 1;
                    if (safetext == ""){
                        safetext = "penyakit kardiovaskular";
                    } else  {
                        safetext += ", penyakit kardiovaskular";
                    }
                }else {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
                    cardioval = 2;
                    if (safetext == ""){
                        safetext = "penyakit kardiovaskular";
                    } else  {
                        safetext += ", penyakit kardiovaskular";
                    }
                }
                if (diabetval == 3 || strokeval == 3 || cardioval == 3) {
                    dangerous_result.setText("Tubuh anda memiliki resiko tinggi untuk "+ dangtext +" sehingga membutuhkan konsultasi secara offline ke dokter");
                    dangerous_result.setVisibility(View.VISIBLE);
                    btn_consult.setVisibility(View.VISIBLE);
                }
                if(diabetval!=3 && strokeval!=3 && cardioval!=3){
                    dangerous_result.setText("Jika anda memiliki keluhan terkait dengan "+ safetext +" silahkan melakukan konsultasi secara offline ke dokter");
                    dangerous_result.setVisibility(View.VISIBLE);
                    btn_consult.setVisibility(View.VISIBLE);
                }
                if (diabetval <= 2 || strokeval <= 2 || cardioval <= 2){
                    safe_result.setText("Untuk "+ safetext +" pada tubuh anda memiliki risiko yang tidak terlalu membahayakan. Silahkan melihat edukasi pencegahan penyakit tersebut berikut untuk mempertahankan capaian anda tersebut.");
                    safe_result.setVisibility(View.VISIBLE);
                    btn_education.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError() {
                time_result.setText(hasil);
            }
        });

        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://178.128.25.139:8080/api/pemeriksaan";
        String token = getIntent().getStringExtra("token");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                returnvalue = gson.fromJson(response, JsonObject.class);
                sch = gson.fromJson(response, PemeriksaanEntity.class);
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    hasil = "Tidak ada Jaringan Internet";

                } else if (error instanceof ServerError || error instanceof AuthFailureError) {
//                    hasil = "Server sedang bermasalah";
                    hasil = "Anda butuh Sign-In kembali\nuntuk menggunakan Apadok";
                    DialogFragment newFragment = new LogOutAuthError();
                    newFragment.show(getSupportFragmentManager(), "");
                }  else if (error instanceof ParseError) {
                    hasil = "Ada masalah di aplikasi Apadok";
                }
//                else if (error instanceof AuthFailureError) {
//                    DialogFragment newFragment = new LogOutAuthError();
//                    newFragment.show(getSupportFragmentManager(), "");
//                }
                callback.onError();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private final View.OnClickListener RedirectToEducation = v -> {
        Intent intent = new Intent(ScreeningResultActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(ScreeningResultActivity.this, ConsultActivity.class);
        //Pass the Category to next activity (Unused)
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);

        intent.putExtra("data", sch);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };
}
