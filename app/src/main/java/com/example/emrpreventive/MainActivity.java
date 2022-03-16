package com.example.emrpreventive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.example.emrpreventive.common.ConfirmExiting;
import com.example.emrpreventive.common.SetupToolbar;
import com.example.emrpreventive.consult.ConsultActivity;
import com.example.emrpreventive.screening.ConfirmRescreening;
import com.example.emrpreventive.screeninghistory.ScreeningHistory;
import com.example.emrpreventive.screeninghistory.ScreeningHistoryActivity;
import com.example.emrpreventive.screening.ScreeningActivity;
import com.example.emrpreventive.common.VolleyCallBack;
import com.example.emrpreventive.user.ConfrimLogOut;
import com.example.emrpreventive.user.LogOutAuthError;
import com.example.emrpreventive.user.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private Button btn_screening, btn_history_screening, btn_consult;
    private long differenceMinutes;
    private TextView tv_subtitle, tv_greet;
    private List<ScreeningHistory> sch;
    private int UserId;
    private String ErrorMsg,Token,UserName,ClinicName,ClinicLogo;

    @Override
    protected void onRestart() {
        super.onRestart();
        setupJson();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupPreference(); //Ngambil UserID,dll
        setupItemView(); //Setup UI
        setupJson(); //Setup API
    }

    private void SetupPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        UserId = sharedPref.getInt("useridlocal", 0);
        UserName = sharedPref.getString("usernamelocal", "");
        ClinicName = sharedPref.getString("clinicnamelocal", "");
        ClinicLogo = sharedPref.getString("cliniclogolocal", "");
        Token = sharedPref.getString("tokenlocal", "");
        if (UserId == 0) {
            UserId = getIntent().getIntExtra("userid", 0);
            UserName = getIntent().getStringExtra("username");
            ClinicName = getIntent().getStringExtra("clinicname");
            ClinicLogo = getIntent().getStringExtra("cliniclogo");
            Token = getIntent().getStringExtra("token");
            if (UserId == 0) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                finish();
            } else {
                //Name Toast
                CharSequence text = "Hello " + UserName;
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getBaseContext(), text, duration);
                toast.show();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("useridlocal", UserId);
                editor.putString("usernamelocal", UserName);
                editor.putString("clinicnamelocal", ClinicName);
                editor.putString("cliniclogolocal", ClinicLogo);
                editor.putString("tokenlocal", Token);
                editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                editor.apply();
            }
        } else {
            //Name Toast
            CharSequence text = "Hello " + UserName;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getBaseContext(), text, duration);
            toast.show();
        }
    }
    private void setupItemView(){
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(ClinicName);

        // Init Logo RS
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://178.128.25.139:8080/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        ImageView logout = (ImageView) findViewById(R.id.logout_icon);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ConfrimLogOut();
                newFragment.show(getSupportFragmentManager(), "");
            }
        });

        //Button
        btn_screening = (Button) findViewById(R.id.btn_screening);
        btn_history_screening = (Button) findViewById(R.id.btn_history_screening);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_greet = (TextView) findViewById(R.id.tv_greet);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        btn_screening.setTypeface(helvetica_font);
        btn_history_screening.setTypeface(helvetica_font);
        btn_consult.setTypeface(helvetica_font);
        tv_subtitle.setTypeface(helvetica_font);
        tv_greet.setTypeface(helvetica_font);

        btn_screening.setOnClickListener(RedirectToScreening);

        btn_history_screening.setOnClickListener(RedirectToHistory);
        btn_history_screening.setEnabled(false);
        btn_consult.setOnClickListener(RedirectToConsult);
        btn_consult.setEnabled(false);

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
                    differenceMinutes = difference / (1000 * 60);
                } catch (ParseException e) {
                    differenceMinutes = -1;
                }
                long differenceHours = differenceMinutes / (60);
                long differenceDays = differenceMinutes / (24 * 60);
                //Add Clinic Greeting
//                String Greeting = "Selamat Datang " + UserName + "\n\n" + "Di " + ClinicName;
                String Greeting = "Selamat Datang " + UserName;
                tv_greet.setText(Greeting);
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
            }

            @Override
            public void onError() {
                differenceMinutes = -1;
                tv_subtitle.setText(ErrorMsg);
            }
        });
        VolleyLog.DEBUG = true;
    }

    public static Bitmap LoadImageFromWebOperations(String url) {
        try {
            url = "http://178.128.25.139:8080/media/klinik/" + url;
            URL weburl = new URL(url);
            Bitmap bmp = BitmapFactory.decodeStream(weburl.openConnection().getInputStream());
            return bmp;
        } catch (Exception e) {
            Log.e("pic",e.toString());
            return null;
        }
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get Latest ID Pemeriksaan from User 1
        String URL = "http://178.128.25.139:8080/api/pemeriksaan/user/"+UserId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<ScreeningHistory>>() {}.getType();
                sch = gson.fromJson(response, screenhistory);
                // Panggil Fungsi API Lain, Simpen ke SQLite
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                ErrorMsg = ""; // error message, show it in toast or dialog, whatever you want
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    ErrorMsg = "Tidak ada Jaringan Internet";
                    // Panggil Fungsi Baca dari SQLite Local
                } else if (error instanceof ServerError || error instanceof AuthFailureError) {
//                    ErrorMsg = "Server sedang bermasalah";
                    ErrorMsg = "Anda butuh Sign-In kembali\nuntuk menggunakan Apadok";
                    DialogFragment newFragment = new LogOutAuthError();
                    newFragment.show(getSupportFragmentManager(), "");
                }  else if (error instanceof ParseError) {
                    ErrorMsg = "Ada masalah di aplikasi Apadok";
                }
//                else if (error instanceof AuthFailureError) {
//                    DialogFragment newFragment = new LogOutAuthError();
//                    newFragment.show(getSupportFragmentManager(), "");
//                }
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

                headers.put("Authorization", "Bearer " + Token);
                return headers;
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
            ((ConfirmRescreening) newFragment).setToken(Token);
            ((ConfirmRescreening) newFragment).setClinicname(ClinicName);
            ((ConfirmRescreening) newFragment).setCliniclogo(ClinicLogo);
            ((ConfirmRescreening) newFragment).setUsername(UserName);
            newFragment.show(getSupportFragmentManager(), "");
        } else {
            Intent intent = new Intent(MainActivity.this, ScreeningActivity.class);
            //Pass the User ID to next activity
            intent.putExtra("userid", UserId);
            intent.putExtra("clinicname", ClinicName);
            intent.putExtra("cliniclogo", ClinicLogo);
            intent.putExtra("username", UserName);
            intent.putExtra("token", Token);
            startActivity(intent);
        }
    };

    private final View.OnClickListener RedirectToHistory = v -> {
        Intent intent = new Intent(MainActivity.this, ScreeningHistoryActivity.class);
        //Pass the User ID to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(MainActivity.this, ConsultActivity.class);
        //Pass the User ID to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
        if (!sch.isEmpty()) {
            intent.putExtra("data",sch.get(0));
        }
        startActivity(intent);
    };



}