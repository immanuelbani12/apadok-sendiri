package com.example.emrpreventive.shorting.stroke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.emrpreventive.LogOutAuthError;
import com.example.emrpreventive.SetupToolbar;
import com.example.emrpreventive.TestEncyclopedia.EncyclopediaActivity;
import com.example.emrpreventive.shorting.screeninghistory.ScreeningHistoryDetailActivity;
import com.google.gson.Gson;

import com.example.emrpreventive.MainActivity;
import com.example.emrpreventive.R;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

public class StrokeResultActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private JsonObject returnvalue;
    private String hasil = "";
    private FormAnswer[] answer = new FormAnswer[17];
    private List<FormAnswer> answers;
    private TextView title_result, time_result, diabetes_result, stroke_result, cardiovascular_result, dangerous_result, safe_result;
    private Button btn_consult, btn_education;
    private int diabetval,strokeval,cardioval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_result);
        setupItemView();
        setupJson();
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);

        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        diabetes_result = (TextView) findViewById(R.id.diabetes_result);
        stroke_result = (TextView) findViewById(R.id.stroke_result);
        cardiovascular_result = (TextView) findViewById(R.id.cardiovascular_result);
        dangerous_result = (TextView) findViewById(R.id.dangerous_result);
        safe_result = (TextView) findViewById(R.id.safe_result);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        btn_education = (Button) findViewById(R.id.btn_education);

        title_result.setText("Hasil Skrining");
        time_result.setText("Mengolah Data....");
        diabetes_result.setVisibility(View.GONE);
        stroke_result.setVisibility(View.GONE);
        cardiovascular_result.setVisibility(View.GONE);
        dangerous_result.setVisibility(View.GONE);
        safe_result.setVisibility(View.GONE);
        btn_consult.setVisibility(View.GONE);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);
    }

    private void setupJson() {
        answers = getIntent().getParcelableArrayListExtra("Answers");
        //Ubah Answers ke string trus ke JSON
        Type answerstype = new TypeToken<List<FormAnswer>>() {}.getType();
        String json = gson.toJson(answers, answerstype);
        Log.e("bobo", json);

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
                    diabetes_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_font));
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
                    stroke_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_font));
                    strokeval = 1;
                    if (dangtext == ""){
                        dangtext = "penyakit stroke";
                    } else  {
                        dangtext += ", penyakit stroke";
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
                        safetext = "penyakit cardiovascular";
                    } else {
                        safetext += ", penyakit cardiovascular";
                    }
                    cardioval = 3;
                } else if (hasil_kardio.contains("Rendah") || hasil_kardio.contains("Tidak")) {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_font));
                    cardioval = 1;
                    if (safetext == ""){
                        safetext = "penyakit cardiovascular";
                    } else  {
                        safetext += ", penyakit cardiovascular";
                    }
                } else {
                    cardiovascular_result.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_font));
                    cardioval = 2;
                    if (safetext == ""){
                        safetext = "penyakit cardiovascular";
                    } else  {
                        safetext += ", penyakit cardiovascular";
                    }
                }

                if (diabetval== 3 || strokeval == 3 || cardioval == 3) {
                    dangerous_result.setText("Tubuh anda memiliki resiko tinggi untuk "+ dangtext +" sehingga membutuhkan konsultasi secara offline ke dokter");
                    dangerous_result.setVisibility(View.VISIBLE);
                    btn_consult.setVisibility(View.VISIBLE);
                }
                if (diabetval <= 2 || strokeval <= 2 || cardioval <= 2){
                    safe_result.setText("Untuk "+ safetext +" silahkan melihat edukasi pencegahan penyakit tersebut berikut.");
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                returnvalue = gson.fromJson(response, JsonObject.class);
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



//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//                    responseString = String.valueOf(response.data);
//
//                    // can get more details such as response.headers
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }
        };

        requestQueue.add(stringRequest);
    }

//    private View.OnClickListener openWhatsApp = v ->{
//        PackageManager packageManager = StrokeResultActivity.this.getPackageManager();
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        String numero = "+62 81282352027";
//        String mensaje = "Text";
//        String url = null;
//        try {
//            url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        i.setPackage("com.whatsapp");
//        i.setData(Uri.parse(url));
//        if (i.resolveActivity(packageManager) != null) {
//            startActivity(i);
//        }else {
//            Intent viewIntent =
//                    new Intent("android.intent.action.VIEW",
//                            Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
//            startActivity(viewIntent);
////                KToast.errorToast(StrokeResultActivity.this, getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
//        }
//
//    };

    private final View.OnClickListener RedirectToEducation = v -> {
        Intent intent = new Intent(StrokeResultActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorydiabetes", diabetval);
        intent.putExtra("categorystroke", strokeval);
        intent.putExtra("categorykardio", cardioval);
        startActivity(intent);
    };
}
