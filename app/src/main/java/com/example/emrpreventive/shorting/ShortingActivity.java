package com.example.emrpreventive.shorting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.emrpreventive.R;
import com.example.emrpreventive.shorting.stroke.StrokeFormActivity;

public class ShortingActivity extends AppCompatActivity {

    private Button btn_stroke;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorting);
        getSupportActionBar().setTitle("EMR Preventive");
        setupItemView();
    }

    private void setupItemView(){
        //Button
        btn_stroke = findViewById(R.id.btn_stroke);

        btn_stroke.setOnClickListener(RedirectToStroke);

    }

    private final View.OnClickListener RedirectToStroke = v -> {
        startActivity(new Intent(ShortingActivity.this, StrokeFormActivity.class));
    };

    private final View.OnClickListener RedirectToShorting = v -> {
        startActivity(new Intent(ShortingActivity.this, ShortingActivity.class));
    };
}