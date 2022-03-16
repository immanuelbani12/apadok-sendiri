package com.example.emrpreventive.consult;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.emrpreventive.R;
import com.example.emrpreventive.chatbot.TestChatbotActivity;
import com.example.emrpreventive.common.SetupToolbar;
import com.example.emrpreventive.screeninghistory.ScreeningHistory;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConsultActivity extends AppCompatActivity {

    // Res/Layout Variables
    private TextView title_consult, tv_subtitle_consult, tv_phone_consult, tv_time_consult;
    private ImageView iv_image_consult;
    private Button btn_whatsapp, btn_call, btn_penjadwalanbeta;

    // Intent Variables
    private ScreeningHistory sch;
    private String ClinicName,ClinicLogo;

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
        String url = "http://178.128.25.139:8080/media/klinik/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
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
        tv_time_consult.setTypeface(helvetica_font);
        btn_call.setTypeface(helvetica_font);
        btn_whatsapp.setTypeface(helvetica_font);
        btn_penjadwalanbeta.setTypeface(helvetica_font);
//        Disable call temporarily, bad layout
//        btn_call.setOnClickListener(RedirecttoCall);
        btn_call.setVisibility(View.GONE);
        btn_penjadwalanbeta.setOnClickListener(RedirectToConsult);

        iv_image_consult = (ImageView) findViewById(R.id.iv_image_consult);
        iv_image_consult.setImageResource(R.drawable.ic_doctor);
        iv_image_consult.setVisibility(View.VISIBLE);
    }

    private void setupItemData() {
        sch = getIntent().getParcelableExtra("data");
    }



    private View.OnClickListener openWhatsApp = v ->{
        PackageManager packageManager = ConsultActivity.this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String numero = "+62 8123456789";
        String hasil_diabet = sch.getHasil_diabetes() == null ? "" : sch.getHasil_diabetes();
        String hasil_kardio = sch.getHasil_kolesterol() == null ? "" : sch.getHasil_kolesterol();
        String hasil_stroke = sch.getHasil_stroke() == null ? "" : sch.getHasil_stroke();
        String timestamp = sch.getUpdated_at() == null ? sch.getCreated_at() : sch.getUpdated_at();
        String mensaje = "Risiko Diabetes : " + hasil_diabet + "\nRisiko Stroke : " + hasil_stroke + "\nRisiko Kardiovaskular : " + hasil_kardio + "\ndata diperoleh pada " + timestamp;
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i);
        }else {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
            startActivity(viewIntent);
//                KToast.errorToast(StrokeResultActivity.this, getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
        }

    };

    private final View.OnClickListener RedirecttoCall = v -> {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:08123456789"));
        startActivity(intent);
    };

    private final View.OnClickListener RedirectToConsult = v -> {
        Intent intent = new Intent(ConsultActivity.this, TestChatbotActivity.class);
        //Pass the Data to next activity
        intent.putExtra("data",sch);
        intent.putExtra("clinicname", ClinicName);
        intent.putExtra("cliniclogo", ClinicLogo);
        startActivity(intent);
    };
}