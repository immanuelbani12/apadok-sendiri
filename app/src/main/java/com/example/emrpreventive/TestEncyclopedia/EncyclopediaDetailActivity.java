package com.example.emrpreventive.TestEncyclopedia;

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
import androidx.appcompat.widget.Toolbar;

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
import com.example.emrpreventive.R;
import com.example.emrpreventive.SetupToolbar;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncyclopediaDetailActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private JsonObject returnvalue;
    private String hasil = "";
    private TextView tv_title, tv_diabetes, tv_cardiovascular, tv_stroke, tv_result;
    private ImageView iv_trophy;
    private Button btn_finish, btn_whatsapp;

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
//                iv_trophy.setVisibility(View.VISIBLE);
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

        tv_title = (TextView) findViewById(R.id.title_result);
        tv_result = (TextView) findViewById(R.id.result);
        tv_diabetes = (TextView) findViewById(R.id.diabetes_title);
        tv_stroke = (TextView) findViewById(R.id.stroke_title);
        tv_cardiovascular = (TextView) findViewById(R.id.cardiovascular_title);

        int position = getIntent().getIntExtra("position", 0);
        tv_title.setText(getIntent().getStringExtra("judul_artikel"));
        tv_result.setText(getIntent().getStringExtra("isi_artikel"));
        String kategori = getIntent().getStringExtra("kategori_artikel");
        int kategoriint = Integer.parseInt(kategori);

        if (kategoriint == 1){
            tv_diabetes.setVisibility(View.GONE);
            tv_cardiovascular.setVisibility(View.GONE);
        }

        if (kategoriint == 2){
            tv_stroke.setVisibility(View.GONE);
            tv_cardiovascular.setVisibility(View.GONE);
        }

        if (kategoriint == 3){
            tv_stroke.setVisibility(View.GONE);
            tv_diabetes.setVisibility(View.GONE);
        }
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

    private final View.OnClickListener RedirectToFinish = v -> {
//        Use Finish Instead of Adding New Activity to the Stack
//        startActivity(new Intent(StrokeResultActivity.this, MainActivity.class));
        finish();
    };
}
