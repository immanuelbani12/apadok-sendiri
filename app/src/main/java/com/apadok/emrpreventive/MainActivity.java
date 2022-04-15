package com.apadok.emrpreventive;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.cardview.widget.CardView;
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
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.SetupToolbar;
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
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.apadok.emrpreventive.user.LogOutAuthError;
import com.apadok.emrpreventive.user.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Gson related
    // API return variables
    private Gson gson = new Gson();
    private ArrayList<PemeriksaanEntity> sch;
    private ArrayList<PemeriksaanKebugaranEntity> sch_bugar;
    private long differenceMinutes;
    private String ErrorMsg;
    private Boolean IsLatestScreenBugar;

    // Res/Layout Variables
    private Button btn_screening, btn_history_screening, btn_consult;
    private TextView tv_subtitle, tv_greet, tv_risiko, tv_kebugaran;

    // Intent Variables
    private int UserId;
    private String Token, UserName, Role, ClinicName, ClinicLogo;

    @Override
    protected void onRestart() {
        super.onRestart();
        setupJson();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupPreference(); //Get UserID,etc
        setupItemView(); //Setup UI
//        setupJson(); //Setup API
    }

    private void SetupPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        UserId = sharedPref.getInt("useridlocal", 0);
        UserName = sharedPref.getString("usernamelocal", "");
        Role = sharedPref.getString("rolelocal", "");
        ClinicName = sharedPref.getString("clinicnamelocal", "");
        ClinicLogo = sharedPref.getString("cliniclogolocal", "");
        Token = sharedPref.getString("tokenlocal", "");
        if (UserId == 0) {
            UserId = getIntent().getIntExtra("userid", 0);
            UserName = getIntent().getStringExtra("username");
            Role = getIntent().getStringExtra("role");
            ClinicName = getIntent().getStringExtra("clinicname");
            ClinicLogo = getIntent().getStringExtra("cliniclogo");
            Token = getIntent().getStringExtra("token");
            if (UserId == 0) {
//                Change this code if cant login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                finish();
            } else {
                //Name Toast
                CharSequence text = "Anda berhasil masuk sebagai anggota " + ClinicName;
                if (Role.equals("N")) {
                    text = "Anda berhasil masuk sebagai non-anggota klinik";
                }

                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(R.style.AppTheme);
                snackbar.show();

                setupJson();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("useridlocal", UserId);
                editor.putString("usernamelocal", UserName);
                editor.putString("rolelocal", Role);
                editor.putString("clinicnamelocal", ClinicName);
                editor.putString("cliniclogolocal", ClinicLogo);
                editor.putString("tokenlocal", Token);
                editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                editor.apply();
            }
        } else {
//            Jika User Ada dan Sudah Login sebelumnya
            CharSequence text = "Selamat datang kembali anggota " + ClinicName;
            if (ClinicName.contains("Apadok")){
                text = "Selamat datang kembali non-anggota klinik";
            }
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(getBaseContext(),R.color.orange_dark));
            snackbar.show();
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
        String url = "http://178.128.25.139:8080/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        ImageView logout = (ImageView) findViewById(R.id.logout_icon);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ConfirmLogOut();
                newFragment.show(getSupportFragmentManager(), "");
            }
        });

        //Button
        btn_screening = (Button) findViewById(R.id.btn_screening);
        btn_history_screening = (Button) findViewById(R.id.btn_history_screening);
        btn_consult = (Button) findViewById(R.id.btn_consult);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_greet = (TextView) findViewById(R.id.tv_greet);

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

        if (Role != null) {
            if (Role.equals("N")) {
                btn_consult.setText("Pencarian Klinik");
                btn_consult.setOnClickListener(RedirectToNearestClinic);
            }
        }

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
        createCalls(new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
                setup2ndJson();
            }

            @Override
            public void onError() {
                // do nothing
            }
        });
        VolleyLog.DEBUG = true;
    }

    private void setup2ndJson() {
        create2ndCalls(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                // here you have the response from the volley.
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
        // Temporary Disables this until next update
        // Call 2nd API
    }

    private void createCalls(final VolleyCallBack callback) {
        String json = null;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get Latest ID Pemeriksaan from User 1
        String URL = "http://178.128.25.139:8080/api/pemeriksaan/userAll/" + UserId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<PemeriksaanEntity>>() {
                }.getType();
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

    private void create2ndCalls(final VolleyCallBack callback) {
        String json = null;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get Latest ID Pemeriksaan from User 1
        String URL = "http://178.128.25.139:8080/api/pemeriksaanKebugaran/userAll/" + UserId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenbugarhistory = new TypeToken<List<PemeriksaanKebugaranEntity>>() {
                }.getType();
                sch_bugar = gson.fromJson(response, screenbugarhistory);
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
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.screening_option_dialog);
        TextView tv_risiko = (TextView) dialog.findViewById(R.id.text_screening_risiko);
        TextView tv_kebugaran = (TextView) dialog.findViewById(R.id.text_screening_kebugaran);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_risiko.setTypeface(helvetica_font);
        tv_kebugaran.setTypeface(helvetica_font);
        CardView cv_risiko = (CardView) dialog.findViewById(R.id.cv_first);
        CardView cv_kebugaran = (CardView) dialog.findViewById(R.id.cv_second);

        cv_risiko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long differenceDays = differenceMinutes / (24 * 60);
                if (differenceDays <= 3 && differenceMinutes != -1 && !IsLatestScreenBugar) {
                    DialogFragment newFragment = new ConfirmRescreening();
                    //Pass the User ID to next activity
                    ((ConfirmRescreening) newFragment).setUser_id(UserId);
                    ((ConfirmRescreening) newFragment).setToken(Token);
                    ((ConfirmRescreening) newFragment).setClinicname(ClinicName);
                    ((ConfirmRescreening) newFragment).setCliniclogo(ClinicLogo);
                    ((ConfirmRescreening) newFragment).setUsername(UserName);
                    ((ConfirmRescreening) newFragment).setIskebugaran(false);
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
            }
        });

        // add on click cv kebugaran
        cv_kebugaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long differenceDays = differenceMinutes / (24 * 60);
                if (differenceDays <= 3 && differenceMinutes != -1 && IsLatestScreenBugar) {
                    DialogFragment newFragment = new ConfirmRescreening();
                    //Pass the User ID to next activity
                    ((ConfirmRescreening) newFragment).setUser_id(UserId);
                    ((ConfirmRescreening) newFragment).setToken(Token);
                    ((ConfirmRescreening) newFragment).setClinicname(ClinicName);
                    ((ConfirmRescreening) newFragment).setCliniclogo(ClinicLogo);
                    ((ConfirmRescreening) newFragment).setUsername(UserName);
                    ((ConfirmRescreening) newFragment).setIskebugaran(true);
                    newFragment.show(getSupportFragmentManager(), "");
                } else {
                    Intent intent = new Intent(MainActivity.this, KebugaranScreeningActivity.class);
                    //Pass the User ID to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    };

    private final View.OnClickListener RedirectToHistory = v -> {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.history_option_dialog);
        TextView tv_risiko = (TextView) dialog.findViewById(R.id.text_riwayat_risiko);
        TextView tv_kebugaran = (TextView) dialog.findViewById(R.id.text_riwayat_kebugaran);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_risiko.setTypeface(helvetica_font);
        tv_kebugaran.setTypeface(helvetica_font);
        CardView cv_history_first = (CardView) dialog.findViewById(R.id.cv_history_first);
        CardView cv_history_second = (CardView) dialog.findViewById(R.id.cv_history_second);

        cv_history_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sch.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, ScreeningHistoryActivity.class);
                    //Pass the User ID to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
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

        // add on click cv kebugaran
        cv_history_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sch_bugar.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, KebugaranHistoryActivity.class);
                    //Pass the User ID to next activity
                    intent.putExtra("userid", UserId);
                    intent.putExtra("clinicname", ClinicName);
                    intent.putExtra("cliniclogo", ClinicLogo);
                    intent.putExtra("username", UserName);
                    intent.putExtra("token", Token);
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

//    private final View.OnClickListener RedirectToHistory = v -> {
//        Intent intent = new Intent(MainActivity.this, ScreeningHistoryActivity.class);
//        //Pass the User ID to next activity
//        intent.putExtra("userid", UserId);
//        intent.putExtra("clinicname", ClinicName);
//        intent.putExtra("cliniclogo", ClinicLogo);
//        intent.putExtra("username", UserName);
//        intent.putExtra("token", Token);
//        if (!sch.isEmpty()) {
//            intent.putParcelableArrayListExtra("history",sch);
//        }
//        startActivity(intent);
//    };

    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(MainActivity.this, ConsultActivity.class);
        //Pass the User ID to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
        if (!sch.isEmpty()) {
            intent.putExtra("data", sch.get(0));
        }
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToNearestClinic = v -> {
        Intent intent = new Intent(MainActivity.this, NearestClinicActivity.class);
        //Pass the User ID to next activity
        intent.putExtra("userid", UserId);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        intent.putExtra("username", UserName);
        intent.putExtra("token", Token);
        startActivity(intent);
    };

}