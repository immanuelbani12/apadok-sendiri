package com.example.emrpreventive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.emrpreventive.TestEncyclopedia.Encyclopedia;
import com.example.emrpreventive.TestEncyclopedia.EncyclopediaActivity;
import com.example.emrpreventive.shorting.TestLogin;
import com.example.emrpreventive.shorting.screeninghistory.ScreeningHistory;
import com.example.emrpreventive.shorting.screeninghistory.ScreeningHistoryActivity;
import com.example.emrpreventive.shorting.stroke.ScreeningActivity;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private Button btn_screening, btn_history_screening, btn_consult;
    private long differenceMinutes;
    private TextView tv_subtitle;
    private List<ScreeningHistory> sch;
    private int UserId;
    private String ErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setTitle("Apadok");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupPreference();
        setupItemView();
        setupJson();
    }

    private void SetupPreference() {
        SharedPreferences sharedPref = this.getPreferences(getBaseContext().MODE_PRIVATE);
//        Deleted when done
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.apply();
//
        UserId = sharedPref.getInt("userlocal", 0);
        if (UserId == 0) {
            UserId = getIntent().getIntExtra("user", 0);
            if (UserId == 0) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("userlocal", UserId);
                editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                editor.apply();
            }
        }
    }
    private void setupItemView(){
        //Button
        btn_screening = (Button) findViewById(R.id.btn_screening);
        btn_history_screening = (Button) findViewById(R.id.btn_history_screening);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        btn_screening.setTypeface(helvetica_font);
        btn_history_screening.setTypeface(helvetica_font);
        btn_consult.setTypeface(helvetica_font);
        tv_subtitle.setTypeface(helvetica_font);

        btn_screening.setOnClickListener(RedirectToScreening);

        btn_history_screening.setOnClickListener(RedirectToHistory);
        btn_history_screening.setEnabled(false);
        btn_consult.setOnClickListener(RedirectToConsult);
        btn_consult.setEnabled(false);
    }

    private void setupJson() {
        //NO API Form Data Yet(No Need)
        createCalls("",new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String Time;
                if (sch.isEmpty()) {
                    Time = "";
                } else {
                    if (sch.get(0).getUpdated_at() == null) {
                        Time = sch.get(0).getCreated_at();
                    } else {
                        Time = sch.get(0).getUpdated_at();
                    }
                }
                try {
                    Date now = Calendar.getInstance().getTime();
                    Date date = formatter.parse(Time);
                    //Comparing dates
                    long difference = Math.abs(now.getTime() - date.getTime());
                    differenceMinutes = difference / (1000);
                } catch (ParseException e) {
                    differenceMinutes = -1;
                }
                long differenceHours = differenceMinutes / (60 * 60);
                long differenceDays = differenceMinutes / (24 * 60 * 60);

                if (differenceMinutes == -1) {
                    tv_subtitle.setText("Tidak ada data skrining sebelumnya");
                } else {
                    btn_history_screening.setEnabled(true);
                    btn_consult.setEnabled(true);
                    if (differenceDays != 0) {
                        //Convert long to String
                        String dayDifference = Long.toString(differenceDays);
                        tv_subtitle.setText("Terakhir diperbarui " + dayDifference + " hari yang lalu.");
                    } else if (differenceHours != 0) {
                        String dayDifference = Long.toString(differenceHours);
                        tv_subtitle.setText("Terakhir diperbarui " + dayDifference + " jam yang lalu.");
                    } else {
                        String dayDifference = Long.toString(differenceMinutes);
                        tv_subtitle.setText("Terakhir diperbarui " + dayDifference + " menit yang lalu.");
                    }
                }
//                tv_subtitle.setText("Terakhir diperbarui " + Time + "\n" + dayDifference + " hari yang lalu.");
            }

            @Override
            public void onError() {
                differenceMinutes = -1;
                tv_subtitle.setText(ErrorMsg);
            }
        });
        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get Latest ID Pemeriksaan from User 1
        String URL = "http://178.128.25.139:8080/pemeriksaan/user/"+UserId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<ScreeningHistory>>() {}.getType();
                sch = gson.fromJson(response, screenhistory);
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                ErrorMsg = ""; // error message, show it in toast or dialog, whatever you want
                if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    ErrorMsg = "Tidak ada Jaringan Internet";
                } else if (error instanceof ServerError) {
                    ErrorMsg = "Server sedang bermasalah";
                }  else if (error instanceof ParseError) {
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

    private final View.OnClickListener RedirectToScreening = v -> {
        long differenceDays = differenceMinutes / (24 * 60 * 60);
        if (differenceDays <= 3 && differenceMinutes != -1) {
            DialogFragment newFragment = new ConfirmRescreening();
            //Pass the User ID to next activity
            ((ConfirmRescreening) newFragment).setUser_id(UserId);
            newFragment.show(getSupportFragmentManager(), "");
        } else {
            Intent intent = new Intent(MainActivity.this, ScreeningActivity.class);
            //Pass the User ID to next activity
            intent.putExtra("user", UserId);
            startActivity(intent);
        }
    };

    private final View.OnClickListener RedirectToHistory = v -> {
        Intent intent = new Intent(MainActivity.this, ScreeningHistoryActivity.class);
        //Pass the User ID to next activity
        intent.putExtra("user", UserId);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        startActivity(new Intent(MainActivity.this, EncyclopediaActivity.class));
    };



}