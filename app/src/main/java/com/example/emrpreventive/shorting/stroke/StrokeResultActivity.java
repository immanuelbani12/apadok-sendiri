package com.example.emrpreventive.shorting.stroke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import com.example.emrpreventive.MainActivity;
import com.example.emrpreventive.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrokeResultActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private FormAnswer[] answer = new FormAnswer[17];
    private List<FormAnswer> answers;
    private TextView tv_score;
    private Button btn_finish, btn_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_result);
        setupItemView();
    }

    private void setupItemView() {

        answers = getIntent().getParcelableArrayListExtra("Answers");
        //Ubah Answers ke JSON
        Type answerstype = new TypeToken<List<FormAnswer>>() {}.getType();
        String json = gson.toJson(answers, answerstype);
        // Ikutin Stackexchange yg gw kirim di LINE buat rombak line 60-87, masukin ke fungsi baru aja. trs panggil fungsinya disini.
        //Send JSON ke API & Get Respons
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", json);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();



        //Indikator Nunggu respons bisa diatur diatas
        //Parse JSON Respons bisa diatur diatas
        //Tampilkan sesuatu bisa lihat contoh dibawah





        int ScoreHigh,ScoreMed,ScoreLow;
        tv_score = (TextView) findViewById(R.id.tv_score);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            ScoreHigh = bundle.getInt("ScoreHigh");
            ScoreMed = bundle.getInt("ScoreMed");
            ScoreLow = bundle.getInt("ScoreLow");
            if(ScoreHigh >= 3) {
                tv_score.setText("Berresiko Tinggi");
            }
            else if (3 > ScoreMed && ScoreMed > 7) {
                tv_score.setText("Berresiko Sedang");
            }
            else {
                tv_score.setText("Berresiko Rendah");
            }
        }
        btn_finish.setOnClickListener(RedirectToFinish);
        btn_whatsapp.setOnClickListener(openWhatsApp);

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
        startActivity(new Intent(StrokeResultActivity.this, MainActivity.class));
    };
}
