package com.apadok.emrpreventive.screening;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;


public class KebugaranScreeningActivity extends AppCompatActivity implements View.OnClickListener {
    // Question Length is 13 as for Now, Check Res/Layout to adjust Progress Bar
    // Use FormAnswer[14] to put Identifier or Primary Key for User
    private FormKebugaran[] forms = new FormKebugaran[13];
    private FormAnswer[] answer = new FormAnswer[14];


    // Iterative or Boolean Variables
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;
    private Boolean isOptionSubmitted = false;

    // Res/Layout Variables
    private TextView tv_option_one, tv_option_two, tv_option_three, tv_option_four, tv_option_five, tv_progress, tv_question;
    private Button btn_submit, btn_backquestion;
    private ProgressBar progressBar;
    private ScrollView sv_bugar_screening;

    // Intent Variables
    private String ClinicName, ClinicLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kebugaran_screening);

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

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                DialogFragment newFragment = new ConfirmExiting();
                //Pass the User ID to next activity
                ((ConfirmExiting) newFragment).setMessage("Anda ingin membatalkan proses skrining?");
                newFragment.show(getSupportFragmentManager(), "");
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        CreateFormList();
        SetForm();
    }


    private void SetForm() {
        FormKebugaran FormQ = forms[CurrentForm - 1];

        sv_bugar_screening = (ScrollView) findViewById(R.id.sv_bugar_screening);
        tv_option_one = (TextView) findViewById(R.id.tv_option_one);
        tv_option_two = (TextView) findViewById(R.id.tv_option_two);
        tv_option_three = (TextView) findViewById(R.id.tv_option_three);
        tv_option_four = (TextView) findViewById(R.id.tv_option_four);
        tv_option_five = (TextView) findViewById(R.id.tv_option_five);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_question = (TextView) findViewById(R.id.tv_question);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_backquestion = (Button) findViewById(R.id.btn_backquestion);
        progressBar.setProgress(CurrentForm);
        tv_progress.setText(CurrentForm + "/" + progressBar.getMax());


        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_option_one.setTypeface(helvetica_font);
        tv_option_two.setTypeface(helvetica_font);
        tv_option_three.setTypeface(helvetica_font);
        tv_option_four.setTypeface(helvetica_font);
        tv_option_five.setTypeface(helvetica_font);
        tv_progress.setTypeface(helvetica_font);
        tv_question.setTypeface(helvetica_font);
        btn_submit.setTypeface(helvetica_font);
        btn_backquestion.setTypeface(helvetica_font);

        tv_question.setText(FormQ.getQuestion());

        defaultOptionsView();

        if (CurrentForm == 13) {
            btn_submit.setText("Selesai");
        } else {
            btn_submit.setText("Submit");
        }

        CheckSet();
        btn_submit.setOnClickListener(this);
        btn_backquestion.setOnClickListener(this);

        // Process Previously Recorded Data
        String previous_data = answer[CurrentForm - 1].getAnswer();
        if (previous_data != null){
            try {
                SelectedOptionPosititon = Integer.parseInt(previous_data);
                switch (SelectedOptionPosititon) {
                    case 1:
                        if (!isOptionSubmitted) selectedOptionView(tv_option_one, 1);
                        break;
                    case 2:
                        if (!isOptionSubmitted) selectedOptionView(tv_option_two, 2);
                        break;
                    case 3:
                        if (!isOptionSubmitted) selectedOptionView(tv_option_three, 3);
                        break;
                    case 4:
                        if (!isOptionSubmitted) selectedOptionView(tv_option_four, 4);
                        break;
                }
            } catch (NumberFormatException ignored){
            }
        }

        // Reset ScrollView
        sv_bugar_screening.scrollTo(0, 0);
    }

    private void CheckSet() {
        if (CurrentForm == 1) {
            btn_backquestion.setVisibility(View.GONE);
        } else {
            btn_backquestion.setVisibility(View.VISIBLE);
        }
        tv_option_one.setText("Tidak pernah");
        tv_option_one.setOnClickListener(this);
        tv_option_one.setVisibility(View.VISIBLE);
        tv_option_two.setText("Merasakan sedikit");
        tv_option_two.setOnClickListener(this);
        tv_option_two.setVisibility(View.VISIBLE);
        tv_option_three.setText("Beberapa kali merasakan");
        tv_option_three.setOnClickListener(this);
        tv_option_three.setVisibility(View.VISIBLE);
        tv_option_four.setText("Cukup banyak merasakan");
        tv_option_four.setOnClickListener(this);
        tv_option_four.setVisibility(View.VISIBLE);
        tv_option_five.setText("Sangat sering merasakan");
        tv_option_five.setOnClickListener(this);
        tv_option_five.setVisibility(View.VISIBLE);
    }

    private void defaultOptionsView() {

        btn_submit.setEnabled(false);
        btn_backquestion.setEnabled(true);

        ArrayList<TextView> options = new ArrayList<TextView>();
        options.add(0, tv_option_one);
        options.add(1, tv_option_two);
        options.add(2, tv_option_three);
        options.add(3, tv_option_four);
        options.add(4, tv_option_five);

        for (TextView option : options) {
            option.setTextColor(Color.parseColor("#7A8089"));
            option.setBackground(ContextCompat.getDrawable(
                    KebugaranScreeningActivity.this,
                    R.drawable.default_option_border_bg
            ));
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_option_one:
                if (!isOptionSubmitted) selectedOptionView(tv_option_one, 1);
                break;
            case R.id.tv_option_two:
                if (!isOptionSubmitted) selectedOptionView(tv_option_two, 2);
                break;
            case R.id.tv_option_three:
                if (!isOptionSubmitted) selectedOptionView(tv_option_three, 3);
                break;
            case R.id.tv_option_four:
                if (!isOptionSubmitted) selectedOptionView(tv_option_four, 4);
                break;
            case R.id.tv_option_five:
                if (!isOptionSubmitted) selectedOptionView(tv_option_five, 5);
                break;

            case R.id.btn_backquestion:
                CurrentForm--;
                SetForm();
                break;

            case R.id.btn_submit:
                isOptionSubmitted = true;
                switch (SelectedOptionPosititon) {
                    case 1:
                        answer[CurrentForm - 1].setAnswer("0");
                        SelectedOptionPosititon = 0;
                        break;
                    case 2:
                        answer[CurrentForm - 1].setAnswer("1");
                        SelectedOptionPosititon = 0;
                        break;
                    case 3:
                        answer[CurrentForm - 1].setAnswer("2");
                        SelectedOptionPosititon = 0;
                        break;
                    case 4:
                        answer[CurrentForm - 1].setAnswer("3");
                        SelectedOptionPosititon = 0;
                        break;
                    case 5:
                        answer[CurrentForm - 1].setAnswer("4");
                        SelectedOptionPosititon = 0;
                        break;
                    default:
                        // Failsafe Case
                        SelectedOptionPosititon = 1;
                        break;
                }
                SelectedOptionPosititon = 0;
                // legacy code
                if (SelectedOptionPosititon == 0) {
                    isOptionSubmitted = false;
                    CurrentForm++;

                    if (CurrentForm <= 13) {
                        SetForm();
                    } else {
                        //Get the User ID from Main Activity
                        answer[CurrentForm - 1] = new FormAnswer("id_user");
                        int id_user = getIntent().getIntExtra("userid", 0);
                        String token = getIntent().getStringExtra("token");
                        String username = getIntent().getStringExtra("username");
                        answer[CurrentForm - 1].setAnswer(Integer.toString(id_user));

                        Intent intent = new Intent(KebugaranScreeningActivity.this, KebugaranScreeningResultActivity.class);
                        ArrayList list = new ArrayList<>(Arrays.asList(answer));
                        intent.putParcelableArrayListExtra("Answers", list);
                        intent.putExtra("token", token);
                        intent.putExtra("username", username);
                        intent.putExtra("clinicname", ClinicName);
                        intent.putExtra("cliniclogo", ClinicLogo);
                        startActivity(intent);
                        finish();
                    }

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
//        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setBackground(ContextCompat.getDrawable(
                KebugaranScreeningActivity.this,
                R.drawable.selected_option_border_bg
        ));
    }

    private final void CreateFormList() {
        forms[0] = new FormKebugaran(1, "Saya merasa lelah secara mental dan fisik");
        forms[1] = new FormKebugaran(2, "Saya merasa lemah secara keseluruhan");
        forms[2] = new FormKebugaran(3, "Saya merasa lesu");
        forms[3] = new FormKebugaran(4, "Saya merasa lelah secara fisik");
        forms[4] = new FormKebugaran(5, "Saya merasa kesulitan memulai sesuatu karena saya lelah");
        forms[5] = new FormKebugaran(6, "Saya merasa kesulitan menyelesaikan sesuatu karena saya lelah");
        forms[6] = new FormKebugaran(7, "Saya memiliki tenaga");
        forms[7] = new FormKebugaran(8, "Saya bisa melakukan aktivitas seperti biasa");
        forms[8] = new FormKebugaran(9, "Saya perlu tidur di siang hari");
        forms[9] = new FormKebugaran(10, "Saya merasa terlalu lelah untuk makan");
        forms[10] = new FormKebugaran(11, "Saya membutuhkan bantuan untuk mengerjakan aktivitas sehari - hari");
        forms[11] = new FormKebugaran(12, "Saya merasa frustrasi karena terlalu lelah untuk melakukan hal-hal yang ingin saya lakukan");
        forms[12] = new FormKebugaran(13, "Saya harus membatasi aktivitas sosial saya karena saya lelah");

        //Setup JSON Answer to be sent to API
        answer[0] = new FormAnswer("pertanyaan_1");
        answer[1] = new FormAnswer("pertanyaan_2");
        answer[2] = new FormAnswer("pertanyaan_3");
        answer[3] = new FormAnswer("pertanyaan_4");
        answer[4] = new FormAnswer("pertanyaan_5");
        answer[5] = new FormAnswer("pertanyaan_6");
        answer[6] = new FormAnswer("pertanyaan_7");
        answer[7] = new FormAnswer("pertanyaan_8");
        answer[8] = new FormAnswer("pertanyaan_9");
        answer[9] = new FormAnswer("pertanyaan_10");
        answer[10] = new FormAnswer("pertanyaan_11");
        answer[11] = new FormAnswer("pertanyaan_12");
        answer[12] = new FormAnswer("pertanyaan_13");
    }
}
