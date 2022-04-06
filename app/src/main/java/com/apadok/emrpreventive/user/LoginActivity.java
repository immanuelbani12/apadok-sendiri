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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apadok.emrpreventive.MainActivity;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.EmptyTextWatcher;
import com.apadok.emrpreventive.common.PopUpMessage;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class LoginActivity extends AppCompatActivity {

    // API Variables
    private Gson gson = new Gson();
    private JsonObject returnvalue;
    private String login_res = "";

    // Res/Layout Variables
    private Button btn_masuk;
    private TextView tv_support_by, phone_text;
    private EditText phone_input;

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

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
//        tv_support_by.setTypeface(helvetica_font);
        btn_masuk.setTypeface(helvetica_font);
        btn_masuk.setEnabled(false);
        phone_input.setTypeface(helvetica_font);
        phone_text.setTypeface(helvetica_font);

        phone_input.addTextChangedListener(new EmptyTextWatcher() {

            @Override
            public void onEmptyField() {
                btn_masuk.setEnabled(false);
            }

            @Override
            public void onFilledField() {
                btn_masuk.setEnabled(true);
            }
        });

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
                String token = returnvalue.get("token").isJsonNull() ? "" : returnvalue.get("token").getAsString();
                String clinicname = returnvalue.get("nama_klinik").isJsonNull() ? "" : returnvalue.get("nama_klinik").getAsString();
                String cliniclogo = returnvalue.get("logo_klinik").isJsonNull() ? "" : returnvalue.get("logo_klinik").getAsString();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userid", Integer.parseInt(userid));
                intent.putExtra("username", username);
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
                ((PopUpMessage) newFragment).setMessage("Akun tidak dapat ditemukan, silahkan coba lagi");
                newFragment.show(getSupportFragmentManager(), "");
            }
        });

        VolleyLog.DEBUG = true;
    }


    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://178.128.25.139:8080/api/login";

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
}