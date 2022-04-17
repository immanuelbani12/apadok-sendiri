package com.apadok.emrpreventive.kebugaranhistory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.apadok.emrpreventive.database.entity.PemeriksaanKebugaranEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KebugaranHistoryActivity extends AppCompatActivity {

    // Gson related
    // API return variables
    private Gson gson = new Gson();
    private ArrayList<PemeriksaanKebugaranEntity> sch_bugar;
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_history);
        setupItemView();
//        setupContent();
        setupJson();
    }

    private void setupContent() {
        sch_bugar = getIntent().getParcelableArrayListExtra("history_bugar");
        KebugaranHistoryAdapter numbersArrayAdapter = new KebugaranHistoryAdapter(getBaseContext(), sch_bugar);
        l.setAdapter(numbersArrayAdapter);
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        String clinicname = getIntent().getStringExtra("clinicname");
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(clinicname);
        // Init Logo RS
        String logo = getIntent().getStringExtra("cliniclogo");
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://apadok.com/media/klinik/" + logo;
        Picasso.get().load(url).into(cliniclogo);

        TextView tv_title = (TextView) findViewById(R.id.tv_title_histories);
        tv_title.setText("Riwayat Skrining Kebugaran");

        l = findViewById(R.id.history_screening);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                Get Current ID of HistoryList (Not Position)
//                String idhistory = (String) view.getTag();
//                int id_history = Integer.parseInt(idhistory);
//                intent.putExtra("history", id_history);
                Intent intent = new Intent(getBaseContext(), KebugaranHistoryDetailActivity.class);
                intent.putExtra("position", position + 1);
                intent.putExtra("data", sch_bugar.get(position));
                intent.putExtra("clinicname", clinicname);
                intent.putExtra("cliniclogo", logo);
                startActivity(intent);
            }
        });
    }

    private void setupJson() {
        //NO API Form Data Yet
        createCalls("", new VolleyCallBack() {

            @Override
            public void onSuccess() {
                KebugaranHistoryAdapter numbersArrayAdapter = new KebugaranHistoryAdapter(getBaseContext(), sch_bugar);
                l.setAdapter(numbersArrayAdapter);
            }

            @Override
            public void onError() {
                //Name Toast
                setupContent();
            }
        });
        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get ID Pemeriksan From Main Activity
        int id_user = getIntent().getIntExtra("userid", 0);
        String token = getIntent().getStringExtra("token");
        String URL = "http://apadok.com/api/pemeriksaanKebugaran/userAll/" + id_user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<PemeriksaanKebugaranEntity>>() {
                }.getType();
                //FailSafe
//                if (response.charAt(response.length()-1) != ']'){
//                    response = response + "]";
//                }
                sch_bugar = gson.fromJson(response, screenhistory);
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
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP)
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }
}