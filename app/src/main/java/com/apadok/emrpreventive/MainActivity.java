package com.apadok.emrpreventive;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;


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
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.StringToTimeStampFormatting;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.apadok.emrpreventive.consult.ConsultActivity;
import com.apadok.emrpreventive.consult.NearestClinicActivity;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.database.entity.PemeriksaanKebugaranEntity;
import com.apadok.emrpreventive.kebugaranhistory.KebugaranHistoryActivity;
import com.apadok.emrpreventive.screening.ConfirmRescreening;
import com.apadok.emrpreventive.screening.KebugaranScreeningActivity;
import com.apadok.emrpreventive.screening.ScreeningActivity;
import com.apadok.emrpreventive.screeninghistory.ScreeningHistoryActivity;
import com.apadok.emrpreventive.socketchat.MessageListActivity;
import com.apadok.emrpreventive.socketchat.SocketChatActivity;
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.apadok.emrpreventive.user.LogOutAuthError;
import com.apadok.emrpreventive.user.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppApadokActivity {

    // Gson related
    // API return variables
    private final Gson gson = new Gson();
    private ArrayList<PemeriksaanEntity> sch;
    private ArrayList<PemeriksaanKebugaranEntity> sch_bugar;
    private long differenceMinutes;
    private String ErrorMsg;
    private Boolean IsLatestScreenBugar;

    private Button btn_history_screening;
    private Button btn_consult;
    private TextView tv_subtitle, tv_greet, tv_time;

    // Intent Variables
    private int UserId;
    private int LoginUserId;
    private int LoginClinicId;
    private String Token, UserName, ClinicName, ClinicLogo, ClinicPhone;
//    private String Role;

    @Override
    protected void onRestart() {
        super.onRestart();
        setupJson(); //Setup API
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupPreferenceAndSnackbar(); //Manage User Properties and Display Snackbar
        setupItemView();
    }

    private void SetupPreferenceAndSnackbar() {
        // Prepare SharedPrefs based on Android Version
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try {
                KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
                String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
                sharedPref = EncryptedSharedPreferences.create(
                        getString(R.string.preference_file_key),
                        mainKeyAlias,
                        getApplicationContext(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }

        // Get App Version
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Store or Receive the Data from SharedPref
        UserId = sharedPref.getInt("user_id", 0);
        UserName = sharedPref.getString("user_name", "");
//        Role = sharedPref.getString("user_role", "");
        ClinicName = sharedPref.getString("clinic_name", "");
        ClinicLogo = sharedPref.getString("clinic_logo", "");
        ClinicPhone = sharedPref.getString("clinic_phone", "");
        LoginUserId = sharedPref.getInt("user_login_id",0);
        LoginClinicId = sharedPref.getInt("clinic_login_id",0);
        int clinicId = sharedPref.getInt("clinic_id", 0);
        Token = sharedPref.getString("user_token", "");
        if (UserId == 0) {
            UserId = getIntent().getIntExtra("userid", 0);
            UserName = getIntent().getStringExtra("username");
//            Role = getIntent().getStringExtra("role");
            ClinicName = getIntent().getStringExtra("clinicname");
            ClinicLogo = getIntent().getStringExtra("cliniclogo");
            ClinicPhone = getIntent().getStringExtra("clinicphone");
            LoginUserId = getIntent().getIntExtra("loginuserid", 0);
            LoginClinicId = getIntent().getIntExtra("loginclinicid", 0);
            clinicId = getIntent().getIntExtra("clinicid", 0);
            Token = getIntent().getStringExtra("token");
            if (UserId == 0) {
                // Change this code if cant login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //finish();
            } else {
                // Snackbar First time Login
                CharSequence text = "Anda berhasil masuk sebagai anggota " + ClinicName;
//                if (Role.equals("N")) {
//                    text = "Anda berhasil masuk sebagai non-anggota klinik";
//                }
                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(getBaseContext(),R.color.orange_dark));
                snackbar.show();

                // Setup API
                setupJson();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("user_id", UserId);
                editor.putString("user_name", UserName);
//                editor.putString("user_role", Role);
                editor.putString("clinic_name", ClinicName);
                editor.putString("clinic_logo", ClinicLogo);
                editor.putString("clinic_phone", ClinicPhone);
                editor.putString("user_token", Token);
                editor.putInt("user_login_id", LoginUserId);
                editor.putInt("clinic_login_id", LoginClinicId);
                editor.putInt("clinic_id", clinicId);
                editor.putInt("app_version", versionCode);
//                editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                editor.apply();
            }
        } else {

            // Check App Version
            if (sharedPref.getInt("app_version", 0) != versionCode) {
                DialogFragment newFragment = new LogOutAuthError();
                newFragment.show(getSupportFragmentManager(), "");
                newFragment.setCancelable(false);
                return;
            }

            // Snackbar if Logged In Already
            CharSequence text = "Selamat datang kembali anggota " + ClinicName;
            if (ClinicName.contains("Apadok")){
                text = "Selamat datang kembali non-anggota klinik";
            }
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(ContextCompat.getColor(getBaseContext(),R.color.orange_dark));
            snackbar.show();

            // Setup API
            setupJson();
        }
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(ClinicName);

        // Init Logo RS
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://apadok.com/media/institusi/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        // Handler for Option Button
        ImageView option = (ImageView) findViewById(R.id.option_icon);
        option.setVisibility(View.VISIBLE);

        //Button
        // Res/Layout Variables
        Button btn_screening = (Button) findViewById(R.id.btn_screening);
        btn_history_screening = (Button) findViewById(R.id.btn_history_screening);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_greet = (TextView) findViewById(R.id.tv_greet);
        tv_time = (TextView) findViewById(R.id.tv_time);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
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

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM y", Locale.ENGLISH);
        String str_time = formatter.format(date);
        String str_time_id = StringToTimeStampFormatting.changeFormatEngToIdNoConvert(str_time,"EEEE, dd MMMM y", "EEEE, dd MMMM y");
        tv_time.setText(str_time_id);
//        tv_time.setText(str_time);
        tv_time.setVisibility(View.VISIBLE);
        // Replace Button if Non-Member (Moved inside Consultation as for now)
//        if (Role != null) {
//            if (Role.equals("N")) {
//                btn_consult.setText("Pencarian Klinik");
//                btn_consult.setOnClickListener(RedirectToNearestClinic);
//            }
//        }

        // Custom Handler for Back Button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                DialogFragment newFragment = new ConfirmExiting();
                //Pass User Properties to next activity
                ((ConfirmExiting) newFragment).setMessage("Anda ingin keluar dari aplikasi?");
                newFragment.show(getSupportFragmentManager(), "");
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // API Handler for 1st API Calls
    private void setupJson() {
        //NO API Form Data Yet(No Need)
        createCalls(new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // Calls 2nd API
                setup2ndJson();
            }

            @Override
            public void onError() {
                differenceMinutes = -1;
                tv_subtitle.setText(ErrorMsg);
            }
        });
        VolleyLog.DEBUG = true;
    }

    // API Handler for 2nd API Calls
    private void setup2ndJson() {
        create2ndCalls(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                // Handling Response from 1st and 2nd API Calls
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String Time;
                if (sch.isEmpty() && sch_bugar.isEmpty()) {
                    Time = "";
                } else {
                    if (sch.isEmpty()) {
                        IsLatestScreenBugar = true;
                        if (sch_bugar.get(0).getUpdated_at() == null) {
                            Time = sch_bugar.get(0).getCreated_at();
                        } else {
                            Time = sch_bugar.get(0).getUpdated_at();
                        }
                    } else {
                        IsLatestScreenBugar = false;
                        if (sch.get(0).getUpdated_at() == null) {
                            Time = sch.get(0).getCreated_at();
                        } else {
                            Time = sch.get(0).getUpdated_at();
                        }
                    }
                }
                try {
                    Date now = Calendar.getInstance().getTime();
                    Date date = formatter.parse(Time);
                    //Comparing dates
                    long difference = Math.abs(now.getTime() - Objects.requireNonNull(date).getTime());
                    differenceMinutes = difference / (1000 * 60);
                } catch (ParseException e) {
                    differenceMinutes = -1;
                }
                long differenceHours = differenceMinutes / (60);
                long differenceDays = differenceMinutes / (24 * 60);
                //Add Clinic Greeting
//                String Greeting = "Selamat Datang " + UserName + "\n\n" + "Di " + ClinicName;
                String Greeting = "Selamat datang, " + UserName;
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
    }

    // Volley API for Getting History Pemeriksaan
    private void createCalls(final VolleyCallBack callback) {
        String json = null;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://apadok.com/api/pemeriksaan/userAll/" + UserId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<PemeriksaanEntity>>() {
                }.getType();
                sch = gson.fromJson(response, screenhistory);
                // Save to SQLite Here
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                ErrorMsg = ""; // error message, show it in toast or dialog, whatever you want
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    ErrorMsg = "Aplikasi gagal terhubung ke Internet";
                    // Panggil Fungsi Baca dari SQLite Local
                } else if (error instanceof ServerError) {
                    ErrorMsg = "Server Apadok sedang bermasalah";
                } else if (error instanceof AuthFailureError) {
                    ErrorMsg = "Anda butuh Sign-In kembali\nuntuk menggunakan Apadok";
                    DialogFragment newFragment = new LogOutAuthError();
                    newFragment.show(getSupportFragmentManager(), "");
                    newFragment.setCancelable(false);
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

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + Token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    // Volley API for Getting History Pemeriksaan Kebugaran
    private void create2ndCalls(final VolleyCallBack callback) {
        String json = null;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get Latest ID Pemeriksaan from User 1
        String URL = "http://apadok.com/api/pemeriksaanKebugaran/userAll/" + UserId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenbugarhistory = new TypeToken<List<PemeriksaanKebugaranEntity>>() {
                }.getType();
                sch_bugar = gson.fromJson(response, screenbugarhistory);
                // Save to SQLite Here
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                ErrorMsg = ""; // error message, show it in toast or dialog, whatever you want
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    ErrorMsg = "Aplikasi gagal terhubung ke Internet";
                    // Panggil Fungsi Baca dari SQLite Local
                } else if (error instanceof ServerError) {
                    ErrorMsg = "Server Apadok sedang bermasalah";
                } else if (error instanceof AuthFailureError) {
                    ErrorMsg = "Anda butuh Sign-In kembali\nuntuk menggunakan Apadok";
                    DialogFragment newFragment = new LogOutAuthError();
                    newFragment.show(getSupportFragmentManager(), "");
                    newFragment.setCancelable(false);
                } else if (error instanceof ParseError) {
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
            public byte[] getBody() {
                return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + Token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    // Handler for Screening Button
    private final View.OnClickListener RedirectToScreening = v -> {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.screening_option_dialog);
        TextView tv_risiko = (TextView) dialog.findViewById(R.id.text_screening_risiko);
        TextView tv_kebugaran = (TextView) dialog.findViewById(R.id.text_screening_kebugaran);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_risiko.setTypeface(helvetica_font);
        tv_kebugaran.setTypeface(helvetica_font);
        CardView cv_risiko = (CardView) dialog.findViewById(R.id.cv_pemeriksaan);
        CardView cv_kebugaran = (CardView) dialog.findViewById(R.id.cv_pemeriksaan_kebugaran);

        // CardView for Pemeriksaan
        cv_risiko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long differenceDays = differenceMinutes / (24 * 60);
                if (differenceDays <= 3 && differenceMinutes != -1 && !IsLatestScreenBugar) {
                    DialogFragment newFragment = new ConfirmRescreening();
                    //Pass User Properties to next activity
                    ((ConfirmRescreening) newFragment).setUser_id(UserId);
                    ((ConfirmRescreening) newFragment).setToken(Token);
                    ((ConfirmRescreening) newFragment).setClinicname(ClinicName);
                    ((ConfirmRescreening) newFragment).setCliniclogo(ClinicLogo);
                    ((ConfirmRescreening) newFragment).setClinicphone(ClinicPhone);
                    ((ConfirmRescreening) newFragment).setUsername(UserName);
//                    ((ConfirmRescreening) newFragment).setRole(Role);
                    ((ConfirmRescreening) newFragment).setIskebugaran(false);
                    newFragment.show(getSupportFragmentManager(), "");
                } else {
                    Intent intent = new Intent(MainActivity.this, ScreeningActivity.class);
                    //Pass User Properties to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("clinicphone", ClinicPhone);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
//                    intent.putExtra("role", Role);
                    startActivity(intent);
                }
            }
        });

        // CardView for Pemeriksaan Kebugaran
        cv_kebugaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long differenceDays = differenceMinutes / (24 * 60);
                if (differenceDays <= 3 && differenceMinutes != -1 && IsLatestScreenBugar) {
                    DialogFragment newFragment = new ConfirmRescreening();
                    //Pass User Properties to next activity
                    ((ConfirmRescreening) newFragment).setUser_id(UserId);
                    ((ConfirmRescreening) newFragment).setToken(Token);
                    ((ConfirmRescreening) newFragment).setClinicname(ClinicName);
                    ((ConfirmRescreening) newFragment).setCliniclogo(ClinicLogo);
                    ((ConfirmRescreening) newFragment).setClinicphone(ClinicPhone);
                    ((ConfirmRescreening) newFragment).setUsername(UserName);
//                    ((ConfirmRescreening) newFragment).setRole(Role);
                    ((ConfirmRescreening) newFragment).setIskebugaran(true);
                    newFragment.show(getSupportFragmentManager(), "");
                } else {
                    Intent intent = new Intent(MainActivity.this, KebugaranScreeningActivity.class);
                    //Pass User Properties to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("clinicphone", ClinicPhone);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
//                    intent.putExtra("role", Role);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    };

    // Handler for History Button
    private final View.OnClickListener RedirectToHistory = v -> {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.history_option_dialog);
        TextView tv_risiko = (TextView) dialog.findViewById(R.id.text_riwayat_risiko);
        TextView tv_kebugaran = (TextView) dialog.findViewById(R.id.text_riwayat_kebugaran);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_risiko.setTypeface(helvetica_font);
        tv_kebugaran.setTypeface(helvetica_font);
        CardView cv_disease_screening_history = (CardView) dialog.findViewById(R.id.cv_disease_screening_history);
        CardView cv_fitness_screening_history = (CardView) dialog.findViewById(R.id.cv_fitness_screening_history);

        // CardView for History Pemeriksaan
        cv_disease_screening_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sch.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, ScreeningHistoryActivity.class);
                    //Pass User Properties to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("clinicphone", ClinicPhone);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
//                    intent.putExtra("role", Role);
                    intent.putParcelableArrayListExtra("history", sch);
                    startActivity(intent);
                } else {
                    //Error Toast
                    CharSequence text = "Anda tidak memiliki riwayat skrining untuk kategori ini";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getBaseContext(), text, duration);
                    toast.show();
                }
            }
        });

        // CardView for History Pemeriksaan Kebugaran
        cv_fitness_screening_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sch_bugar.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, KebugaranHistoryActivity.class);
                    //Pass User Properties to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("clinicphone", ClinicPhone);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
