package com.example.emrpreventive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.emrpreventive.screening.ScreeningActivity;
import com.example.emrpreventive.shorting.ShortingActivity;
import com.example.emrpreventive.shorting.stroke.StrokeFormActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_screening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("EMR Preventive");
        setupItemView();
    }

    private void setupItemView(){
        //Button
        btn_screening = findViewById(R.id.btn_screening);

        btn_screening.setOnClickListener(RedirectToStroke);

    }

    private final View.OnClickListener RedirectToStroke = v -> {
        startActivity(new Intent(MainActivity.this, StrokeFormActivity.class));
    };

    private final View.OnClickListener RedirectToScreening = v -> {
        startActivity(new Intent(MainActivity.this, ScreeningActivity.class));
    };

    private final View.OnClickListener RedirectToShorting = v -> {
        startActivity(new Intent(MainActivity.this, ShortingActivity.class));
    };


}