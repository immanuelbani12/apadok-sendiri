package com.example.emrpreventive.TestEncyclopedia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.emrpreventive.R;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EncyclopediaActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private ArrayList<Encyclopedia> ecl = new ArrayList<Encyclopedia>();
    private ListView l;
    private JsonObject returnvalue;
    private ArrayList<Encyclopedia> eclnew;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_history);
        CreateFormList();
        eclnew = FilterEncyclopedia();
        l = findViewById(R.id.history_screening);
        EncyclopediaAdapter numbersArrayAdapter = new EncyclopediaAdapter(getBaseContext(), eclnew);
        l.setAdapter(numbersArrayAdapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String idhistory = (String) view.getTag();
                int id_history = Integer.parseInt(idhistory);
                Intent intent = new Intent(getBaseContext(), EncyclopediaDetailActivity.class);
                intent.putExtra("history", id_history);
                intent.putExtra("position", position+1);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Encyclopedia> FilterEncyclopedia() {
        ArrayList<Encyclopedia> eclparsed = new ArrayList<Encyclopedia>();
        String filtered = "1";
        for (int counter = 0; counter < ecl.size(); counter++) {
            if(ecl.get(counter).kategori_artikel == filtered){
                eclparsed.add(ecl.get(counter));
            }
        }
        return eclparsed;
    }

//    private void setupJson() {
//        //NO API Form Data Yet
//        createCalls("",new VolleyCallBack() {
//
//            @Override
//
//
//            @Override
//            public void onError() {
//
//            }
//        });
//        VolleyLog.DEBUG = true;
//    }



    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get ID Pemeriksan From Main Activity
        int id_user = getIntent().getIntExtra("user", 0);
        String URL = "http://178.128.25.139:8080/pemeriksaan/userAll/"+id_user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<Encyclopedia>>() {}.getType();
                //FailSafe
                if (response.charAt(response.length()-1) != ']'){
                    response = response + "]";
                }
                ecl = gson.fromJson(response, screenhistory);
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
        };

        requestQueue.add(stringRequest);
    }

    private final void CreateFormList(){
        ecl.add(new Encyclopedia("1","Judul Artikel Struk","Ini isi artikel 1","1","",""));
        ecl.add(new Encyclopedia("2","Judul Artikel Diabetes","Ini isi artikel 2","2","",""));
        ecl.add(new Encyclopedia("3","Judul Artikel Kardiovaskular","Ini isi artikel 3","3","",""));
        ecl.add(new Encyclopedia("4","Judul Artikel Struk 2","Ini isi artikel 4","1","",""));
        ecl.add(new Encyclopedia("5","Judul Artikel Diabetes 2","Ini isi artikel 5","2","",""));
        ecl.add(new Encyclopedia("6","Judul Artikel Kardiovaskular 2","Ini isi artikel 6","3","",""));
    }
}