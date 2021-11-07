package com.example.emrpreventive.shorting.stroke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.emrpreventive.MainActivity;
import com.example.emrpreventive.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StrokeResultActivity extends AppCompatActivity {

    private TextView tv_score;
    private Button btn_finish, btn_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_result);
        setupItemView();
    }

    private void setupItemView() {
        int ScoreHigh,ScoreMed,ScoreLow;
        tv_score = (TextView) findViewById(R.id.tv_score);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            ScoreHigh = bundle.getInt("ScoreHigh");
            ScoreMed = bundle.getInt("ScoreMed");
            ScoreLow = bundle.getInt("ScoreLow");
            if(ScoreHigh >= 3) {
                tv_score.setText("Berresiko Tinggi");
            }
            else if (3 > ScoreMed && ScoreMed > 7) {
                tv_score.setText("Berresiko Sedang");
            }
            else {
                tv_score.setText("Berresiko Rendah");
            }
        }
        btn_finish.setOnClickListener(RedirectToFinish);
        btn_whatsapp.setOnClickListener(openWhatsApp);

    }

    private View.OnClickListener openWhatsApp = v ->{
        PackageManager packageManager = StrokeResultActivity.this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String numero = "+62 81282352027";
        String mensaje = "Text";
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

    private final View.OnClickListener RedirectToFinish = v -> {
        startActivity(new Intent(StrokeResultActivity.this, MainActivity.class));
    };
}
