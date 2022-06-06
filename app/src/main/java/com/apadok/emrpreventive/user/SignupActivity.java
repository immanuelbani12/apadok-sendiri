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
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class SignupActivity extends AppApadokActivity {

    // API Variables
    private final Gson gson = new Gson();
    private JsonObject returnvalue;

    // Res/Layout Variables
    private TextView phone_text, additional_text, login_text, btn_signup, institusi_text;
    private EditText name_input, phone_input, group_input, institusi_input;
    private String ErrorMsg;
    private final RegexorChecker regex = new RegexorChecker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupItemView();
    }

    private void setupItemView() {
        //Button
        btn_signup = (Button) findViewById(R.id.btn_signup);
//        tv_support_by = (TextView) findViewById(R.id.support_by);
        phone_text = (TextView) findViewById(R.id.phone_text);
        name_input = (EditText) findViewById(R.id.name_input);
        phone_input = (EditText) findViewById(R.id.phone_input);
        group_input = (EditText) findViewById(R.id.group_input);
        institusi_text = (TextView) findViewById(R.id.institusi_text);
        institusi_input = (EditText) findViewById(R.id.institusi_input);
        additional_text = (TextView) findViewById(R.id.additional_text);
        login_text = (TextView) findViewById(R.id.login);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
//        tv_support_by.setTypeface(helvetica_font);
        btn_signup.setTypeface(helvetica_font);
        btn_signup.setEnabled(false);
        additional_text.setTypeface(helvetica_font);
        login_text.setTypeface(helvetica_font);
        name_input.setTypeface(helvetica_font);
        phone_input.setTypeface(helvetica_font);
        group_input.setTypeface(helvetica_font);
        phone_text.setTypeface(helvetica_font);
        institusi_text.setTypeface(helvetica_font);
        institusi_input.setTypeface(helvetica_font);


//        name_input.addTextChangedListener(new EmptyTextWatcher() {
//
//            @Override
//            public void onEmptyField() {
//                btn_signup.setEnabled(false);
//            }
//
//            @Override
//            public void onFilledField() {
//                if (name_input.getText().toString().length() > 0 && phone_input.getText().toString().length() > 0) {
//                    if (regex.NameRegex(name_input.getText().toString())){
//                        if (regex.PhoneChecker(phone_input.getText().toString())){
//                            btn_signup.setEnabled(true);
//                        } else {
//                            btn_signup.setEnabled(false);
//                            phone_input.setError("Nomor Handphone membutuhkan 9-17 digit");
//                        }
//                    } else {
//                        btn_signup.setEnabled(false);
//                        name_input.setError("Nama tidak valid");
//                    }
//                } else {
//                    btn_signup.setEnabled(false);
//                }
//            }
//        });

        phone_input.addTextChangedListener(new EmptyTextWatcher() {

            @Override
            public void onEmptyField() {
                btn_signup.setEnabled(false);
            }

            @Override
            public void onFilledField() {
                if (group_input.getText().toString().length() > 0 && institusi_input.getText().toString().length() > 0 && phone_input.getText().toString().length() > 0) {
                    if (regex.PhoneChecker(phone_input.getText().toString())){
                        btn_signup.setEnabled(true);
                    } else {
                        btn_signup.setEnabled(false);
                        phone_input.setError("Nomor handphone membutuhkan 9-17 digit");
                    }
                } else {
                    btn_signup.setEnabled(false);
                }
            }
        });

        group_input.addTextChangedListener(new EmptyTextWatcher() {

            @Override
            public void onEmptyField() {
                btn_signup.setEnabled(false);
            }

            @Override
            public void onFilledField() {
                if (group_input.getText().toString().length() > 0 && institusi_input.getText().toString().length() > 0 && phone_input.getText().toString().length() > 0) {
                    if (regex.PhoneChecker(phone_input.getText().toString())){
                        btn_signup.setEnabled(true);
                    } else {
                        btn_signup.setEnabled(false);
                        phone_input.setError("Nomor handphone membutuhkan 7-16 digit");
                    }
                } else {
                    btn_signup.setEnabled(false);
                }
            }
        });

        institusi_input.addTextChangedListener(new EmptyTextWatcher() {

            @Override
            public void onEmptyField() {
                btn_signup.setEnabled(false);
            }

            @Override
            public void onFilledField() {
                if (group_input.getText().toString().length() > 0 && institusi_input.getText().toString().length() > 0 && phone_input.getText().toString().length() > 0) {
                    if (regex.PhoneChecker(phone_input.getText().toString())){
                        btn_signup.setEnabled(true);
                    } else {
                        btn_signup.setEnabled(false);
                        phone_input.setError("Nomor handphone membutuhkan 7-16 digit");
                    }
                } else {
                    btn_signup.setEnabled(false);
                }
            }
        });

//        connect text view to sign up activity
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name_input = (EditText) findViewById(R.id.name_input);
                phone_input = (EditText) findViewById(R.id.phone_input);
                group_input = (EditText) findViewById(R.id.group_input);
                String name = name_input.getText().toString();
                String phone = regex.PhoneChanger(phone_input.getText().toString());
                phone = "62" + phone;
                String group = group_input.getText().toString();
                setupJson(name,phone,group);
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

    private void setupJson(String name, String phonenum, String groupcode) {
        //Construct Obj with Phonenum + Change Obj to string then to JSON
        NewUser Obj = new NewUser(phonenum,groupcode);
        Type user = new TypeToken<NewUser>() {
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
                String idclinic = returnvalue.get("id_institusi").isJsonNull() ? "" : returnvalue.get("id_institusi").getAsString();
                String clinicname = returnvalue.get("nama_institusi").isJsonNull() ? "" : returnvalue.get("nama_institusi").getAsString();
                String cliniclogo = returnvalue.get("logo_institusi").isJsonNull() ? "" : returnvalue.get("logo_institusi").getAsString();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("userid", Integer.parseInt(userid));
                intent.putExtra("username", username);
                intent.putExtra("role", "U");
                intent.putExtra("idclinic", idclinic);
                intent.putExtra("clinicname", clinicname);
                intent.putExtra("cliniclogo", cliniclogo);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
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
        String URL = "http://apadok.com/api/daftar";

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
                //get status code here
                String body;
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                ErrorMsg = "Terdapat kesalahan saat pembuatan akun, silahkan cek kembali data - data yang telah diisikan";
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    ErrorMsg = "Aplikasi gagal terhubung ke Internet, silahkan coba lagi";
                } else if (error instanceof ServerError || error instanceof AuthFailureError) {
                    if(error.networkResponse.data!=null) {
                        try {
                            body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JsonObject errorvalue = gson.fromJson(body, JsonObject.class);
                            JsonObject errordetails = errorvalue.getAsJsonObject("messages");
                            ErrorMsg  = errordetails.get("error").isJsonNull() ? "" : errordetails.get("error").getAsString();
                        } catch (JsonIOException | NullPointerException ex) {
                            ErrorMsg = "Terdapat kesalahan saat pembuatan akun,silahkan hubungi apadokdeveloper@gmail.com";
                        }
                    }
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