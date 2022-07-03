package com.apadok.emrpreventive.consult;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.StringToTimeStampFormatting;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.user.ConfirmLogOut;
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
    private String ClinicName, ClinicLogo, ClinicPhoneWhatsapp, ClinicPhone;

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
        String url = "http://apadok.com/media/institusi/" + ClinicLogo;
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
        //Get Latest Pemeriksaan from Previous Activity
        sch = getIntent().getParcelableExtra("data");
        //Get Phone Number from Previous Activity
        String clinicphone = getIntent().getStringExtra("clinicphone");

        //Phone Number Formattter
        String phone_formatted;
        //IF starts with 021 disable whatsapp and no need to reformat
        if (clinicphone.startsWith("021")){
            ClinicPhone = clinicphone;
            btn_whatsapp.setEnabled(false);
            phone_formatted = ClinicPhone.substring(0,3) + "-" + ClinicPhone.substring(3,7) + "-" + ClinicPhone.substring(7);
        }
        //IF starts with 0, reformat to +62 strictly for Whatsapp
        else if (clinicphone.charAt(0) == '0'){
            ClinicPhone = clinicphone;
            ClinicPhoneWhatsapp = "+62" + clinicphone.substring(1);
            phone_formatted = ClinicPhone.substring(0,4) + "-" + ClinicPhone.substring(4,8) + "-" + ClinicPhone.substring(8);
        //IF starts with 62, reformat all
        } else {
            ClinicPhone = "0" + clinicphone.substring(2);
            ClinicPhoneWhatsapp = "+" + clinicphone.substring(0,2) + clinicphone.substring(2);
            phone_formatted = ClinicPhone.substring(0,4) + "-" + ClinicPhone.substring(4,8) + "-" + ClinicPhone.substring(8);
        }

        //Update displayed Phone Number on Consult Activity
        tv_phone_consult.setText(phone_formatted);
    }


    private final View.OnClickListener openWhatsApp = v -> {
        PackageManager packageManager = ConsultActivity.this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String numero = ClinicPhoneWhatsapp;
        String hasil_diabet = sch.getHasil_diabetes() == null ? "" : sch.getHasil_diabetes();
        String hasil_kardio = sch.getHasil_kardiovaskular() == null ? "" : sch.getHasil_kardiovaskular();
        String hasil_stroke = sch.getHasil_stroke() == null ? "" : sch.getHasil_stroke();
        String timestamp = sch.getUpdated_at() == null ? sch.getCreated_at() : sch.getUpdated_at();
        String kadar_gula = sch.getKadar_gula() == null ? "" : sch.getKadar_gula();
        String tekanan_darah = sch.getTekanan_darah() == null ? "" : sch.getTekanan_darah();
        String kadar_kolesterol = sch.getKadar_kolesterol() == null ? "" : sch.getKadar_kolesterol();
        // Do some data manipulation here
        String FormattedTimeStamp = StringToTimeStampFormatting.changeFormat(timestamp,"yyyy-MM-dd HH:mm:ss", "dd MMMM yyyy HH.mm");
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
        //Open Phone Application and Pass Admin Klinik Phone Number
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ClinicPhone));
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