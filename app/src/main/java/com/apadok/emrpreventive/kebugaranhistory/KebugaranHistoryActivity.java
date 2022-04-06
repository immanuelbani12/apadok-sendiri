package com.apadok.emrpreventive.kebugaranhistory;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.PopUpMessage;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KebugaranHistoryActivity extends AppCompatActivity {

    // Gson related
    // API return variables
    private Gson gson = new Gson();
    private ArrayList<PemeriksaanEntity> sch;
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_history);
        setupItemView();
//        setupContent();
//        setupJson();
    }

//    private void setupContent() {
//        sch = getIntent().getParcelableArrayListExtra("history");
//        ScreeningHistoryAdapter numbersArrayAdapter = new ScreeningHistoryAdapter(getBaseContext(), sch);
//        l.setAdapter(numbersArrayAdapter);
//    }

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
        String url = "http://178.128.25.139:8080/media/klinik/" + logo;
        Picasso.get().load(url).into(cliniclogo);

        TextView tv_title = (TextView) findViewById(R.id.tv_title_histories);
        tv_title.setText("Riwayat Skrining Kebugaran");

        DialogFragment newFragment = new PopUpMessage();
        // Set Message
        ((PopUpMessage) newFragment).setMessage("Fitur masih dalam tahap pengembangan");
        newFragment.show(getSupportFragmentManager(), "");
    }

//    private void setupJson() {
//        //NO API Form Data Yet
//        createCalls("",new VolleyCallBack() {
//
//            @Override
//            public void onSuccess() {
//                ScreeningHistoryAdapter numbersArrayAdapter = new ScreeningHistoryAdapter(getBaseContext(), sch);
//                l.setAdapter(numbersArrayAdapter);
//            }
//
//            @Override
//            public void onError() {
//                setupContent();
//            }
//        });
//        VolleyLog.DEBUG = true;
//    }

//    private void createCalls(String json, final VolleyCallBack callback) {
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        //Temporarily Get ID Pemeriksan From Main Activity
//        int id_user = getIntent().getIntExtra("userid", 0);
//        String token = getIntent().getStringExtra("token");
//        String URL = "http://178.128.25.139:8080/api/pemeriksaan/userAll/"+id_user;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("VOLLEY", response);
//                Type screenhistory = new TypeToken<List<PemeriksaanEntity>>() {}.getType();
//                //FailSafe
//                if (response.charAt(response.length()-1) != ']'){
//                    response = response + "]";
//                }
//                sch = gson.fromJson(response, screenhistory);
//                callback.onSuccess();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VOLLEY", error.toString());
//                callback.onError();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return json == null ? null : json.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
//                    return null;
//                }
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                // Basic Authentication
//                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP)
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//
//        requestQueue.add(stringRequest);
//    }
}