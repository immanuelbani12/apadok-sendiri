package com.apadok.emrpreventive.user;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
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
import com.apadok.emrpreventive.MainActivity;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.EmptyTextWatcher;
import com.apadok.emrpreventive.common.PopUpMessage;
import com.apadok.emrpreventive.common.RegexorChecker;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppApadokActivity {

    // API Variables
    private final Gson gson = new Gson();
    private JsonObject returnvalue;

    // Res/Layout Variables
    private Button btn_masuk;
    private TextView phone_text, additional_text, register_text;
    private EditText phone_input;
    private String ErrorMsg;
    private final RegexorChecker regex = new RegexorChecker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupItemView();
    }

    private void setupItemView() {
        //Button
        btn_masuk = (Button) findViewById(R.id.btn_masuk);
//        tv_support_by = (TextView) findViewById(R.id.support_by);
        phone_text = (TextView) findViewById(R.id.phone_text);
        phone_input = (EditText) findViewById(R.id.phone_input);
        additional_text = (TextView) findViewById(R.id.additional_text);
        register_text = (TextView) findViewById(R.id.register);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
//        tv_support_by.setTypeface(helvetica_font);
        btn_masuk.setTypeface(helvetica_font);
        btn_masuk.setEnabled(false);
        additional_text.setTypeface(helvetica_font);
        register_text.setTypeface(helvetica_font);
        phone_input.setTypeface(helvetica_font);
        phone_text.setTypeface(helvetica_font);

        phone_input.addTextChangedListener(new EmptyTextWatcher() {

            @Override
            public void onEmptyField() {
                btn_masuk.setEnabled(false);
            }

            @Override
            public void onFilledField() {
                if (phone_input.getText().toString().length() > 0) {
                    if (regex.PhoneChecker(phone_input.getText().toString())){
                        btn_masuk.setEnabled(true);
                    } else {
                        btn_masuk.setEnabled(false);
                        phone_input.setError("Nomor handphone membutuhkan 9-17 digit");
                    }
                } else {
                    btn_masuk.setEnabled(false);
                }
            }
        });

//        connect text view to sign up activity
        register_text.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                phone_input = (EditText) findViewById(R.id.phone_input);
                String count = phone_input.getText().toString();
                setupJson(count);
            }
        });

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                DialogFragment newFragment = new ConfirmExiting();
                //Pass the User ID to next activity
                ((ConfirmExiting) newFragment).setMessage("Anda ingin keluar dari aplikasi?");
                newFragment.show(getSupportFragmentManager(), "");
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupJson(String phonenum) {
        //Construct Obj with Phonenum + Change Obj to string then to JSON
        User Obj = new User(phonenum);
        Type user = new TypeToken<User>() {
        }.getType();
        String json = gson.toJson(Obj, user);

        // Send JSON ke API & Parse Respons di createcall
        // Parse JSON Respons di createcall
        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
        createCalls(json, new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
                String userid = returnvalue.get("id_user").isJsonNull() ? "" : returnvalue.get("id_user").getAsString();
                String username = returnvalue.get("nama_user").isJsonNull() ? "" : returnvalue.get("nama_user").getAsString();
                String role = returnvalue.get("role").isJsonNull() ? "" : returnvalue.get("role").getAsString();
                String token = returnvalue.get("token").isJsonNull() ? "" : returnvalue.get("token").getAsString();
                String clinicname = returnvalue.get("nama_klinik").isJsonNull() ? "" : returnvalue.get("nama_klinik").getAsString();
                String cliniclogo = returnvalue.get("logo_klinik").isJsonNull() ? "" : returnvalue.get("logo_klinik").getAsString();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userid", Integer.parseInt(userid));
                intent.putExtra("username", username);
                intent.putExtra("role", role);
                intent.putExtra("clinicname", clinicname);
                intent.putExtra("cliniclogo", cliniclogo);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
                phone_input.getText().clear();
                DialogFragment newFragment = new PopUpMessage();
                // Set Message
                ((PopUpMessage) newFragment).setMessage(ErrorMsg);
                newFragment.show(getSupportFragmentManager(), "");
            }
        });

        VolleyLog.DEBUG = true;
    }


    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://apadok.com/api/login";

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
                ErrorMsg = "Ada masalah di aplikasi Apadok";
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    ErrorMsg = "Aplikasi gagal terhubung ke Internet, silahkan coba lagi";
                } else if (error instanceof ServerError || error instanceof AuthFailureError) {
                    ErrorMsg = "Akun tidak dapat ditemukan, silahkan coba lagi";
                } else if (error instanceof ParseError) {
                    ErrorMsg = "Ada masalah di aplikasi Apadok";
                }
                callback.onError();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
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
}