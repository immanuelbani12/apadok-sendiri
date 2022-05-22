package com.apadok.emrpreventive.screening;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;


public class KebugaranScreeningActivity extends AppApadokActivity implements View.OnClickListener {
    // Question Length is 13 as for Now, Check Res/Layout to adjust Progress Bar
    // Use FormAnswer[14] to put Identifier or Primary Key for User
    private FormKebugaran[] forms = new FormKebugaran[13];
    private FormAnswer[] answer = new FormAnswer[14];


    // Iterative or Boolean Variables
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;
    private Boolean isOptionSubmitted = false;

    // Res/Layout Variables
    private TextView tv_progress, tv_question, left_add_text, right_add_text, center_add_text;
    private RadioButton radio_one, radio_two, radio_three, radio_four, radio_five;
    private Button btn_submit, btn_backquestion;
    private ProgressBar progressBar;
    private ScrollView sv_bugar_screening;
    private RadioGroup radioGroup;

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
        radio_one = (RadioButton) findViewById(R.id.radio_one);
        radio_two = (RadioButton) findViewById(R.id.radio_two);
        radio_three = (RadioButton) findViewById(R.id.radio_three);
        radio_four = (RadioButton) findViewById(R.id.radio_four);
        radio_five = (RadioButton) findViewById(R.id.radio_five);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupKebugaran);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_question = (TextView) findViewById(R.id.tv_question);
        left_add_text = (TextView) findViewById(R.id.left_add_text);
        right_add_text = (TextView) findViewById(R.id.right_add_text);
        center_add_text = (TextView) findViewById(R.id.center_add_text);
        tv_question = (TextView) findViewById(R.id.tv_question);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_backquestion = (Button) findViewById(R.id.btn_backquestion);
        progressBar.setProgress(CurrentForm);
        tv_progress.setText(CurrentForm + "/" + progressBar.getMax());


        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        radio_one.setTypeface(helvetica_font);
        radio_two.setTypeface(helvetica_font);
        radio_three.setTypeface(helvetica_font);
        radio_four.setTypeface(helvetica_font);
        radio_five.setTypeface(helvetica_font);
        tv_progress.setTypeface(helvetica_font);
        tv_question.setTypeface(helvetica_font);
        btn_submit.setTypeface(helvetica_font);
        left_add_text.setTypeface(helvetica_font);
        right_add_text.setTypeface(helvetica_font);
        center_add_text.setTypeface(helvetica_font);
        center_add_text.setText("( Selama 7 hari terakhir )");
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
                    case 0:
                        if (!isOptionSubmitted) selectedOptionView(radio_one, 1);
                        break;
                    case 1:
                        if (!isOptionSubmitted) selectedOptionView(radio_two, 2);
                        break;
                    case 2:
                        if (!isOptionSubmitted) selectedOptionView(radio_three, 3);
                        break;
                    case 3:
                        if (!isOptionSubmitted) selectedOptionView(radio_four, 4);
                        break;
                    case 4:
                        if (!isOptionSubmitted) selectedOptionView(radio_five, 5);
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
        radio_one.setText("1");
        radio_one.setVisibility(View.VISIBLE);
        radio_two.setText("2");
        radio_two.setVisibility(View.VISIBLE);
        radio_three.setText("3");
        radio_three.setVisibility(View.VISIBLE);
        radio_four.setText("4");
        radio_four.setVisibility(View.VISIBLE);
        radio_five.setText("5");
        radio_five.setVisibility(View.VISIBLE);
    }

    private void defaultOptionsView() {

        btn_submit.setEnabled(false);
        btn_backquestion.setEnabled(true);
        radioGroup.clearCheck();

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
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

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radio_one:
                if (checked) {
                    if (!isOptionSubmitted) selectedOptionView(radio_one, 1);
                }
                break;
            case R.id.radio_two:
                if(checked) {
                    if (!isOptionSubmitted) selectedOptionView(radio_two, 2);
                }
                break;
            case R.id.radio_three:
                if(checked) {
                    if (!isOptionSubmitted) selectedOptionView(radio_three, 3);
                }
                break;
            case R.id.radio_four:
                if(checked){
                    if (!isOptionSubmitted) selectedOptionView(radio_four, 4);
                }
                break;
            case R.id.radio_five:
                if(checked){
                    if (!isOptionSubmitted) selectedOptionView(radio_five, 5);
                }
                break;
        }
    }

    private void selectedOptionView(RadioButton radio, int selectedOptionNum) {

        defaultOptionsView();
        SelectedOptionPosititon = selectedOptionNum;
        btn_submit.setEnabled(true);
        btn_backquestion.setEnabled(true);
        radio.setChecked(true);
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
