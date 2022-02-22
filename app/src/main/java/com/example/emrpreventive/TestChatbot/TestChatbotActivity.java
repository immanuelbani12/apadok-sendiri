package com.example.emrpreventive.TestChatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

public class TestChatbotActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private TextView tv_timeresult2, tv_btn_education, tv_btn_education2, tv_btn_education3, tv_timeresult4, tv_test, tv_test2, tv_test3, tv_test4, tv_test5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_temporary);
        setupItemView();
    }

    private void setupItemView() {
        tv_timeresult2 = (TextView) findViewById(R.id.time_result2);
        tv_btn_education = (TextView) findViewById(R.id.btn_education);
        tv_btn_education2 = (TextView) findViewById(R.id.btn_education2);
        tv_btn_education3 = (TextView) findViewById(R.id.btn_education3);
        tv_timeresult4 = (TextView) findViewById(R.id.time_result4);

        tv_timeresult2.setVisibility(View.GONE);
        tv_btn_education.setVisibility(View.GONE);
        tv_btn_education2.setVisibility(View.GONE);
        tv_btn_education3.setVisibility(View.GONE);
        tv_timeresult4.setVisibility(View.GONE);
    }

    public void show_question1(View view) {
        tv_test = (TextView) findViewById(R.id.time_result2);
        tv_test2 = (TextView) findViewById(R.id.btn_education);
        tv_test3 = (TextView) findViewById(R.id.btn_education2);
        tv_test4 = (TextView) findViewById(R.id.btn_education3);
        tv_test.setVisibility(View.VISIBLE);
        tv_test2.setVisibility(View.VISIBLE);
        tv_test3.setVisibility(View.VISIBLE);
        tv_test4.setVisibility(View.VISIBLE);
    }
    public void show_question2(View view) {
        tv_test3.setVisibility(View.GONE);
        tv_test4.setVisibility(View.GONE);
        tv_test5 = (TextView) findViewById(R.id.time_result4);
        tv_test5.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada pagi hari");
        tv_test5.setVisibility(View.VISIBLE);
    }
    public void show_question3(View view) {
        tv_test2.setVisibility(View.GONE);
        tv_test4.setVisibility(View.GONE);
        tv_test5 = (TextView) findViewById(R.id.time_result4);
        tv_test5.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada siang hari");
        tv_test5.setVisibility(View.VISIBLE);
    }
    public void show_question4(View view) {
        tv_test2.setVisibility(View.GONE);
        tv_test3.setVisibility(View.GONE);
        tv_test5 = (TextView) findViewById(R.id.time_result4);
        tv_test5.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada sore hari");
        tv_test5.setVisibility(View.VISIBLE);
    }
}
