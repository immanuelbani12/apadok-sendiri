package com.example.emrpreventive.TestChatbot;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.emrpreventive.SetupToolbar;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

public class TestChatbotActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private Button btn_penjadwalan, btn_pagi, btn_siang, btn_sore;
    private ImageButton btn_chat_send;
    private TextView tv_title_activity, tv_first_chat, tv_second_chat, tv_third_chat;
    private EditText edit_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_temporary);
        setupItemView();
    }

    private void setupItemView() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        String clinicname = sharedPref.getString("clinicnamelocal", "");
        clinic.setText(clinicname);

        tv_title_activity = (TextView) findViewById(R.id.tv_title_activity);
        tv_first_chat = (TextView) findViewById(R.id.tv_first_chat);
        String username = sharedPref.getString("usernamelocal", "");
        tv_first_chat.setText("Halo "+ username +", saya Putri, asisten virtual APADOK. Apakah ada yang bisa kami bantu?");
        tv_second_chat = (TextView) findViewById(R.id.tv_second_chat);
        tv_third_chat = (TextView) findViewById(R.id.tv_third_chat);
        btn_penjadwalan = (Button) findViewById(R.id.btn_penjadwalan);
        btn_pagi = (Button) findViewById(R.id.btn_pagi);
        btn_siang = (Button) findViewById(R.id.btn_siang);
        btn_sore = (Button) findViewById(R.id.btn_sore);
        edit_message = (EditText) findViewById(R.id.edit_message);
        btn_chat_send = (ImageButton) findViewById(R.id.btn_chat_send);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        tv_title_activity.setTypeface(helvetica_font);
        tv_first_chat.setTypeface(helvetica_font);
        tv_second_chat.setTypeface(helvetica_font);
        tv_third_chat.setTypeface(helvetica_font);
        btn_penjadwalan.setTypeface(helvetica_font);
        btn_pagi.setTypeface(helvetica_font);
        btn_siang.setTypeface(helvetica_font);
        btn_sore.setTypeface(helvetica_font);
        btn_chat_send.setEnabled(false);
        edit_message.setTypeface(helvetica_font);
        edit_message.setEnabled(false);
        edit_message.setInputType(InputType.TYPE_NULL);


        tv_second_chat.setVisibility(View.GONE);
        btn_pagi.setVisibility(View.GONE);
        btn_siang.setVisibility(View.GONE);
        btn_sore.setVisibility(View.GONE);
        tv_third_chat.setVisibility(View.GONE);
    }

    public void show_question1(View view) {
        btn_penjadwalan.setEnabled(false);
        tv_second_chat.setVisibility(View.VISIBLE);
        btn_pagi.setVisibility(View.VISIBLE);
        btn_siang.setVisibility(View.VISIBLE);
        btn_sore.setVisibility(View.VISIBLE);
    }
    public void show_question2(View view) {
        btn_penjadwalan.setEnabled(false);
        btn_pagi.setEnabled(false);
        btn_siang.setEnabled(false);
        btn_sore.setEnabled(false);
        tv_third_chat.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada pagi hari");
        tv_third_chat.setVisibility(View.VISIBLE);
    }
    public void show_question3(View view) {
        btn_penjadwalan.setEnabled(false);
        btn_pagi.setEnabled(false);
        btn_siang.setEnabled(false);
        btn_sore.setEnabled(false);
        tv_third_chat.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada siang hari");
        tv_third_chat.setVisibility(View.VISIBLE);
    }
    public void show_question4(View view) {
        btn_penjadwalan.setEnabled(false);
        btn_pagi.setEnabled(false);
        btn_siang.setEnabled(false);
        btn_sore.setEnabled(false);
        tv_third_chat.setText("Terima Kasih, kami sudah menjadwalkan anda untuk bertemu dengan Dokter pada sore hari");
        tv_third_chat.setVisibility(View.VISIBLE);
    }
}
