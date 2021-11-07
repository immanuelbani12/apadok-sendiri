package com.example.emrpreventive.shorting.stroke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.emrpreventive.R;
import com.example.emrpreventive.shorting.ShortingActivity;

import java.util.ArrayList;

public class StrokeFormActivity extends AppCompatActivity implements View.OnClickListener {

    private Form[] forms = new Form[9];
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;
    private int ScoreCardScore = 0;
    private TextView tv_option_one,tv_option_two, tv_option_three, tv_option_four, tv_progress, tv_question;
    private Button btn_submit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_form);
        CreateFormList();
        SetForm();
    }


    private void SetForm() {
        Form FormQ = forms[CurrentForm-1];

        tv_option_one = (TextView) findViewById(R.id.tv_option_one);
        tv_option_two = (TextView) findViewById(R.id.tv_option_two);
        tv_option_three = (TextView) findViewById(R.id.tv_option_three);
        tv_option_four = (TextView) findViewById(R.id.tv_option_four);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_question = (TextView) findViewById(R.id.tv_question);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        progressBar.setProgress(CurrentForm);
        tv_progress.setText(CurrentForm + "/" + progressBar.getMax());


        tv_question.setText(FormQ.getQuestion());

        defaultOptionsView();

        if (CurrentForm == 9) {
            btn_submit.setText("Selesai");
        } else {
            btn_submit.setText("Submit");
        }

        CheckSet(FormQ);
        btn_submit.setOnClickListener(this);
//        tv_option_one.setText(FormQ.getOpt1());
//        tv_option_two.setText(FormQ.getOpt2());
//        tv_option_three.setText(FormQ.getOpt3());
//        tv_option_four.setText(FormQ.getOpt4());
//        tv_option_one.setOnClickListener(this);
//        tv_option_two.setOnClickListener(this);
//        tv_option_three.setOnClickListener(this);
//        tv_option_four.setOnClickListener(this);

    }

    private void CheckSet(Form formcheck){
        if(formcheck.getOpt1() != ""){
            tv_option_one.setText(formcheck.getOpt1());
            tv_option_one.setOnClickListener(this);
            tv_option_one.setVisibility(View.VISIBLE);
        } else {
            tv_option_one.setVisibility(View.GONE);
        }
        if(formcheck.getOpt2() != ""){
            tv_option_two.setText(formcheck.getOpt2());
            tv_option_two.setOnClickListener(this);
            tv_option_two.setVisibility(View.VISIBLE);
        } else {
            tv_option_two.setVisibility(View.GONE);
        }
        if(formcheck.getOpt3() != ""){
            tv_option_three.setText(formcheck.getOpt3());
            tv_option_three.setOnClickListener(this);
            tv_option_three.setVisibility(View.VISIBLE);
        } else {
            tv_option_three.setVisibility(View.GONE);
        }
        if(formcheck.getOpt4() != ""){
            tv_option_four.setText(formcheck.getOpt4());
            tv_option_four.setOnClickListener(this);
            tv_option_four.setVisibility(View.VISIBLE);
        } else {
            tv_option_four.setVisibility(View.GONE);
        }
    }

    private void defaultOptionsView() {

        ArrayList<TextView> options = new ArrayList<TextView>();
        options.add(0,tv_option_one);
        options.add(1, tv_option_two);
        options.add(2, tv_option_three);
        options.add(3, tv_option_four);

        for (TextView option : options){
            option.setTextColor(Color.parseColor("#7A8089"));
            option.setTypeface(Typeface.DEFAULT);
            option.setBackground(ContextCompat.getDrawable(
                    StrokeFormActivity.this,
                    R.drawable.default_option_border_bg
            ));
        }

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tv_option_one:
                selectedOptionView(tv_option_one, 3);
                break;
            case R.id.tv_option_two:
                selectedOptionView(tv_option_two, 2);
                break;
            case R.id.tv_option_three:
                selectedOptionView(tv_option_three, 1);
                break;
            case R.id.tv_option_four:
                selectedOptionView(tv_option_four, 3);
                break;
            case R.id.btn_submit:
                if (SelectedOptionPosititon == 0) {

                    CurrentForm++;

                    if(CurrentForm <= 9){
                        SetForm();
                    } else {
                        Intent intent = new Intent(StrokeFormActivity.this, StrokeResultActivity.class);
                        intent.putExtra("Score", ScoreCardScore);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    if (CurrentForm == 9) {
                        btn_submit.setText("Lihat Hasil");
                    } else {
                        btn_submit.setText("Pertanyaan Selanjutnya");
                    }
                    ScoreCardScore += SelectedOptionPosititon;
                    SelectedOptionPosititon = 0;
                }
                break;
        }
    }

    private void selectedOptionView(TextView tv, int selectedOptionNum) {

        defaultOptionsView();

        SelectedOptionPosititon = selectedOptionNum;

        tv.setTextColor(
                Color.parseColor("#363A43")
        );
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setBackground(ContextCompat.getDrawable(
                StrokeFormActivity.this,
                R.drawable.selected_option_border_bg
                ));
    }

    private void CreateFormList() {
        forms[0] = new Form(1,"Apakah anda aktif melakukan aktivitas fisik ?","Tidak","Jarang","Ya","",R.drawable.default_image);
        forms[1] = new Form(2,"Apakah anda merokok ?","Perokok Aktif","Sedang berusaha berhenti merokok","Tidak Merokok","",R.drawable.default_image);
        forms[2] = new Form(3,"Apakah anda pernah mengalami tekanan darah tinggi ?","Ya","","Tidak","",R.drawable.default_image);
        forms[3] = new Form(4,"Masukkan tekanan darah anda saat ini","> 140/90","120 - 139 / 80 - 89","< 120/80","Tidak diketahui",R.drawable.default_image);
        forms[4] = new Form(5,"Apakah anda pernah mengalami peningkatan kadar gula darah (saat hamil, sakit, pemeriksaan gula darah) ?","Iya","","Tidak","",R.drawable.default_image);
        forms[5] = new Form(6,"Masukkan kadar gula anda saat ini","> 150","120 - 150","< 120","",R.drawable.default_image);
        forms[6] = new Form(7,"Berapa kadar Cholesterol anda saat ini ?","> 240","200 - 239","< 200","Tidak diketahui",R.drawable.default_image);
        forms[7] = new Form(8,"Apakah keluarga memiliki riwayat stroke ?","Ya","","Tidak","Tidak diketahui",R.drawable.default_image);
        forms[8] = new Form(9,"Apakah menderita gangguan irama jantung ?\n","> 140/90","","Tidak","Tidak diketahui",R.drawable.default_image);
    }
}