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
import com.google.gson.JsonObject;
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
    private RequestQueue queue;
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
        //Ubah Answers ke string trus ke JSON
        Type answerstype = new TypeToken<List<FormAnswer>>() {}.getType();
        String json = gson.toJson(answers, answerstype);
        Log.e("bobo", json);

        // Send JSON ke API & Parse Respons di createcall
        // Parse JSON Respons di createcall
        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
        // Handling Callback need Test
        queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";
        queue.start();
        createCall(Request.Method.POST,url, json, new VolleyCallBack() {

            @Override
            public void onSuccess() {
                //Tampilkan sesuatu bisa lihat contoh dibawah
            }
            });



        //Tampilkan sesuatu bisa lihat contoh dibawah(Rujukan atas)
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


    private void createCall(int type, String url, String json, final VolleyCallBack callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // textView.setText("Response is: "+ response.substring(0,500));
                        // Olah Respons API Success disini ya
                        callback.onSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
                // Olah Respons API Error disini ya
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
        queue.add(stringRequest);
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
