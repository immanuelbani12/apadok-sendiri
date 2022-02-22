package com.example.emrpreventive.shorting.screeninghistory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.example.emrpreventive.MainActivity;
import com.example.emrpreventive.R;
import com.example.emrpreventive.TestEncyclopedia.EncyclopediaActivity;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ScreeningHistoryDetailActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private JsonObject returnvalue;
    private String hasil = "";
    private TextView title_result, time_result, diabetes_result, stroke_result, cardiovascular_result, dangerous_result, safe_result;
    private Button btn_consult, btn_education;
    private int stroke,kardiio,diabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_result);
        setupItemView();
        setupItemData();
//        setupJson();
    }

    private void setupItemData() {
        ScreeningHistory sch = getIntent().getParcelableExtra("data");
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
        int diabetval, strokeval, cardioval;
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

        // Please Change those 3 Val into Var Global and revamp the code
        stroke = strokeval;
        diabet = diabetval;
        kardiio = cardioval;
    }


    private void setupJson() {
        // Send JSON ke API & Parse Respons di createcall
        // Parse JSON Respons di createcall
        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
        createCalls("",new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
            }

            @Override
            public void onError() {
                // here you have the error response from the volley.
            }
        });

        VolleyLog.DEBUG = true;
    }

    private void setupItemView() {
        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        diabetes_result = (TextView) findViewById(R.id.diabetes_result);
        stroke_result = (TextView) findViewById(R.id.stroke_result);
        cardiovascular_result = (TextView) findViewById(R.id.cardiovascular_result);
        dangerous_result = (TextView) findViewById(R.id.dangerous_result);
        safe_result = (TextView) findViewById(R.id.safe_result);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        btn_education = (Button) findViewById(R.id.btn_education);

        int position = getIntent().getIntExtra("position", 0);
        title_result.setText("Riwayat Skrining "+ position);
        time_result.setText("Mengambil Data....");
        diabetes_result.setVisibility(View.GONE);
        stroke_result.setVisibility(View.GONE);
        cardiovascular_result.setVisibility(View.GONE);
        dangerous_result.setVisibility(View.GONE);
        safe_result.setVisibility(View.GONE);
        btn_consult.setVisibility(View.GONE);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);

    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int id_history = getIntent().getIntExtra("history", 0);
        String URL = "http://178.128.25.139:8080/pemeriksaan/show/"+id_history;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
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
                if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    hasil = "Tidak ada Jaringan Internet";
                } else if (error instanceof ServerError) {
                    hasil = "Server sedang bermasalah";
                }  else if (error instanceof ParseError) {
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
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }

        };

        requestQueue.add(stringRequest);
    }

//    private View.OnClickListener openWhatsApp = v ->{
//        PackageManager packageManager = ScreeningHistoryDetailActivity.this.getPackageManager();
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
        Intent intent = new Intent(ScreeningHistoryDetailActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorydiabetes", diabet);
        intent.putExtra("categorystroke", stroke);
        intent.putExtra("categorykardio", kardiio);
        startActivity(intent);
    };

}