//                    intent.putExtra("role", Role);
                    intent.putParcelableArrayListExtra("history_bugar", sch_bugar);
                    startActivity(intent);
                } else {
                    //Error Toast
                    CharSequence text = "Anda tidak memiliki riwayat skrining untuk kategori ini";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getBaseContext(), text, duration);
                    toast.show();
                }
            }
        });
        dialog.show();
    };

    // Handler for Consult Button
    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(MainActivity.this, ConsultActivity.class);
        //Pass User Properties to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("clinicphone", ClinicPhone);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
//        intent.putExtra("role", Role);
        //Pass Latest History Pemeriksaan to next activity
        if (!sch.isEmpty()) {
            intent.putExtra("data", sch.get(0));
        }
        startActivity(intent);
    };

    // Handler for Nearest Clinic Button (Moved inside Consultation as for now)
    private final View.OnClickListener RedirectToNearestClinic = v -> {
        Intent intent = new Intent(MainActivity.this, NearestClinicActivity.class);
        //Pass User Properties to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
//        intent.putExtra("role", Role);
        startActivity(intent);
    };

    // Handler for Chat Button (Work In Progress)
    private final View.OnClickListener RedirectToSocketChat = v -> {
        Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
        //Pass User Properties to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
//        intent.putExtra("role", Role);
        intent.putExtra("loginid", LoginUserId);
        intent.putExtra("logininstitusiid",LoginClinicId);
        startActivity(intent);
    };

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.option_main_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                DialogFragment newFragment = new ConfirmLogOut();
                newFragment.show(getSupportFragmentManager(), "");
                return true;
            case R.id.chat:
                Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
                intent.putExtra("userid", UserId);
                intent.putExtra("clinicname", ClinicName);
                intent.putExtra("cliniclogo", ClinicLogo);
                intent.putExtra("username", UserName);
                intent.putExtra("token", Token);
                intent.putExtra("loginid", LoginUserId);
                intent.putExtra("logininstitusiid",LoginClinicId);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void onUpdateFirstRun () {

    }
}