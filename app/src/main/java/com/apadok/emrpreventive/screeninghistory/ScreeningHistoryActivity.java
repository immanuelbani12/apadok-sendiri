package com.apadok.emrpreventive.screeninghistory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreeningHistoryActivity extends AppApadokActivity {

    // Gson related
    // API return variables
    private final Gson gson = new Gson();
    private ArrayList<PemeriksaanEntity> sch;
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
        sch = getIntent().getParcelableArrayListExtra("history");
        ScreeningHistoryAdapter numbersArrayAdapter = new ScreeningHistoryAdapter(getBaseContext(), sch);
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
        String url = "http://apadok.com/media/institusi/" + logo;
        Picasso.get().load(url).into(cliniclogo);

        String clinicphone = getIntent().getStringExtra("clinicphone");

        l = findViewById(R.id.history_screening);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                Get Current ID of HistoryList (Not Position)
//                String idhistory = (String) view.getTag();
//                int id_history = Integer.parseInt(idhistory);
//                intent.putExtra("history", id_history);
                Intent intent = new Intent(getBaseContext(), ScreeningHistoryDetailActivity.class);
                intent.putExtra("position", position + 1);
                intent.putExtra("data", sch.get(position));
                intent.putExtra("clinicname", clinicname);
                intent.putExtra("cliniclogo", logo);
                intent.putExtra("clinicphone", clinicphone);
                String Role = getIntent().getStringExtra("role");
                intent.putExtra("role", Role);
                String token = getIntent().getStringExtra("token");
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }

    private void setupJson() {
        //NO API Form Data Yet
        createCalls(new VolleyCallBack() {

            @Override
            public void onSuccess() {
                ScreeningHistoryAdapter numbersArrayAdapter = new ScreeningHistoryAdapter(getBaseContext(), sch);
                l.setAdapter(numbersArrayAdapter);
            }

            @Override
            public void onError() {
                setupContent();
            }
        });
        VolleyLog.DEBUG = true;
    }

    private void createCalls(final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get ID Pemeriksan From Main Activity
        int id_user = getIntent().getIntExtra("userid", 0);
        String token = getIntent().getStringExtra("token");
        String URL = "http://apadok.com/api/pemeriksaan/userAll/" + id_user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<PemeriksaanEntity>>() {
                }.getType();
                //FailSafe
                if (response.charAt(response.length() - 1) != ']') {
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
            public byte[] getBody() {
                return "".getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP)
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        requestQueue.add(stringRequest);
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.option_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                DialogFragment newFragment = new ConfirmLogOut();
                newFragment.show(getSupportFragmentManager(), "");
            default:
                return false;
        }
    }

}