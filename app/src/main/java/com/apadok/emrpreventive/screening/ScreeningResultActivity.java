package com.apadok.emrpreventive.screening;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.StringToTimeStampFormatting;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.apadok.emrpreventive.consult.ConsultActivity;
import com.apadok.emrpreventive.consult.NearestClinicActivity;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.encyclopedia.EncyclopediaActivity;
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.apadok.emrpreventive.user.LogOutAuthError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScreeningResultActivity extends AppApadokActivity {

    // Gson related
    // API return variables
    private final Gson gson = new Gson();
    private ArrayList<PemeriksaanEntity> sch;
    private String hasil = "";
    // API input Variables
    private final FormAnswer[] answer = new FormAnswer[17];
    private List<FormAnswer> answers;

    // Res/Layout Variables
    private TextView title_result, time_result, diabetes_result, stroke_result, cardiovascular_result, dangerous_result, safe_result, stroke_details, diabetes_info;
    private Button btn_consult, btn_education;

    // Intent Variables
    private int diabetval, strokeval, cardioval;
    private String ClinicName, ClinicLogo;
//    private String Role;

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
        String url = "http://apadok.com/media/institusi/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        //Assign UI Attributes
        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        diabetes_result = (TextView) findViewById(R.id.diabetes_result);
        diabetes_info = (TextView) findViewById(R.id.diabetes_info);
        stroke_result = (TextView) findViewById(R.id.stroke_result);
        stroke_details = (TextView) findViewById(R.id.stroke_details);
        cardiovascular_result = (TextView) findViewById(R.id.cardiovascular_result);
        dangerous_result = (TextView) findViewById(R.id.dangerous_result);
        safe_result = (TextView) findViewById(R.id.safe_result);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        btn_education = (Button) findViewById(R.id.btn_education);

        //Assign Font Type
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        title_result.setTypeface(helvetica_font);
        time_result.setTypeface(helvetica_font);
        diabetes_result.setTypeface(helvetica_font);
        diabetes_info.setTypeface(helvetica_font);
        stroke_result.setTypeface(helvetica_font);
        cardiovascular_result.setTypeface(helvetica_font);
        dangerous_result.setTypeface(helvetica_font);
        safe_result.setTypeface(helvetica_font);
        btn_consult.setTypeface(helvetica_font);
        btn_education.setTypeface(helvetica_font);

        //Set Justify for safe_result and dangerous_result
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            safe_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dangerous_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        //Set Default View
        title_result.setText("Hasil Skrining Risiko Penyakit");
        time_result.setText("Mengolah Data....");
        diabetes_result.setVisibility(View.GONE);
        diabetes_info.setVisibility(View.GONE);
        stroke_result.setVisibility(View.GONE);
        stroke_details.setVisibility(View.GONE);
        cardiovascular_result.setVisibility(View.GONE);
        dangerous_result.setVisibility(View.GONE);
        safe_result.setVisibility(View.GONE);
        btn_consult.setVisibility(View.GONE);
        btn_consult.setOnClickListener(RedirectToConsult);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);

