package com.example.emrpreventive.shorting.stroke;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.emrpreventive.ConfirmExiting;
import com.example.emrpreventive.R;
import com.example.emrpreventive.SetupToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ScreeningActivity extends AppCompatActivity implements View.OnClickListener {

    // Question Length is 18 as for Now, Check Res/Layout to adjust Progress Bar
    // Use FormAnswer[18] to put Identifier or Primary Key for User
    private Form[] forms = new Form[18];
    private FormAnswer[] answer = new FormAnswer[19];


    // Iterative Variables
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;

    // Res/Layout Variables
    private TextView tv_option_one,tv_option_two, tv_option_three, tv_option_four, tv_progress, tv_question;
    private Button btn_submit, btn_backquestion;
    private ImageView iv_image;
    private EditText edit_text;
    private ProgressBar progressBar;
    private DatePickerDialog picker;
    private Boolean isOptionSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        String clinicname = getIntent().getStringExtra("clinicname");
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(clinicname);

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
        Form FormQ = forms[CurrentForm-1];

        tv_option_one = (TextView) findViewById(R.id.tv_option_one);
        tv_option_two = (TextView) findViewById(R.id.tv_option_two);
        tv_option_three = (TextView) findViewById(R.id.tv_option_three);
        tv_option_four = (TextView) findViewById(R.id.tv_option_four);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_question = (TextView) findViewById(R.id.tv_question);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_backquestion = (Button) findViewById(R.id.btn_backquestion);
        edit_text = (EditText) findViewById(R.id.editText);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        progressBar.setProgress(CurrentForm);
        tv_progress.setText(CurrentForm + "/" + progressBar.getMax());

        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        tv_option_one.setTypeface(helvetica_font);
        tv_option_two.setTypeface(helvetica_font);
        tv_option_three.setTypeface(helvetica_font);
        tv_option_four.setTypeface(helvetica_font);
        tv_progress.setTypeface(helvetica_font);
        tv_question.setTypeface(helvetica_font);
        btn_submit.setTypeface(helvetica_font);
        btn_backquestion.setTypeface(helvetica_font);
        edit_text.setTypeface(helvetica_font);



        tv_question.setText(FormQ.getQuestion());

        defaultOptionsView();

        if (CurrentForm == 18) {
            btn_submit.setText("Selesai");
        } else {
            btn_submit.setText("Submit");
        }

        CheckSet(FormQ);
        btn_submit.setOnClickListener(this);
        btn_backquestion.setOnClickListener(this);
    }

    private void CheckSet(Form formcheck){
        // Hide Textbar here
        edit_text.setVisibility(View.GONE);
        edit_text.setText(null);
        if(CurrentForm == 1){
            btn_backquestion.setVisibility(View.GONE);
        } else {
            btn_backquestion.setVisibility(View.VISIBLE);
        }
        if(formcheck.getImage() != R.drawable.default_image){
            iv_image.setImageResource(formcheck.getImage());
            iv_image.setVisibility(View.VISIBLE);
        } else {
            iv_image.setVisibility(View.GONE);
        }
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
        if(formcheck.getOpt1() == "" && formcheck.getOpt2() == "" && formcheck.getOpt3() == "" && formcheck.getOpt4() == ""){
            // Display Textbar here
            SelectedOptionPosititon = -1;
            edit_text.setVisibility(View.VISIBLE);
            edit_text.setHint(formcheck.getHint());
            if(formcheck.getHint() == "Tanggal Lahir") {
                edit_text.setInputType(InputType.TYPE_CLASS_TEXT);
                edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            //Clear keyboard
                            v.clearFocus();
                            btn_submit.setEnabled(true);
                            final Calendar cldr = Calendar.getInstance();
                            cldr.add(Calendar.YEAR, -12);
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);
                            // date picker dialog
                            picker = new DatePickerDialog(ScreeningActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            edit_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }
                                    }, year, month, day);
                            picker.show();
                        }
                    }
                });
            } else {
                edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);
                edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            btn_submit.setEnabled(true);
                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                });
            }
        }
    }

    private void defaultOptionsView() {

        btn_submit.setEnabled(false);
        btn_backquestion.setEnabled(true);

        ArrayList<TextView> options = new ArrayList<TextView>();
        options.add(0,tv_option_one);
        options.add(1, tv_option_two);
        options.add(2, tv_option_three);
        options.add(3, tv_option_four);

        for (TextView option : options){
            option.setTextColor(Color.parseColor("#7A8089"));
            option.setBackground(ContextCompat.getDrawable(
                    ScreeningActivity.this,
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

            case R.id.btn_backquestion:
                SelectedOptionPosititon = 0;
                CurrentForm--;
                SetForm();
                break;

            case R.id.btn_submit:
                btn_backquestion.setEnabled(false);
                if (SelectedOptionPosititon == 0) {
                    isOptionSubmitted = false;
                    CurrentForm++;

                    if(CurrentForm <= 18){
                        SetForm();
                    } else {
                        //Get the User ID from Main Activity
                        answer[CurrentForm-1] = new FormAnswer("id_user");
                        int id_user = getIntent().getIntExtra("userid", 0);
                        String token = getIntent().getStringExtra("token");
                        String username = getIntent().getStringExtra("username");
                        String clinicname = getIntent().getStringExtra("clinicname");
                        answer[CurrentForm-1].setAnswer(Integer.toString(id_user));

                        Intent intent = new Intent(ScreeningActivity.this, StrokeResultActivity.class);
                        ArrayList list = new ArrayList<>(Arrays.asList(answer));
                        intent.putParcelableArrayListExtra("Answers", list);
                        intent.putExtra("token", token);
                        intent.putExtra("username", username);
                        intent.putExtra("clinicname", clinicname);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    isOptionSubmitted = true;
                    if (CurrentForm == 18) {
                        btn_submit.setText("Kirim Hasil");
                    } else {
                        btn_submit.setText("Pertanyaan Selanjutnya");
                    }

                    // Setup Answer Form to be Packaged into JSON
//                    answer[CurrentForm-1] = new FormAnswer(forms[CurrentForm-1].getQuestion());
                    switch (SelectedOptionPosititon){
                        case 1:
                            if (CurrentForm == 1) {
                                answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt1());
                            } else {
                                answer[CurrentForm-1].setAnswer("1");
                            }
                            break;
                        case 2:
                            if (CurrentForm == 1) {
                                answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt2());
                            } else {
                                answer[CurrentForm - 1].setAnswer("2");
                            }
                            break;
                        case 3:
//                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt3());
                            answer[CurrentForm-1].setAnswer("3");
                            break;
                        case 4:
//                            answer[CurrentForm-1].setAnswer(forms[CurrentForm-1].getOpt4());
                            answer[CurrentForm-1].setAnswer("4");
                            break;
                        case -1:
                            // Get answer from Textbar here
                            answer[CurrentForm-1].setAnswer(edit_text.getText().toString());
                            break;
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
//        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setBackground(ContextCompat.getDrawable(
                ScreeningActivity.this,
                R.drawable.selected_option_border_bg
        ));
    }

    private final void CreateFormList(){
        forms[0] = new Form(1,"Jenis Kelamin","Laki-laki","Perempuan","","",R.drawable.default_image, null);
        forms[1] = new Form(2,"Tanggal Lahir","","","","",R.drawable.screening_res, "Tanggal Lahir");
        forms[2] = new Form(3,"Masukkan tinggi badan (cm)","","","","",R.drawable.tinggi_badan, "Tinggi badan (cm)");
        forms[3] = new Form(4,"Masukkan berat badan (kg)","","","","",R.drawable.berat_badan, "Berat badan (kg)");
        forms[4] = new Form(5,"Apakah anda aktif melakukan aktivitas fisik?","Ya","Tidak","Jarang","",R.drawable.default_image, null);
        forms[5] = new Form(6,"Apakah anda merokok?","Perokok Aktif","Sedang berusaha berhenti merokok","Tidak Merokok","",R.drawable.default_image, null);
        forms[6] = new Form(7,"Ukuran lingkar pinggang (cm)","","","","", R.drawable.lingkar_pinggang, "Lingkar pinggang (cm)");
        forms[7] = new Form(8,"Apakah anda pernah mengalami tekanan darah tinggi?","Ya","Tidak","","",R.drawable.darah_tinggi, null);
        forms[8] = new Form(9,"Masukkan tekanan darah anda saat ini:","> 140/90","120 - 139 / 80 - 89","< 120/80","Tidak diketahui",R.drawable.default_image, null);
        forms[9] = new Form(10,"Apakah anda pernah mengalami peningkatan kadar gula darah (saat hamil, sakit, pemeriksaan gula darah) ?","Ya","Tidak","","",R.drawable.default_image, null);
        forms[10] = new Form(11,"Masukkan kadar gula anda saat ini:","< 120","120 - 150","> 150","Tidak diketahui",R.drawable.default_image, null);
        forms[11] = new Form(12,"Berapa kadar kolesterol anda saat ini? (mmol/L)","> 240","200 - 239","< 200","Tidak diketahui",R.drawable.default_image, null);
        forms[12] = new Form(13,"Apakah keluarga memiliki riwayat stroke?","Ya","Tidak","Tidak diketahui","",R.drawable.default_image, null);
        forms[13] = new Form(14,"Apakah anda menderita gangguan irama jantung?","Ya","Tidak diketahui","Tidak pernah","",R.drawable.default_image, null);
        forms[14] = new Form(15,"Seberapa sering anda makan sayuran, buah-buahan atau beri?","Setiap Hari","Tidak setiap hari","","",R.drawable.sayuran, null);
        forms[15] = new Form(16,"Apakah mengonsumsi obat anti hipertensi secara reguler?","Tidak","Ya","","",R.drawable.obat, null);
        forms[16] = new Form(17,"Apakah memiliki anggota keluarga atau saudara yang terdiagnosa diabetes? (Diabetes 1 atau Diabetes 2)","Tidak","Ya (Kakek/Nenek, Bibi, Paman, atau sepupu dekat)","Ya (Orang tua, Kakak, Adik, Anak kandung)","",R.drawable.default_image, null);
        forms[17] = new Form(18,"Berapakah kadar kolesterol sehat (HDL) anda saat ini (mmol/L)","< 30","30 - 50","> 50","Tidak Diketahui",R.drawable.default_image, null);

        //Setup JSON Answer to be sent to API
        answer[0] = new FormAnswer("jenis_kelamin");
        answer[1] = new FormAnswer("tanggal_lahir");
        answer[2] = new FormAnswer("tinggi_badan");
        answer[3] = new FormAnswer("berat_badan");
        answer[4] = new FormAnswer("aktivitas_fisik");
        answer[5] = new FormAnswer("merokok");
        answer[6] = new FormAnswer("lingkar_pinggang");
        answer[7] = new FormAnswer("histori_hipertensi");
        answer[8] = new FormAnswer("tekanan_darah");
        answer[9] = new FormAnswer("gula_darah");
        answer[10] = new FormAnswer("kadar_gula");
        answer[11] = new FormAnswer("kadar_kolesterol");
        answer[12] = new FormAnswer("riwayat_stroke");
        answer[13] = new FormAnswer("irama_jantung");
        answer[14] = new FormAnswer("buah_sayur");
        answer[15] = new FormAnswer("obat_hipertensi");
        answer[16] = new FormAnswer("keturunan");
        answer[17] = new FormAnswer("kolesterol_hdl");
    }
}