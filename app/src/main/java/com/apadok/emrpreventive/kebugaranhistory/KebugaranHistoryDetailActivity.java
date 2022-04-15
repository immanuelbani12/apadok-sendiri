package com.apadok.emrpreventive.kebugaranhistory;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

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
import com.apadok.emrpreventive.common.PopUpMessage;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.database.entity.PemeriksaanKebugaranEntity;
import com.apadok.emrpreventive.encyclopedia.EncyclopediaActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class KebugaranHistoryDetailActivity extends AppCompatActivity {

    // Res/Layout Variables
    private TextView title_result, time_result, kebugaran_result, kebugaran_category;
    private Button btn_education;

    // Intent Variables
    private int kebugaranval;
    private PemeriksaanKebugaranEntity sch;
    private String ClinicName, ClinicLogo, score_kebugaran;

    // Temporary Calculation Variables
    private int[] calc = new int[20];
    private int calculateres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kebugaran_screening_result);
        setupItemView();
        setupItemData();
//        setupJson();
        setupChart();
    }

    private void setupChart(){
        AnyChartView anyChartView = findViewById(R.id.bar_chart);

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
    }

    private void setupItemData() {
        sch = getIntent().getParcelableExtra("data");
        String hasil_kebugaran = sch.gethasil_kebugaran() == null ? "" : sch.gethasil_kebugaran();
        score_kebugaran = sch.getScore_kebugaran() == null ? "" : sch.getScore_kebugaran();
        String timestamp = sch.getUpdated_at() == null ? sch.getCreated_at() : sch.getUpdated_at();

        time_result.setText(timestamp);
        kebugaran_result.setText(score_kebugaran);
        kebugaran_result.setVisibility(View.VISIBLE);
        kebugaran_category.setText(hasil_kebugaran);
        kebugaran_category.setVisibility(View.VISIBLE);

        btn_education.setVisibility(View.VISIBLE);
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
        String url = "http://178.128.25.139:8080/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        title_result = (TextView) findViewById(R.id.title_result);
        time_result = (TextView) findViewById(R.id.time_result);
        kebugaran_result = (TextView) findViewById(R.id.screening_result);
        kebugaran_result.setVisibility(View.GONE);
        kebugaran_category = (TextView) findViewById(R.id.kebugaran_result);
        btn_education = (Button) findViewById(R.id.btn_education);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        title_result.setTypeface(helvetica_font);
        time_result.setTypeface(helvetica_font);
        kebugaran_category.setTypeface(helvetica_font);
        kebugaran_category.setVisibility(View.GONE);
        btn_education.setTypeface(helvetica_font);
        kebugaran_result.setTypeface(helvetica_font);

        title_result.setText("Hasil Skrining Kebugaran");
        btn_education.setVisibility(View.GONE);
        btn_education.setOnClickListener(RedirectToEducation);
    }


    private final View.OnClickListener RedirectToEducation = v -> {
        // Pass value as true
        kebugaranval = 1;

        Intent intent = new Intent(KebugaranHistoryDetailActivity.this, EncyclopediaActivity.class);
        //Pass the Category to next activity
        intent.putExtra("categorykebugaran", kebugaranval);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
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
}