//        Role = getIntent().getStringExtra("role");
//        if (Role != null) {
//            if (Role.equals("N")) {
//                btn_consult.setText("Cari Klinik");
//                btn_consult.setOnClickListener(RedirectToNearestClinic);
//            }
//        }
    }

    private void setupJson() {
        //Get Answers from Previous Activity
        answers = getIntent().getParcelableArrayListExtra("Answers");
        //Change Answers to string and then to JSON
        Type answerstype = new TypeToken<List<FormAnswer>>() {
        }.getType();
        String json = gson.toJson(answers, answerstype);
        Log.e("JSON Body", json);

        //Send JSON to API via this Function
        createCalls(json, new VolleyCallBack() {
            @Override
            //What to do when APICalls success
            public void onSuccess() {
                //Turn API Response into String
                String hasil_diabet = sch.get(0).getHasil_diabetes() == null ? "" : sch.get(0).getHasil_diabetes();
                String hasil_kardio = sch.get(0).getHasil_kardiovaskular() == null ? "" : sch.get(0).getHasil_kardiovaskular();
                String hasil_stroke = sch.get(0).getHasil_stroke() == null ? "" : sch.get(0).getHasil_stroke();
                String timestamp = sch.get(0).getUpdated_at() == null ? sch.get(0).getCreated_at() : sch.get(0).getUpdated_at();

                //Format TimeStamp into Readable
                time_result.setText(StringToTimeStampFormatting.changeFormat(timestamp,"yyyy-MM-dd HH:mm:ss", "dd MMMM yyyy HH:mm"));

                //Assign String to UI TextView
                diabetes_result.setText(hasil_diabet);
                stroke_result.setText(hasil_stroke);
                cardiovascular_result.setText(hasil_kardio);

                //Set UI TextView Visibility
                diabetes_result.setVisibility(View.VISIBLE);
                stroke_result.setVisibility(View.VISIBLE);
                cardiovascular_result.setVisibility(View.VISIBLE);

                //Set Konsultasi & Edukasi Text Paragraph
                String dangtext = "";
                String safetext = "";
                if (hasil_diabet.contains("Tinggi") && !hasil_diabet.contains("Sedikit Tinggi")) {
                    diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
                    dangtext = "penyakit diabetes";
                    diabetval = 3;
                } else if (hasil_diabet.contains("Rendah") || hasil_diabet.contains("Sedikit Tinggi")  ) {
                    diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
                    safetext = "penyakit diabetes";
                    diabetval = 1;
                } else {
                    diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_dark));
                    diabetval = 2;
                    safetext = "penyakit diabetes";
                }
                if (hasil_stroke.contains("Tinggi")) {
                    stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
                    if (dangtext.equals("")) {
                        dangtext = "penyakit stroke";
                    } else {
                        dangtext += ", penyakit stroke";
                    }
                    strokeval = 3;
                } else if (hasil_stroke.contains("Rendah")) {
                    stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
                    strokeval = 1;
                    if (safetext.equals("")) {
                        safetext = "penyakit stroke";
                    } else {
                        safetext += ", penyakit stroke";
                    }
                } else {
                    stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_dark));
                    strokeval = 2;
                    if (safetext.equals("")) {
                        safetext = "penyakit stroke";
                    } else {
                        safetext += ", penyakit stroke";
                    }
                }
                if (hasil_kardio.contains("Tinggi")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_font));
                    if (safetext.equals("")) {
                        safetext = "penyakit kardiovaskular";
                    } else {
                        safetext += ", penyakit kardiovaskular";
                    }
                    cardioval = 3;
                } else if (hasil_kardio.contains("Rendah")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green_font));
                    cardioval = 1;
                    if (safetext.equals("")) {
                        safetext = "penyakit kardiovaskular";
                    } else {
                        safetext += ", penyakit kardiovaskular";
                    }
                } else if (hasil_kardio.contains("Tidak")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_green_font));
                    cardioval = 1;
                    if (safetext.equals("")) {
                        safetext = "penyakit kardiovaskular";
                    } else {
                        safetext += ", penyakit kardiovaskular";
                    }
                } else {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_dark));
                    cardioval = 2;
                    if (safetext.equals("")) {
                        safetext = "penyakit kardiovaskular";
                    } else {
                        safetext += ", penyakit kardiovaskular";
                    }
                }

                //Add Explanation Why Risiko Menengah or Risiko Tinggi
                if (strokeval >= 2) {
                    //Turn API Response into String
                    String kadar_gula = sch.get(0).getKadar_gula() == null ? "" : sch.get(0).getKadar_gula();
                    String tekanan_darah = sch.get(0).getTekanan_darah() == null ? "" : sch.get(0).getTekanan_darah();
                    String kadar_kolesterol = sch.get(0).getKadar_kolesterol() == null ? "" : sch.get(0).getKadar_kolesterol();

                    //Set Explanation String
                    String stroke_warning = "";

                    //Update Explanation String IF API Response contains tidak diketahui for kadar gula,tekanan darah, kadar kolesterol
                    if (Objects.equals(kadar_gula, "4") || Objects.equals(tekanan_darah, "4") || Objects.equals(kadar_kolesterol, "4")){
                        if (kadar_gula.contains("4")) {
                            stroke_warning = "kadar gula darah";
                        }
                        if (tekanan_darah.contains("4")) {
                            if (stroke_warning.equals("")) {
                                stroke_warning = "tekanan darah";
                            } else {
                                stroke_warning += ", tekanan darah";
                            }
                        }
                        if (kadar_kolesterol.contains("4")) {
                            if (stroke_warning.equals("")) {
                                stroke_warning = "kadar kolesterol";
                            } else {
                                stroke_warning += ", kadar kolesterol";
                            }
                        }

                        //Set Explanation Background and Text Color
                        GradientDrawable gradientDrawable = (GradientDrawable) stroke_details.getBackground();
                        gradientDrawable.setStroke(2, Color.parseColor("#EFCC00"));
                        if (strokeval == 3){
                            gradientDrawable.setStroke(2, Color.RED);
                        }

                        //Update Explanation Text and Visibility
                        stroke_result.setText("Kemungkinan "+ hasil_stroke);
                        stroke_details.setText(hasil_stroke + " muncul karena anda mengisi jawaban pertanyaan skrining dengan pilihan tidak diketahui pada bagian " + stroke_warning);
                        stroke_details.setVisibility(View.VISIBLE);

                    } else {
                        //Get kadar gula,tekanan darah, kadar kolesterol from previous activity intent
                        Boolean tekanan_darah_intent = getIntent().getBooleanExtra("tekanan_darah",false);
                        Boolean kadar_gula_intent = getIntent().getBooleanExtra("kadar_gula",false);
                        Boolean kadar_kolesterol_intent = getIntent().getBooleanExtra("kadar_kolesterol",false);

                        //Update Explanation String IF previous activity intent contains tidak diketahui for kadar gula,tekanan darah, kadar kolesterol
                        if (tekanan_darah_intent || kadar_gula_intent || kadar_kolesterol_intent){
                            if (kadar_gula_intent) {
                                stroke_warning = "kadar gula darah";
                            }
                            if (tekanan_darah_intent) {
                                if (stroke_warning.equals("")) {
                                    stroke_warning = "tekanan darah";
                                } else {
                                    stroke_warning += ", tekanan darah";
                                }
                            }
                            if (kadar_kolesterol_intent) {
                                if (stroke_warning.equals("")) {
                                    stroke_warning = "kadar kolesterol";
                                } else {
                                    stroke_warning += ", kadar kolesterol";
                                }
                            }
                            //Set Explanation Background and Text Color
                            GradientDrawable gradientDrawable = (GradientDrawable) stroke_details.getBackground();
                            gradientDrawable.setStroke(2, Color.parseColor("#EFCC00"));
                            if (strokeval == 3){
                                gradientDrawable.setStroke(2, Color.RED);
                            }

                            //Update Explanation Text and Visibility
                            stroke_result.setText("Kemungkinan "+ hasil_stroke);
                            stroke_details.setText(hasil_stroke + " muncul karena anda mengisi jawaban pertanyaan skrining dengan pilihan tidak diketahui pada bagian " + stroke_warning);
                            stroke_details.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if(diabetval >= 2){
                    GradientDrawable gradientDrawable = (GradientDrawable) diabetes_info.getBackground();
                    gradientDrawable.setStroke(2, Color.parseColor("#EFCC00"));
                    if (diabetval == 3){
                        gradientDrawable.setStroke(2, Color.RED);
                    }
                    diabetes_info.setText("Tingkat " + hasil_diabet + " kemungkinan muncul antara lain dikarenakan faktor usia, Indeks Massa Tubuh (Berat Badan dan Tinggi Badan), lingkar pinggang, aktivitas fisik, konsumsi obat hipertensi, konsumsi buah serta sayur, pernah mengalami peningkatan gula darah serta riwayat keturunan diabetes");
                    diabetes_info.setVisibility(View.VISIBLE);
                }
                //Set Visibility for Konsultasi & Edukasi Text Paragraph
                if (diabetval == 3 || strokeval == 3 || cardioval == 3) {
                    dangerous_result.setText("Anda memiliki risiko tinggi untuk " + dangtext + ". Anda disarankan untuk melakukan konsultasi secara luring ke dokter.");
                    dangerous_result.setVisibility(View.VISIBLE);
                    btn_consult.setVisibility(View.VISIBLE);
                }
                if (diabetval != 3 && strokeval != 3 && cardioval != 3) {
                    dangerous_result.setText("Jika anda memiliki keluhan terkait dengan " + safetext + " silahkan melakukan konsultasi secara luring ke dokter");
                    dangerous_result.setVisibility(View.VISIBLE);
                    btn_consult.setVisibility(View.VISIBLE);
                }
                if (diabetval <= 2 || strokeval <= 2 || cardioval <= 2) {
                    safe_result.setText("Anda memiliki risiko rendah untuk " + safetext + ". Silahkan perhatikan edukasi pencegahan penyakit yang diberikan untuk mempertahankan capaian anda.");
                    safe_result.setVisibility(View.VISIBLE);
                    btn_education.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError() {
                //Display Error Text when API Calls Error
                time_result.setText(hasil);
            }
        });

        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        //Prepare Volley Request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Construct API URL
        String URL = "http://apadok.com/api/pemeriksaan";
        //Get Token from previous activity
        String token = getIntent().getStringExtra("token");
        //Prepare request in String Format
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            //Response when APICalls Success
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                //Turns Response into PemeriksaanEntity Object
                Type screenhistory = new TypeToken<List<PemeriksaanEntity>>() {
                }.getType();
                sch = gson.fromJson(response, screenhistory);
                // Panggil Fungsi API Lain, Simpen ke SQLite
                //Calls -> What to do when APICalls success
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                //If error is related to connectivity
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    hasil = "Aplikasi gagal terhubung ke Internet";
                //If error is related to Apadok server
                } else if (error instanceof ServerError) {
                    hasil = "Server Apadok sedang bermasalah";
                //If error is related to Authorization Token
                } else if (error instanceof AuthFailureError) {
                    //Display Error Dialog to do relogin
                    hasil = "Anda butuh Sign-In kembali\nuntuk menggunakan Apadok";
                    DialogFragment newFragment = new LogOutAuthError();
                    newFragment.show(getSupportFragmentManager(), "");
                    newFragment.setCancelable(false);
                } else if (error instanceof ParseError) {
                //If error is related to parsing
                    hasil = "Ada masalah di aplikasi Apadok";
                }
                callback.onError();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            //Set API Body
            public byte[] getBody() {
                return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        //Disable Retry Policy
        requestQueue.add(stringRequest).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    private final View.OnClickListener RedirectToEducation = v -> {
        Intent intent = new Intent(ScreeningResultActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        String token = getIntent().getStringExtra("token");
        intent.putExtra("token", token);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(ScreeningResultActivity.this, ConsultActivity.class);
        //Pass the Category to next activity (Unused)
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);

        intent.putExtra("data", sch.get(0));
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        String clinicphone = getIntent().getStringExtra("clinicphone");
        intent.putExtra("clinicphone", clinicphone);
        String username = getIntent().getStringExtra("username");
        intent.putExtra("username", username);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToNearestClinic = v -> {
        Intent intent = new Intent(ScreeningResultActivity.this, NearestClinicActivity.class);
        //Pass the Category to next activity (Unused)
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);

        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
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
