package com.apadok.emrpreventive.consult;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.chatbot.TestChatbotActivity;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.StringToTimeStampFormatting;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class ConsultActivity extends AppCompatActivity {

    // Res/Layout Variables
    private TextView title_consult, tv_subtitle_consult, tv_phone_consult, tv_time_consult;
    private ImageView iv_image_consult;
    private Button btn_whatsapp, btn_call, btn_penjadwalanbeta;

    // Intent Variables
    private PemeriksaanEntity sch;
    private String ClinicName, ClinicLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        setupItemView();
        setupItemData();
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

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        title_consult = (TextView) findViewById(R.id.title_consult);
        tv_subtitle_consult = (TextView) findViewById(R.id.tv_subtitle_consult);
        tv_phone_consult = (TextView) findViewById(R.id.tv_phone_consult);
        tv_time_consult = (TextView) findViewById(R.id.tv_time_consult);
        btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_penjadwalanbeta = (Button) findViewById(R.id.btn_penjadwalanbeta);
        btn_whatsapp.setOnClickListener(openWhatsApp);
        title_consult.setTypeface(helvetica_font);
        tv_subtitle_consult.setTypeface(helvetica_font);
        tv_phone_consult.setTypeface(helvetica_font);
        tv_phone_consult.setText("0822-6000-6070");
        tv_time_consult.setTypeface(helvetica_font);
        btn_call.setTypeface(helvetica_font);
        btn_whatsapp.setTypeface(helvetica_font);
        btn_penjadwalanbeta.setTypeface(helvetica_font);
        btn_call.setOnClickListener(RedirecttoCall);
        btn_penjadwalanbeta.setOnClickListener(RedirectToNearestClinic);
        btn_penjadwalanbeta.setText("Pencarian Klinik");
//      Remove Prototip Penjadwalan as for now
        btn_penjadwalanbeta.setVisibility(View.VISIBLE);

        iv_image_consult = (ImageView) findViewById(R.id.iv_image_consult);
        iv_image_consult.setImageResource(R.drawable.ic_doctor);
        iv_image_consult.setVisibility(View.VISIBLE);
    }

    private void setupItemData() {
        sch = getIntent().getParcelableExtra("data");
    }


    private final View.OnClickListener openWhatsApp = v -> {
        PackageManager packageManager = ConsultActivity.this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String numero = "+62 82260006070";
        String hasil_diabet = sch.getHasil_diabetes() == null ? "" : sch.getHasil_diabetes();
        String hasil_kardio = sch.getHasil_kolesterol() == null ? "" : sch.getHasil_kolesterol();
        String hasil_stroke = sch.getHasil_stroke() == null ? "" : sch.getHasil_stroke();
        String timestamp = sch.getUpdated_at() == null ? sch.getCreated_at() : sch.getUpdated_at();
        String kadar_gula = sch.getKadar_gula() == null ? "" : sch.getKadar_gula();
        String tekanan_darah = sch.getTekanan_darah() == null ? "" : sch.getTekanan_darah();
        String kadar_kolesterol = sch.getKadar_kolesterol() == null ? "" : sch.getKadar_kolesterol();
        // Do some data manipulation here
        String FormattedTimeStamp = StringToTimeStampFormatting.changeFormat(timestamp,"yyyy-MM-dd HH:mm:ss", "dd LLL yyyy HH.mm");
        if (Objects.equals(kadar_gula, "4") || Objects.equals(tekanan_darah, "4") || Objects.equals(kadar_kolesterol, "4")){

        }
        String stroke_warning = "";
        if (Objects.equals(kadar_gula, "4") || Objects.equals(tekanan_darah, "4") || Objects.equals(kadar_kolesterol, "4")) {
            if (kadar_gula.contains("4")) {
                stroke_warning += "\nKadar Gula Darah : Tidak Diketahui";
            }
            if (tekanan_darah.contains("4")) {
                stroke_warning += "\nTekanan Darah : Tidak Diketahui";
            }
            if (kadar_kolesterol.contains("4")) {
                stroke_warning += "\nKadar Kolesterol : Tidak Diketahui";
            }
            hasil_stroke = "Kemungkinan "+ hasil_stroke;
        }
        String mensaje = "Risiko Diabetes : " + hasil_diabet + "\nRisiko Stroke : " + hasil_stroke + stroke_warning + "\nRisiko Kardiovaskular : " + hasil_kardio + "\ndata diperoleh pada " + FormattedTimeStamp;
        Log.e("Print",mensaje);
        String url = null;
        try {
            url = "https://wa.me/" + numero + "?text=" + URLEncoder.encode(mensaje, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i);
        } else {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
            startActivity(viewIntent);
//                KToast.errorToast(StrokeResultActivity.this, getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
        }

    };

    private final View.OnClickListener RedirecttoCall = v -> {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:082260006070"));
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToNearestClinic = v -> {
        Intent intent = new Intent(ConsultActivity.this, NearestClinicActivity.class);
        //Pass the Data to next activity
        intent.putExtra("data", sch);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };

//    private final View.OnClickListener RedirectToConsult = v -> {
//        Intent intent = new Intent(ConsultActivity.this, TestChatbotActivity.class);
//        //Pass the Data to next activity
//        intent.putExtra("data", sch);
//        intent.putExtra("clinicname", ClinicName);
//        intent.putExtra("cliniclogo", ClinicLogo);
//        startActivity(intent);
//    };
}