package com.example.emrpreventive.shorting.screeninghistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.emrpreventive.R;
import com.example.emrpreventive.SetupToolbar;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreeningHistoryActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private ArrayList<ScreeningHistory> sch;
    private ListView l;
    private JsonObject returnvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_history);
        setupItemView();
        setupJson();
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);

        l = findViewById(R.id.history_screening);
//        String tutorials[]
//        = { "Algorithms", "Data Structures",
//        "Languages", "Interview Corner",
//        "GATE", "ISRO CS",
//        "UGC NET CS", "CS Subjects",
//        "Web Technologies" };
//        ArrayAdapter<String> arr;
//        arr
//                = new ArrayAdapter<String>(
//                this,
//                R.layout.support_simple_spinner_dropdown_item,
//                tutorials);
//        l.setAdapter(arr);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                String idhistory = (String) view.getTag();
//                int id_history = Integer.parseInt(idhistory);
                Intent intent = new Intent(getBaseContext(), ScreeningHistoryDetailActivity.class);
//                intent.putExtra("history", id_history);
                intent.putExtra("position", position+1);
                intent.putExtra("data",sch.get(position));
                startActivity(intent);
            }
        });
    }

    private void setupJson() {
        //NO API Form Data Yet
        createCalls("",new VolleyCallBack() {

            @Override
            public void onSuccess() {
                ScreeningHistoryAdapter numbersArrayAdapter = new ScreeningHistoryAdapter(getBaseContext(), sch);
                l.setAdapter(numbersArrayAdapter);
            }

            @Override
            public void onError() {

            }
        });
        VolleyLog.DEBUG = true;
    }



    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get ID Pemeriksan From Main Activity
        int id_user = getIntent().getIntExtra("user", 0);
        String URL = "http://178.128.25.139:8080/api/pemeriksaan/userAll/"+id_user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<ScreeningHistory>>() {}.getType();
                //FailSafe
                if (response.charAt(response.length()-1) != ']'){
                    response = response + "]";
                }
                sch = gson.fromJson(response, screenhistory);
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjEzNTY5OTk1MjQsIm5iZiI6MTM1NzAwMDAwMCwiaWRfbG9naW4iOiIyIiwidXNlcm5hbWUiOiJ1c2VyQGdtYWlsLmNvbSJ9.QhtyvpX5N6lgQPZmX7an2vU0zP0W2ir-bZfrbkz08MU");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }
}