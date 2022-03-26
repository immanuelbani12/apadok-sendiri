package com.apadok.emrpreventive.screening;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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


public class KebugaranScreeningActivity extends AppCompatActivity implements View.OnClickListener {
    // Question Length is 18 as for Now, Check Res/Layout to adjust Progress Bar
    // Use FormAnswer[18] to put Identifier or Primary Key for User
    private FormKebugaran[] forms = new FormKebugaran[13];
//    private FormAnswer[] answer = new FormAnswer[19];


    // Iterative or Boolean Variables
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;
    private Boolean isOptionSubmitted = false;

    // Res/Layout Variables
    private TextView tv_option_one,tv_option_two, tv_option_three, tv_option_four, tv_option_five, tv_progress, tv_question;
    private Button btn_submit, btn_backquestion;
    private ProgressBar progressBar;

    // Intent Variables
    private String ClinicName,ClinicLogo;

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
        String url = "http://178.128.25.139:8080/media/klinik/" + ClinicLogo;
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
        FormKebugaran FormQ = forms[CurrentForm-1];

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


        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
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
    }

    private void CheckSet(){
        if(CurrentForm == 1){
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

//    public static void scrollToView(ScrollView scrollView, View view) {
//        int vTop = view.getTop();
//
//        while (!(view.getParent() instanceof ScrollView)) {
//            view = (View) view.getParent();
//            vTop += view.getTop();
//        }
//
//        final int scrollPosition = vTop;
//
//        new Handler().post(() -> scrollView.smoothScrollTo(0, scrollPosition));
//    }

    private void defaultOptionsView() {

        btn_submit.setEnabled(false);
        btn_backquestion.setEnabled(true);

        ArrayList<TextView> options = new ArrayList<TextView>();
        options.add(0,tv_option_one);
        options.add(1, tv_option_two);
        options.add(2, tv_option_three);
        options.add(3, tv_option_four);
        options.add(4, tv_option_five);

        for (TextView option : options){
            option.setTextColor(Color.parseColor("#7A8089"));
            option.setBackground(ContextCompat.getDrawable(
                    KebugaranScreeningActivity.this,
                    R.drawable.default_option_border_bg
            ));
        }

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
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
                SelectedOptionPosititon = 0;
                CurrentForm--;
                SetForm();
//                Ku-komen line 212, appnya jadi gabisa kebuild
//                break;

//            case R.id.btn_submit:
//                isOptionSubmitted = true;
//                switch (SelectedOptionPosititon){
//                    case 1:
//                        if (CurrentForm == 1) {
//                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt1());
//                        } else {
//                            answer[CurrentForm-1].setAnswer("1");
//                        }
//                        break;
//                    case 2:
//                        if (CurrentForm == 1) {
//                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt2());
//                        } else {
//                            answer[CurrentForm - 1].setAnswer("2");
//                        }
//                        break;
//                    case 3:
////                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt3());
//                        answer[CurrentForm-1].setAnswer("3");
//                        break;
//                    case 4:
////                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt4());
//                        answer[CurrentForm-1].setAnswer("4");
//                        break;
//                }
            SelectedOptionPosititon = 0;
            // legacy code
            if (SelectedOptionPosititon == 0) {
                isOptionSubmitted = false;
                CurrentForm++;

                if(CurrentForm <= 18){
                    SetForm();
                } else {
                    //Get the User ID from Main Activity
//                        answer[CurrentForm-1] = new FormAnswer("id_user");
//                        int id_user = getIntent().getIntExtra("userid", 0);
                    String token = getIntent().getStringExtra("token");
                    String username = getIntent().getStringExtra("username");
//                        answer[CurrentForm-1].setAnswer(Integer.toString(id_user));

                    Intent intent = new Intent(KebugaranScreeningActivity.this, ScreeningResultActivity.class);
//                        ArrayList list = new ArrayList<>(Arrays.asList(answer));
//                        intent.putParcelableArrayListExtra("Answers", list);
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

    private final void CreateFormList(){
        forms[0] = new FormKebugaran(1,"Saya merasa lelah secara fisik");
        forms[1] = new FormKebugaran(2,"Saya merasa lemah di semua hal");
        forms[2] = new FormKebugaran(3,"Saya merasa lesu");
        forms[3] = new FormKebugaran(4,"Saya merasa lelah secara fisik dan psikis");
        forms[4] = new FormKebugaran(5,"Saya merasa kesulitan memulai sesuatu karena saya lelah");
        forms[5] = new FormKebugaran(6,"Saya merasa kesulitan menyelesaikan sesuatu karena saya lelah");
        forms[6] = new FormKebugaran(7,"Saya memiliki energi");
        forms[7] = new FormKebugaran(8,"Saya bisa mengerjakan aktivitas seperti biasa");
        forms[8] = new FormKebugaran(9,"Saya butuh tidur seharian");
        forms[9] = new FormKebugaran(10,"Saya merasa terlalu lelah untuk makan");
        forms[10] = new FormKebugaran(11,"Saya membutuhkan bantuan untuk mengerjakan aktivitas seperti biasa");
        forms[11] = new FormKebugaran(12,"Saya merasa frustrasi menjadi terlalu lelah untuk melakukan sesuatu yang saya ingin kerjakan");
        forms[12] = new FormKebugaran(13,"Saya harus membatasi aktivitas sosial saya karena saya lelah");

        //Setup JSON Answer to be sent to API
//        answer[0] = new FormAnswer("jenis_kelamin");
//        answer[1] = new FormAnswer("tanggal_lahir");
//        answer[2] = new FormAnswer("tinggi_badan");
//        answer[3] = new FormAnswer("berat_badan");
//        answer[4] = new FormAnswer("aktivitas_fisik");
//        answer[5] = new FormAnswer("merokok");
//        answer[6] = new FormAnswer("lingkar_pinggang");
//        answer[7] = new FormAnswer("histori_hipertensi");
//        answer[8] = new FormAnswer("tekanan_darah");
//        answer[9] = new FormAnswer("gula_darah");
//        answer[10] = new FormAnswer("kadar_gula");
//        answer[11] = new FormAnswer("kadar_kolesterol");
//        answer[12] = new FormAnswer("riwayat_stroke");
//        answer[13] = new FormAnswer("irama_jantung");
//        answer[14] = new FormAnswer("buah_sayur");
//        answer[15] = new FormAnswer("obat_hipertensi");
//        answer[16] = new FormAnswer("keturunan");
//        answer[17] = new FormAnswer("kolesterol_hdl");
    }
}
