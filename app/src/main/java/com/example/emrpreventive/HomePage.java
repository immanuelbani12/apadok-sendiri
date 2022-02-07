package com.example.emrpreventive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.emrpreventive.shorting.screeninghistory.ScreeningHistoryActivity;
import com.example.emrpreventive.shorting.stroke.StrokeFormActivity;
import com.example.emrpreventive.shorting.stroke.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomePage extends AppCompatActivity {

    private Gson gson = new Gson();
    private Button btn_screening, btn_history_screening, btn_consult;
    private long differenceMinutes;
    private TextView tv_subtitle;
    private JsonObject returnvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().setTitle("Apadok");
        setupItemView();
        setupJson();
    }

    private void setupItemView(){
        //Button
        btn_screening = (Button) findViewById(R.id.btn_screening);
        btn_history_screening = (Button) findViewById(R.id.btn_history_screening);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/helvetica_neue.ttf");
        btn_screening.setTypeface(typeface);
        btn_history_screening.setTypeface(typeface);
        btn_consult.setTypeface(typeface);
        tv_subtitle.setTypeface(typeface);

        btn_screening.setOnClickListener(RedirectToScreening);

        btn_history_screening.setOnClickListener(RedirectToHistory);
        btn_history_screening.setEnabled(false);
        btn_consult.setOnClickListener(RedirectToConsult);
        btn_consult.setEnabled(false);
    }

    private void setupJson() {
        //NO API Form Data Yet
        createCalls("",new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String Time = returnvalue.get("updated_at").isJsonNull() ? "" : returnvalue.get("updated_at").getAsString();
                try {
                    Date now = Calendar.getInstance().getTime();
                    Date date = formatter.parse(Time);
                    //Comparing dates
                    long difference = Math.abs(now.getTime() - date.getTime());
                    differenceMinutes = difference / (1000);
                } catch (ParseException e) {
                    differenceMinutes = -1;
                    e.printStackTrace();
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
                tv_subtitle.setText("Tidak ada data skrining sebelumnya");
            }
        });
        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get ID Pemeriksan_1
        String URL = "http://192.168.1.194:8080/pemeriksaan/1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
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
        };

        requestQueue.add(stringRequest);
    }

    public static class ConfirmRescreening extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Anda sudah melakukan skrining dalam waktu kurang dari 3 hari,\n\nLakukan kembali skrining?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(getContext(), StrokeFormActivity.class));
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private final View.OnClickListener RedirectToScreening = v -> {
        long differenceDays = differenceMinutes / (24 * 60 * 60);
        if (differenceDays <= 3 && differenceMinutes != -1) {
            DialogFragment newFragment = new ConfirmRescreening();
            newFragment.show(getSupportFragmentManager(), "");
        } else {
            startActivity(new Intent(HomePage.this, StrokeFormActivity.class));
        }

    };

    private final View.OnClickListener RedirectToHistory = v -> {
        startActivity(new Intent(HomePage.this, ScreeningHistoryActivity.class));
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        startActivity(new Intent(HomePage.this, StrokeFormActivity.class));
    };



}