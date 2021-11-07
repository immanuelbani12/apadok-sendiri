package com.example.emrpreventive.shorting.stroke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.emrpreventive.MainActivity;
import com.example.emrpreventive.R;
import com.example.emrpreventive.shorting.ShortingActivity;

public class StrokeResultActivity extends AppCompatActivity {

    private TextView tv_score;
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_result);
        setupItemView();
    }

    private void setupItemView() {
        int Score;
        tv_score = (TextView) findViewById(R.id.tv_score);
        btn_finish = (Button) findViewById(R.id.btn_finish);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            Score = bundle.getInt("Score");
            if(Score >= 15) {
                tv_score.setText("Berresiko Tinggi");
            }
            else if (Score >= 10) {
                tv_score.setText("Berresiko Sedang");
            }
            else {
                tv_score.setText("Berresiko Rendah");
            }
        }
        btn_finish.setOnClickListener(RedirectToFinish);

    }

    private final View.OnClickListener RedirectToFinish = v -> {
        startActivity(new Intent(StrokeResultActivity.this, MainActivity.class));
    };
}
