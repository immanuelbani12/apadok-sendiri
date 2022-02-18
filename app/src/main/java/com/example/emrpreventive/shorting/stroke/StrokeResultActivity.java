package com.example.emrpreventive.shorting.stroke;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView tv_score, tv_informasi;
    private ImageView iv_trophy;
    private Button btn_finish, btn_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_result);
        setupItemView();
        setupJson();
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
                String hasil_koles = returnvalue.get("hasil_kolesterol").isJsonNull() ? "" : returnvalue.get("hasil_kolesterol").getAsString();
                hasil = "Anda Memiliki \nRisiko Diabetes "+hasil_diabet+" \nRisiko " + hasil_stroke+" \nRisiko Kolesterol " + hasil_koles;
                tv_score.setText(hasil);
                iv_trophy.setVisibility(View.VISIBLE);
                tv_informasi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                tv_score.setText(hasil);
            }
        });

        VolleyLog.DEBUG = true;
    }

    private void setupItemView() {
        tv_score = (TextView) findViewById(R.id.tv_score);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);
        tv_informasi = (TextView) findViewById(R.id.tv_informasi);
        iv_trophy = (ImageView) findViewById(R.id.iv_trophy);
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//
//        if(bundle != null){
//            ScoreHigh = bundle.getInt("ScoreHigh");
//            ScoreMed = bundle.getInt("ScoreMed");
//            ScoreLow = bundle.getInt("ScoreLow");
//            if(ScoreHigh >= 3) {
//                tv_score.setText("Berresiko Tinggi");
//            }
//            else if (3 > ScoreMed && ScoreMed > 7) {
//                tv_score.setText("Berresiko Sedang");
//            }
//            else {
//                tv_score.setText("Berresiko Rendah");
//            }
//        }
        tv_score.setText("Mengolah Data....");
        iv_trophy.setVisibility(View.GONE);
        tv_informasi.setVisibility(View.GONE);
        btn_finish.setOnClickListener(RedirectToFinish);
        btn_whatsapp.setOnClickListener(openWhatsApp);

    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://178.128.25.139:8080/pemeriksaan";

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

    private View.OnClickListener openWhatsApp = v ->{
        PackageManager packageManager = StrokeResultActivity.this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String numero = "+62 81282352027";
        String mensaje = "Text";
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i);
        }else {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
            startActivity(viewIntent);
//                KToast.errorToast(StrokeResultActivity.this, getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
        }

    };

    private final View.OnClickListener RedirectToFinish = v -> {
//        Use Finisih Instead of Adding New Activity to the Stack
        startActivity(new Intent(StrokeResultActivity.this, MainActivity.class));
        finish();
    };
}
