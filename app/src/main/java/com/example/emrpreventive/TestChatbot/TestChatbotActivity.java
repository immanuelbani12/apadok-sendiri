package com.example.emrpreventive.TestChatbot;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
    private Button btn_penjadwalan, btn_pagi, btn_siang, btn_sore;
    private TextView tv_title_activity, tv_first_chat, tv_second_chat, tv_third_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_temporary);
        setupItemView();
    }

    private void setupItemView() {

        tv_title_activity = (TextView) findViewById(R.id.tv_title_activity);
        tv_first_chat = (TextView) findViewById(R.id.tv_first_chat);
        tv_second_chat = (TextView) findViewById(R.id.tv_second_chat);
        tv_third_chat = (TextView) findViewById(R.id.tv_third_chat);
        btn_penjadwalan = (Button) findViewById(R.id.btn_penjadwalan);
        btn_pagi = (Button) findViewById(R.id.btn_pagi);
        btn_siang = (Button) findViewById(R.id.btn_siang);
        btn_sore = (Button) findViewById(R.id.btn_sore);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        tv_title_activity.setTypeface(helvetica_font);
        tv_first_chat.setTypeface(helvetica_font);
        tv_second_chat.setTypeface(helvetica_font);
        tv_third_chat.setTypeface(helvetica_font);
        btn_penjadwalan.setTypeface(helvetica_font);
        btn_pagi.setTypeface(helvetica_font);
        btn_siang.setTypeface(helvetica_font);
        btn_sore.setTypeface(helvetica_font);

        tv_second_chat.setVisibility(View.GONE);
        btn_pagi.setVisibility(View.GONE);
        btn_siang.setVisibility(View.GONE);
        btn_sore.setVisibility(View.GONE);
        tv_third_chat.setVisibility(View.GONE);
    }

    public void show_question1(View view) {
//        tv_test = (TextView) findViewById(R.id.time_result2);
//        tv_test2 = (TextView) findViewById(R.id.btn_education);
//        tv_test3 = (TextView) findViewById(R.id.btn_education2);
//        tv_test4 = (TextView) findViewById(R.id.btn_education3);
        btn_penjadwalan.setEnabled(false);
        tv_second_chat.setVisibility(View.VISIBLE);
        btn_pagi.setVisibility(View.VISIBLE);
        btn_siang.setVisibility(View.VISIBLE);
        btn_sore.setVisibility(View.VISIBLE);
    }
    public void show_question2(View view) {
//        tv_test3.setVisibility(View.GONE);
//        tv_test4.setVisibility(View.GONE);
//        tv_test5 = (TextView) findViewById(R.id.time_result4);
        btn_penjadwalan.setEnabled(false);
        btn_pagi.setEnabled(false);
        btn_siang.setEnabled(false);
        btn_sore.setEnabled(false);
        tv_third_chat.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada pagi hari");
        tv_third_chat.setVisibility(View.VISIBLE);
    }
    public void show_question3(View view) {
//        tv_test2.setVisibility(View.GONE);
//        tv_test4.setVisibility(View.GONE);
//        tv_test5 = (TextView) findViewById(R.id.time_result4);
        btn_penjadwalan.setEnabled(false);
        btn_pagi.setEnabled(false);
        btn_siang.setEnabled(false);
        btn_sore.setEnabled(false);
        tv_third_chat.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada siang hari");
        tv_third_chat.setVisibility(View.VISIBLE);
    }
    public void show_question4(View view) {
//        tv_test2.setVisibility(View.GONE);
//        tv_test3.setVisibility(View.GONE);
//        tv_test5 = (TextView) findViewById(R.id.time_result4);
        btn_penjadwalan.setEnabled(false);
        btn_pagi.setEnabled(false);
        btn_siang.setEnabled(false);
        btn_sore.setEnabled(false);
        tv_third_chat.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada sore hari");
        tv_third_chat.setVisibility(View.VISIBLE);
    }
}
