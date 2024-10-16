package com.apadok.emrpreventive.kebugaranhistory;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

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
import com.apadok.emrpreventive.database.entity.PemeriksaanKebugaranEntity;
import com.apadok.emrpreventive.encyclopedia.EncyclopediaActivity;
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class KebugaranHistoryDetailActivity extends AppCompatActivity {

    // Res/Layout Variables
    private AnyChartView chart_result;
    private TextView tv_title, tv_time_result, tv_score_title, tv_score_result, tv_description_result;
    private Button btn_education;

    // Intent Variables
    private int kebugaranval;
    private PemeriksaanKebugaranEntity sch;
    private String ClinicName, ClinicLogo, score_kebugaran;
    private ImageView side_pic;

    // Temporary Calculation Variables
//    private final int[] calc = new int[20];
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
        setupItemData();
        setupChart();
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
        side_pic.setVisibility(View.VISIBLE);
        chart_result.setChart(cartesian);
        chart_result.setVisibility(View.VISIBLE);

        String sourceString = "Skor kebugaran anda <b>" + score_kebugaran + "</b> dari maksimal skor <b>52</b>.<br> Semakin tinggi skor kebugaran menunjukkan bahwa tubuh anda juga semakin bugar, sebaliknya jika semakin rendah skor kebugaran maka menunjukkan tubuh anda kurang bugar";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_description_result.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_description_result.setText(Html.fromHtml(sourceString));
        }
        btn_education.setVisibility(View.VISIBLE);
    }

    private void setupItemData() {
        sch = getIntent().getParcelableExtra("data");
        score_kebugaran = sch.getScore_kebugaran() == null ? "" : sch.getScore_kebugaran();
        String timestamp = sch.getUpdated_at() == null ? sch.getCreated_at() : sch.getUpdated_at();

        tv_time_result.setText(StringToTimeStampFormatting.changeFormat(timestamp,"yyyy-MM-dd HH:mm:ss", "dd MMMM yyyy HH.mm"));
        tv_score_result.setText(score_kebugaran);
        tv_score_result.setVisibility(View.VISIBLE);
        tv_description_result.setText("Mengolah Grafik Data...");
        tv_description_result.setVisibility(View.VISIBLE);

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
        String url = "http://apadok.com/media/institusi/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time_result = (TextView) findViewById(R.id.tv_time_result);
        tv_score_title = (TextView) findViewById(R.id.tv_score_title);
        tv_score_result = (TextView) findViewById(R.id.tv_score_result);
        tv_description_result = (TextView) findViewById(R.id.tv_description_result);
        btn_education = (Button) findViewById(R.id.btn_education);
        side_pic = (ImageView) findViewById(R.id.side_pic);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tv_description_result.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        side_pic.setImageResource(R.drawable.graph_side_pic);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_title.setTypeface(helvetica_font);
        tv_time_result.setTypeface(helvetica_font);
        tv_score_title.setTypeface(helvetica_font);
        tv_score_result.setTypeface(helvetica_font);
        tv_description_result.setTypeface(helvetica_font);
        btn_education.setTypeface(helvetica_font);

        int position = getIntent().getIntExtra("position", 0);
        tv_title.setText("Riwayat Skrining Kebugaran " + position);
        tv_score_result.setVisibility(View.GONE);
        side_pic.setVisibility(View.GONE);
        tv_description_result.setVisibility(View.GONE);
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);
        chart_result= findViewById(R.id.chart_result);
        chart_result.setVisibility(View.GONE);
    }


    private final View.OnClickListener RedirectToEducation = v -> {
        // Pass value as true
        kebugaranval = 1;

        Intent intent = new Intent(KebugaranHistoryDetailActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorykebugaran", kebugaranval);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        String token = getIntent().getStringExtra("token");
        intent.putExtra("token", token);
        startActivity(intent);
    };

//    private final View.OnClickListener RedirectToConsult = v -> {
//        Intent intent = new Intent(ScreeningResultActivity.this, ConsultActivity.class);
//        //Pass the Category to next activity (Unused)
//        intent.putExtra("categorydiabetes", diabetval);
//        intent.putExtra("categorystroke", strokeval);
//        intent.putExtra("categorykardio", cardioval);
//
//        intent.putExtra("data", sch);
//        intent.putExtra("clinicname", ClinicName);
//        intent.putExtra("cliniclogo", ClinicLogo);
//        startActivity(intent);
//    };
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
