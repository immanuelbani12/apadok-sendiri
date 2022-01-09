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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrokeFormActivity extends AppCompatActivity implements View.OnClickListener {

    private Form[] forms = new Form[18];
    private FormAnswer[] answer = new FormAnswer[18];
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;
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

        if (CurrentForm == 18) {
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
        // Hide Textbar
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
        if(formcheck.getOpt1() != "" && formcheck.getOpt2() != "" && formcheck.getOpt3() != "" && formcheck.getOpt4() != ""){
            // Display TextBar
        }
    }

    private void defaultOptionsView() {

        btn_submit.setEnabled(false);

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
                selectedOptionView(tv_option_one, 4);
                break;
            case R.id.tv_option_two:
                selectedOptionView(tv_option_two, 3);
                break;
            case R.id.tv_option_three:
                selectedOptionView(tv_option_three, 2);
                break;
            case R.id.tv_option_four:
                selectedOptionView(tv_option_four, 1);
                break;
            case R.id.btn_submit:
                if (SelectedOptionPosititon == 0) {

                    CurrentForm++;

                    if(CurrentForm <= 18){
                        SetForm();
                    } else {
                        Intent intent = new Intent(StrokeFormActivity.this, StrokeResultActivity.class);
                        ArrayList list = new ArrayList<>(Arrays.asList(answer));
                        intent.putParcelableArrayListExtra("Answers", list);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    if (CurrentForm == 18) {
                        btn_submit.setText("Kirim Hasil");
                    } else {
                        btn_submit.setText("Pertanyaan Selanjutnya");
                    }

                    answer[CurrentForm-1].setQuestion(forms[CurrentForm-1].getQuestion());
                    switch (SelectedOptionPosititon){
                        case 1:
                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt1());
                            break;
                        case 2:
                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt2());
                            break;
                        case 3:
                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt3());
                            break;
                        case 4:
                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt4());
                            break;
                        default:
                            // Get answer from TextBar
                    }
                    SelectedOptionPosititon = 0;
                }
                break;
        }
    }

    private void selectedOptionView(TextView tv, int selectedOptionNum) {

        defaultOptionsView();

        SelectedOptionPosititon = selectedOptionNum;
        btn_submit.setEnabled(true);


        tv.setTextColor(
                Color.parseColor("#363A43")
        );
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setBackground(ContextCompat.getDrawable(
                StrokeFormActivity.this,
                R.drawable.selected_option_border_bg
                ));
    }

    private final void CreateFormList(){
        forms[0] = new Form(1,"Jenis Kelamin","Laki-laki","Perempuan","","",R.drawable.default_image);
        forms[1] = new Form(2,"Tanggal Lahir","","","","",R.drawable.default_image);
        forms[2] = new Form(3,"Masukkan tinggi badan (cm)","","","","",R.drawable.default_image);
        forms[3] = new Form(4,"Masukkan berat badan (kg)","","","","",R.drawable.default_image);
        forms[4] = new Form(5,"Apakah anda aktif melakukan aktivitas fisik?","Ya","Tidak","Jarang","",R.drawable.default_image);
        forms[5] = new Form(6,"Apakah anda merokok?","Perokok Aktif","Sedang berusaha berhenti merokok","Tidak Merokok","",R.drawable.default_image);
        forms[6] = new Form(7,"Ukuran lingkar pinggang (cm)","","","","", R.drawable.default_image);
        forms[7] = new Form(8,"Apakah anda pernah mengalami tekanan darah tinggi?","Ya","Tidak","","",R.drawable.default_image);
        forms[8] = new Form(9,"Masukkan tekanan darah anda saat ini:","> 140/90","120 - 139 / 80 - 89","< 120/80","Tidak diketahui",R.drawable.default_image);
        forms[9] = new Form(10,"Apakah anda pernah mengalami peningkatan kadar gula darah (saat hamil, sakit, pemeriksaan gula darah) ?","Ya","Tidak","","",R.drawable.default_image);
        forms[10] = new Form(11,"Masukkan kadar gula anda saat ini:","< 120","120 - 150","> 150","",R.drawable.default_image);
        forms[11] = new Form(12,"Berapa kadar kolesterol anda saat ini? (mmol/L)","> 240","200 - 239","< 200","Tidak diketahui",R.drawable.default_image);
        forms[12] = new Form(13,"Apakah keluarga memiliki riwayat stroke?","Ya","Tidak","Tidak diketahui","",R.drawable.default_image);
        forms[13] = new Form(14,"Apakah anda menderita gangguan irama jantung?","Ya","Tidak diketahui","Tidak pernah","",R.drawable.default_image);
        forms[14] = new Form(15,"Seberapa sering anda makan sayuran, buah-buahan atau beri?","Setiap Hari","Tidak setiap hari","","",R.drawable.default_image);
        forms[15] = new Form(16,"Apakah mengonsumsi obat anti hipertensi secara reguler?","Tidak","Ya","","",R.drawable.default_image);
        forms[16] = new Form(17,"Apakah memiliki anggota keluarga atau saudara yang terdiagnosa diabetes? (Diabetes 1 atau Diabetes 2)","Tidak","Ya (Kakek/Nenek, Bibi, Paman, atau sepupu dekat)","Ya (Orang tua, Kakak, Adik, Anak kandung)","",R.drawable.default_image);
        forms[17] = new Form(18,"Berapakah kadar kolesterol sehat (HDL) anda saat ini (mmol/L)","< 30","30 - 50","> 50","Tidak Diketahui",R.drawable.default_image);
    }
}