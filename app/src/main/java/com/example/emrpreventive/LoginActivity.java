package com.example.emrpreventive;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
                setupJson(count);

                //Removable Code Starts from here
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

    private void setupJson(String phonenum) {
        //Construct Obj with Phonenum + Change Obj to string then to JSON
        //Type answerstype = new TypeToken<List<FormAnswer>>() {}.getType();
        //String json = gson.toJson(Obj, ObjClassType);
        //Log.e("bobo", json);

        // Send JSON ke API & Parse Respons di createcall
        // Parse JSON Respons di createcall
        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
//        createCalls(json,new VolleyCallBack() {
//
//            @Override
//            public void onSuccess() {
//                // here you have the response from the volley.
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//
//        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://178.128.25.139:8080/api/pemeriksaan";

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