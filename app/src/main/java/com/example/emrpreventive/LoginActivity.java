package com.example.emrpreventive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

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
import com.example.emrpreventive.shorting.TestLogin;
import com.example.emrpreventive.shorting.screeninghistory.ScreeningHistory;
import com.example.emrpreventive.shorting.screeninghistory.ScreeningHistoryActivity;
import com.example.emrpreventive.shorting.stroke.StrokeFormActivity;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btn_masuk;
    private TextView tv_support_by, phone_text;
    private EditText phone_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setupItemView();
    }

    private void setupItemView(){
        //Button
        btn_masuk = (Button) findViewById(R.id.btn_masuk);
        tv_support_by = (TextView) findViewById(R.id.support_by);
        phone_text = (TextView) findViewById(R.id.phone_text);
        phone_input = (EditText) findViewById(R.id.phone_input);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        tv_support_by.setTypeface(helvetica_font);
        btn_masuk.setTypeface(helvetica_font);
        phone_input.setTypeface(helvetica_font);
        phone_text.setTypeface(helvetica_font);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                phone_input = (EditText)findViewById(R.id.phone_input);
                String count = phone_input.getText().toString();
                Integer id = 0;

                // Creating array of string length & convert to uppercase
                char[] ch = new char[count.length()];
                count.toUpperCase();

                // Copy character by character into array
                for (int i = 0; i < count.length(); i++) {
                    ch[i] = count.charAt(i);
                }

                for (int i = 0; i < count.length(); i++){
                    id = id + (i+1) * (ch[i] - 64);
                }

                id = Math. abs(id);
                id = id % 200;

                Log.e("login", String.valueOf(id));

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //Pass the User ID to next activity
                intent.putExtra("user", id);
                startActivity(intent);
                finish();
            }
        });
    }


}