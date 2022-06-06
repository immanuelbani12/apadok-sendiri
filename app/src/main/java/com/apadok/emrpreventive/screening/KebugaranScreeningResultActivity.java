package com.apadok.emrpreventive.screening;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.StringToTimeStampFormatting;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.encyclopedia.EncyclopediaActivity;
import com.apadok.emrpreventive.user.LogOutAuthError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KebugaranScreeningResultActivity extends AppCompatActivity {

    // Gson related
    // API return variables
    private final Gson gson = new Gson();
    private JsonObject returnvalue;
    private PemeriksaanEntity sch;
    private String hasil = "";
    // API input Variables
    private final FormAnswer[] answer = new FormAnswer[17];
    private List<FormAnswer> answers;

    // Res/Layout Variables
    private AnyChartView anyChartView;
    private TextView title_result, time_result, kebugaran_result, kebugaran_category;
    private Button btn_education;
    private ImageView side_pic;

    // Intent Variables
    private int kebugaranval;
    private String ClinicName, ClinicLogo, score_kebugaran;

    // Temporary Calculation Variables
//    private int[] calc = new int[20];
//    private int calculateres;

    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale(this, getResources().getConfiguration());
        setContentView(R.layout.activity_kebugaran_screening_result);
        setupItemView();
        setupJson();
//        setupChart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        APIlib apilib = new APIlib();
        apilib.setActiveAnyChartView(null);
    }


    private void setupChart(){
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("", Integer.parseInt(score_kebugaran)));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.yScale().maximum(52);


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yAxis(0).title("Skor");
        anyChartView.setChart(cartesian);

        String sourceString = "Skor kebugaran anda <b>" + score_kebugaran + "</b> dari maksimal skor <b>52</b>.<br> Semakin tinggi skor kebugaran menunjukkan bahwa tubuh anda juga semakin bugar, sebaliknya jika semakin rendah skor kebugaran maka menunjukkan tubuh anda kurang bugar";
        kebugaran_category.setText(Html.fromHtml(sourceString));
        anyChartView.setVisibility(View.VISIBLE);
    }

    private void setupItemView() {
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        ClinicName = getIntent().getStringExtra("clinicname");
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(ClinicName);
        // Init Logo RS
        ClinicLogo = getIntent().getStringExtra("cliniclogo");
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://apadok.com/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        kebugaran_result = (TextView) findViewById(R.id.screening_result);
        kebugaran_result.setVisibility(View.GONE);
        kebugaran_category = (TextView) findViewById(R.id.kebugaran_result);
        btn_education = (Button) findViewById(R.id.btn_education);
        side_pic = (ImageView) findViewById(R.id.side_pic);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            kebugaran_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }


        side_pic.setImageResource(R.drawable.graph_side_pic);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        title_result.setTypeface(helvetica_font);
        time_result.setTypeface(helvetica_font);
        kebugaran_category.setTypeface(helvetica_font);
        kebugaran_category.setVisibility(View.GONE);
        btn_education.setTypeface(helvetica_font);
        kebugaran_result.setTypeface(helvetica_font);

        title_result.setText("Hasil Skrining Kebugaran");
        time_result.setText("Mengolah Data....");
        anyChartView = findViewById(R.id.bar_chart);
        anyChartView.setVisibility(View.GONE);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);
    }

    private void setupJson() {

//        Date currentTime = Calendar.getInstance().getTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        time_result.setText(formatter.format(currentTime));

        answers = getIntent().getParcelableArrayListExtra("Answers");
//        for (int i=0; i<13; i++){
//            answer[i] = answers.get(i);
//            calc[i] = Integer.parseInt(answer[i].getAnswer());
//        }
//
//        calculateres = (4 - calc[0]) + (4 - calc[1])
//                + (4 - calc[2]) + (4 - calc[3]) + (4 - calc[4])
//                + (4 - calc[5]) + (0 + calc[6]) + (0 + calc[7])
//                + (4 - calc[8]) + (4 - calc[9]) + (4 - calc[10])
//                + (4 - calc[11]) + (4 - calc[12]);
//
//        kebugaran_result.setText(String.valueOf(calculateres));
//
//        if (calculateres < 13){
//            kebugaran_category.setText("Skor Kebugaran anda termasuk Tidak Bugar");
//        }
//        else if (calculateres >= 13 && calculateres < 26){
//            kebugaran_category.setText("Skor Kebugaran anda termasuk Bugar Rendah");
//        }
//        else if (calculateres >= 26 && calculateres < 39){
//            kebugaran_category.setText("Skor Kebugaran anda termasuk Bugar Menengah");
//        }
//        else if (calculateres >= 39){
//            kebugaran_category.setText("Skor Kebugaran anda termasuk Bugar");
//        }

        //Ubah Answers ke string trus ke JSON
        Type answerstype = new TypeToken<List<FormAnswer>>() {
        }.getType();
        String json = gson.toJson(answers, answerstype);

        // Send JSON ke API & Parse Respons di createcall
        // Parse JSON Respons di createcall
        // Lakukan sesuatu di OnSuccess after Respons diubah jadi variabel siap pakai
        createCalls(json, new VolleyCallBack() {

            @Override
            public void onSuccess() {
                // here you have the response from the volley.
                String hasil_kebugaran = returnvalue.get("hasil_kebugaran").isJsonNull() ? "" : returnvalue.get("hasil_kebugaran").getAsString();
                score_kebugaran = returnvalue.get("score_kebugaran").isJsonNull() ? "" : returnvalue.get("score_kebugaran").getAsString();
                String timestamp = returnvalue.get("updated_at").isJsonNull() ? returnvalue.get("created_at").getAsString() : returnvalue.get("updated_at").getAsString();

                time_result.setText(StringToTimeStampFormatting.changeFormat(timestamp,"yyyy-MM-dd HH:mm:ss", "dd LLL yyyy HH.mm"));
                kebugaran_result.setText(score_kebugaran);
                kebugaran_result.setVisibility(View.VISIBLE);
                kebugaran_category.setText("Mengolah Grafik Data...");
                kebugaran_category.setVisibility(View.VISIBLE);

                btn_education.setVisibility(View.VISIBLE);
                setupChart();
            }

            @Override
            public void onError() {
                time_result.setText(hasil);
            }
        });

        VolleyLog.DEBUG = true;
    }

    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://apadok.com/api/pemeriksaanKebugaran";
        String token = getIntent().getStringExtra("token");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                returnvalue = gson.fromJson(response, JsonObject.class);
                sch = gson.fromJson(response, PemeriksaanEntity.class);
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    hasil = "Aplikasi gagal terhubung ke Internet";

                } else if (error instanceof ServerError) {
                    hasil = "Server Apadok sedang bermasalah";
                } else if (error instanceof AuthFailureError) {
                    hasil = "Anda butuh Sign-In kembali\nuntuk menggunakan Apadok";
                    DialogFragment newFragment = new LogOutAuthError();
                    newFragment.show(getSupportFragmentManager(), "");
                } else if (error instanceof ParseError) {
                    hasil = "Ada masalah di aplikasi Apadok";
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

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(stringRequest).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    private final View.OnClickListener RedirectToEducation = v -> {
        // Pass value as true
        kebugaranval = 1;
        Intent intent = new Intent(KebugaranScreeningResultActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorykebugaran", kebugaranval);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };


}
