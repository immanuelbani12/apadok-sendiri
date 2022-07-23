package com.apadok.emrpreventive.screening;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.apadok.emrpreventive.common.ConfirmExiting;
import com.apadok.emrpreventive.common.EmptyTextWatcher;
import com.apadok.emrpreventive.common.RegexorChecker;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.user.ConfirmLogOut;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import me.grantland.widget.AutofitHelper;

public class ScreeningActivity extends AppApadokActivity implements View.OnClickListener {

    // Question Length is 18 as for Now, Check Res/Layout to adjust Progress Bar
    // Use FormAnswer[19] to put Identifier or Primary Key for User
    private final Form[] forms = new Form[18];
    private final FormAnswer[] answer = new FormAnswer[19];


    // Iterative or Boolean Variables
    private int CurrentForm = 1;
    private int SelectedOptionPosititon = 0;
    private Boolean isOptionSubmitted = false;
    private Boolean isMonthFilled = false;

    // Res/Layout Variables
    private TextView tv_option_one, tv_option_two, tv_option_three, tv_option_four, tv_progress, tv_question_screening;
    private Button btn_submit, btn_backquestion;
    private ImageView iv_image_screening;
    private EditText answer_input;
    private ProgressBar progressBar;
    private ScrollView sv_screening;
    private final RegexorChecker regex = new RegexorChecker();
    private MaterialSpinner month_picker;
    private NumberPicker year_picker;
    private String Year = "", Month = "";

    // Intent Variables
    private String ClinicName, ClinicLogo;
    private Boolean tekanan_darah = false;
    private Boolean kadar_gula = false;
    private Boolean kadar_kolesterol = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        SetupItemView();
        SetupFormData();
        SetForm();
    }

    private void SetupItemView() {

        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        ClinicName = getIntent().getStringExtra("clinicname");
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(ClinicName);
        // Load Clinic Logo for Toolbar
        ClinicLogo = getIntent().getStringExtra("cliniclogo");
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://apadok.com/media/institusi/" + ClinicLogo;
        Picasso.get().load(url).into(cliniclogo);

        // Back Button Handler
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                DialogFragment newFragment = new ConfirmExiting();
                //Pass User Properties to next activity
                ((ConfirmExiting) newFragment).setMessage("Anda ingin membatalkan proses skrining?");
                newFragment.show(getSupportFragmentManager(), "");
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        //Assign UI Attributes
        sv_screening = (ScrollView) findViewById(R.id.sv_screening);
        tv_option_one = (TextView) findViewById(R.id.tv_option_one);
        tv_option_two = (TextView) findViewById(R.id.tv_option_two);
        tv_option_three = (TextView) findViewById(R.id.tv_option_three);
        tv_option_four = (TextView) findViewById(R.id.tv_option_four);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_question_screening = (TextView) findViewById(R.id.tv_question_screening);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_backquestion = (Button) findViewById(R.id.btn_backquestion);
        answer_input = (EditText) findViewById(R.id.editText);
        iv_image_screening = (ImageView) findViewById(R.id.iv_image_screening);
        month_picker = (MaterialSpinner) findViewById(R.id.month_picker);
        year_picker = (NumberPicker) findViewById(R.id.year_picker);


        //Assign Font Type
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_neue);
        tv_option_one.setTypeface(helvetica_font);
        tv_option_two.setTypeface(helvetica_font);
        tv_option_three.setTypeface(helvetica_font);
        tv_option_four.setTypeface(helvetica_font);
        tv_progress.setTypeface(helvetica_font);
        tv_question_screening.setTypeface(helvetica_font);
        btn_submit.setTypeface(helvetica_font);
        btn_backquestion.setTypeface(helvetica_font);
        answer_input.setTypeface(helvetica_font);
    }

    private void SetupFormData() {
        //Setup Question in the Activity
        forms[0] = new Form(1, "Jenis Kelamin", "Laki-laki", "Perempuan", "", "", R.drawable.default_image, null);
        forms[1] = new Form(2, "Bulan dan tahun lahir", "", "", "", "", R.drawable.birthdate, "Bulan dan tahun lahir");
        forms[2] = new Form(3, "Masukkan tinggi badan (cm)", "", "", "", "", R.drawable.tinggi_badan, "Tinggi badan (cm)");
        forms[3] = new Form(4, "Masukkan berat badan (kg)", "", "", "", "", R.drawable.berat_badan, "Berat badan (kg)");
        forms[4] = new Form(5, "Apakah anda aktif melakukan aktivitas fisik minimal 30 menit setiap hari?", "Ya", "Tidak", "Jarang", "", R.drawable.default_image, null);
        forms[5] = new Form(6, "Apakah anda merokok?", "Perokok Aktif", "Sedang berusaha berhenti merokok", "Tidak Merokok", "", R.drawable.default_image, null);
        forms[6] = new Form(7, "Ukuran lingkar pinggang (cm)", "", "", "", "", R.drawable.lingkar_pinggang, "Lingkar pinggang (cm)");
        forms[7] = new Form(8, "Apakah anda pernah mengalami tekanan darah tinggi?", "Ya", "Tidak", "", "", R.drawable.darah_tinggi, null);
        forms[8] = new Form(9, "Masukkan tekanan darah anda saat ini", "> 140/90", "120 - 139 / 80 - 89", "< 120/80", "Tidak diketahui", R.drawable.default_image, null);
        forms[9] = new Form(10, "Apakah anda pernah mengalami peningkatan kadar gula darah (saat hamil, sakit, pemeriksaan gula darah) ?", "Ya", "Tidak", "", "", R.drawable.default_image, null);
        forms[10] = new Form(11, "Masukkan kadar gula anda saat ini", "< 120", "120 - 150", "> 150", "Tidak diketahui", R.drawable.default_image, null);
        forms[11] = new Form(12, "Berapa kadar kolesterol anda saat ini? (mmol/L)", "> 240", "200 - 239", "< 200", "Tidak diketahui", R.drawable.default_image, null);
        forms[12] = new Form(13, "Apakah keluarga memiliki riwayat stroke?", "Ya", "Tidak", "Tidak diketahui", "", R.drawable.default_image, null);
        forms[13] = new Form(14, "Apakah anda menderita gangguan irama jantung?", "Ya", "Tidak diketahui", "Tidak pernah", "", R.drawable.default_image, null);
        forms[14] = new Form(15, "Seberapa sering anda makan sayuran, buah-buahan atau beri?", "Setiap Hari", "Tidak setiap hari", "", "", R.drawable.sayuran, null);
        forms[15] = new Form(16, "Apakah mengonsumsi obat anti hipertensi secara reguler?", "Tidak", "Ya", "", "", R.drawable.obat, null);
        forms[16] = new Form(17, "Apakah memiliki anggota keluarga atau saudara yang terdiagnosa diabetes? (Diabetes 1 atau Diabetes 2)", "Tidak", "Ya (Kakek/Nenek, Bibi, Paman, atau sepupu dekat)", "Ya (Orang tua, Kakak, Adik, Anak kandung)", "", R.drawable.default_image, null);
        forms[17] = new Form(18, "Berapakah kadar kolesterol sehat (HDL) anda saat ini (mmol/L)", "< 30", "30 - 50", "> 50", "Tidak Diketahui", R.drawable.default_image, null);

        //Setup JSON Answer-Question Structure to be sent to API
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

    private void SetForm() {
        //Read Current Question
        Form FormQ = forms[CurrentForm - 1];

        //Update Question Progress & Question
        progressBar.setProgress(CurrentForm);
        tv_progress.setText(CurrentForm + "/" + progressBar.getMax());
        tv_question_screening.setText(FormQ.getQuestion());

        //Set TextView AutoFit
        AutofitHelper.create(tv_question_screening);
        AutofitHelper.create(tv_option_one);
        AutofitHelper.create(tv_option_two);
        AutofitHelper.create(tv_option_three);
        AutofitHelper.create(tv_option_four);

        defaultOptionsView();

        CheckForms(FormQ);

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
                    default:
                        SelectedOptionPosititon = -1;
                        answer_input.setText(answer[CurrentForm - 1].getAnswer());
                        break;
                }
            } catch (NumberFormatException e){
                Log.e("prev",previous_data);
                if (previous_data.equals("Laki-laki")){
                    if (!isOptionSubmitted) selectedOptionView(tv_option_one, 1);
                } else if (previous_data.equals("Perempuan")){
                    if (!isOptionSubmitted) selectedOptionView(tv_option_two, 2);
                } else {
                    SelectedOptionPosititon = -1;
                    previous_data = previous_data.substring(2);
                    answer_input.setText(previous_data);
//                    try {
//                        String[] splitted = previous_data.split("/");
//                        Month = splitted[0];
//                        Year = splitted[1];
//                        isMonthFilled = true;
//                    } catch (NullPointerException nothing){}
                }
            }
        }

        // Reset ScrollView to the Top
        sv_screening.scrollTo(0, 0);
    }

    private void CheckForms(Form formcheck) {
        // Hide TextField on New Question
        answer_input.setVisibility(View.GONE);
        month_picker.setVisibility(View.GONE);
        year_picker.setVisibility(View.GONE);
        answer_input.setText(null);
        // Reset the Click Behavior on Image & TextField
        answer_input.setOnFocusChangeListener(null);
        iv_image_screening.setOnClickListener(null);
        // Update what to display on Submit Button
        if (CurrentForm == 18) {
            btn_submit.setText("Selesai");
        } else {
            btn_submit.setText("Selanjutnya");
        }
        // Hide Mundur Button on First Question
        if (CurrentForm == 1) {
            btn_backquestion.setVisibility(View.GONE);
        } else {
            btn_backquestion.setVisibility(View.VISIBLE);
        }

        // Set Non-Available Answer Options as Invisible
        if (formcheck.getImage() != R.drawable.default_image) {
            iv_image_screening.setImageResource(formcheck.getImage());
            iv_image_screening.setVisibility(View.VISIBLE);
        } else {
            iv_image_screening.setVisibility(View.GONE);
        }
        if (!formcheck.getOpt1().equals("")) {
            tv_option_one.setText(formcheck.getOpt1());
            tv_option_one.setOnClickListener(this);
            tv_option_one.setVisibility(View.VISIBLE);
        } else {
            tv_option_one.setVisibility(View.GONE);
        }
        if (!formcheck.getOpt2().equals("")) {
            tv_option_two.setText(formcheck.getOpt2());
            tv_option_two.setOnClickListener(this);
            tv_option_two.setVisibility(View.VISIBLE);
        } else {
            tv_option_two.setVisibility(View.GONE);
        }
        if (!formcheck.getOpt3().equals("")) {
            tv_option_three.setText(formcheck.getOpt3());
            tv_option_three.setOnClickListener(this);
            tv_option_three.setVisibility(View.VISIBLE);
        } else {
            tv_option_three.setVisibility(View.GONE);
        }
        if (!formcheck.getOpt4().equals("")) {
            tv_option_four.setText(formcheck.getOpt4());
            tv_option_four.setOnClickListener(this);
            tv_option_four.setVisibility(View.VISIBLE);
        } else {
            tv_option_four.setVisibility(View.GONE);
        }

        // Prerpare TextField if expected Answer is Essay
        if (formcheck.getOpt1().equals("") && formcheck.getOpt2().equals("") && formcheck.getOpt3().equals("") && formcheck.getOpt4().equals("")) {
            SelectedOptionPosititon = -1;
            // Enable TextField and Show Hints on it
            answer_input.setVisibility(View.VISIBLE);
            answer_input.setHint(formcheck.getHint());
            // If TextField is on "Bulan dan tahun lahir" Question
            if (formcheck.getHint().equals("Bulan dan tahun lahir")) {

                // Change TextField input to DateTime
                answer_input.setInputType(InputType.TYPE_CLASS_DATETIME);
                month_picker.setVisibility(View.VISIBLE);
                year_picker.setVisibility(View.VISIBLE);

//                // Create an ArrayAdapter using the string array and a default spinner layout
//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                        R.array.months_array, android.R.layout.simple_spinner_item);
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                // Apply the adapter to the spinner
//                month_picker.setAdapter(adapter);

                month_picker.setItems("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember");

                Calendar cldr = Calendar.getInstance();
                int indexofmonth = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                year_picker.setMinValue(year-100);
                year_picker.setMaxValue(year);

                if (Year.equals("") || Month.equals("")){
                    month_picker.setSelectedIndex(indexofmonth);
                    year_picker.setValue(year-30);
                    Year = Integer.toString(year-30);
                    Month = Integer.toString(indexofmonth);
                    answer_input.setText((Month) + "/" + Year);
                    btn_submit.setEnabled(true);
                } else {
                    month_picker.setSelectedIndex(Integer.parseInt(Month)-1);
                    year_picker.setValue(Integer.parseInt(Year));
                    answer_input.setText((Month) + "/" + Year);
                }

                month_picker.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        Month = Integer.toString(position+1);
                        Log.e("Month", Month);
                        answer_input.setText((Month) + "/" + Year);
                    }
                });


//                month_picker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                        Month = Integer.toString(pos+1);
//                        Log.e("Month", Month);
//                        answer_input.setText((Month) + "/" + Year);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });

                year_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldval, int newval) {
                        Year = Integer.toString(newval);
                        Log.e("Year",Year);
                        if (!Month.equals("")){
                            answer_input.setText((Month) + "/" + Year);
                        }
                    }
                });



//                //Display MonthPicker
//                answer_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (hasFocus) {
//                            // Start Calendar from 12 Years ago
//                            Calendar cldr = Calendar.getInstance();
//                            cldr.add(Calendar.YEAR, -30);
//                            int month = cldr.get(Calendar.MONTH);
//                            int year = cldr.get(Calendar.YEAR);
//
//                            // Set MonthPicker
//                            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ScreeningActivity.this, new MonthPickerDialog.OnDateSetListener() {
//                                @Override
//                                public void onDateSet(int selectedMonth, int selectedYear) {
//                                    answer_input.setText((selectedMonth + 1) + "/" + selectedYear);
//                                }
//                            }, year, month);
//                            builder.setActivatedMonth(Calendar.MONTH)
//                                    .build()
//                                    .show();
//                        }
//                    }
//                });
//
//                // Use Image to Reset DatePicker
//                iv_image_screening.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        answer_input.clearFocus();
//                        answer_input.requestFocus();
//                    }
//                });
            } else {
                // Change TextField input to Number
                answer_input.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            // Check Empty and Regex for TextField Answers
            answer_input.addTextChangedListener(new EmptyTextWatcher() {
                @Override
                // When TextField is empty
                public void onEmptyField() {
                    btn_submit.setEnabled(false);
//                    if (CurrentForm == 2) {
//                        answer_input.clearFocus();
//                        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Tekan foto untuk memilih kembali bulan", Snackbar.LENGTH_SHORT);
//                        snackbar.setBackgroundTint(ContextCompat.getColor(getBaseContext(),R.color.orange_dark));
//                        snackbar.show();
//                    }
                }

                // When TextField is filled
                @Override
                public void onFilledField() {
                    switch (CurrentForm) {
                        // Regex when on 2nd Question
                        case 2:
                            if (regex.DateRegex(answer_input.getText().toString())) {
                                btn_submit.setEnabled(true);
                                answer_input.setError(null);
                            } else {
                                btn_submit.setEnabled(false);
//                                answer_input.setError("Format bulan/tahun tidak valid");
//                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Format bulan/tahun tidak valid", Snackbar.LENGTH_SHORT);
//                                snackbar.setBackgroundTint(ContextCompat.getColor(getBaseContext(),R.color.orange_dark));
//                                snackbar.show();
                            }
                            break;
                        case 3:
                            // Regex when on 3rd Question
                            if (regex.HeightChecker(answer_input.getText().toString())) {
                                btn_submit.setEnabled(true);
                                answer_input.setError(null);
                            } else {
                                btn_submit.setEnabled(false);
                                answer_input.setError("Tinggi badan tidak valid");
                            }
                            break;
                        case 4:
                            // Regex when on 4th Question
                            if (regex.WeightChecker(answer_input.getText().toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                                answer_input.setError("Berat badan tidak valid");
                            }
                            break;
                        case 7:
                            // Regex when on 5th Question
                            if (regex.HipsChecker(answer_input.getText().toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                                answer_input.setError("Lingkar pinggang tidak valid");
                            }
                            break;
                    }
                }
            });
        }
    }

    private void defaultOptionsView() {
        // Disable Button on New Question
        btn_submit.setEnabled(false);
        btn_backquestion.setEnabled(true);

        // Prerpare Themed Answer Options
        ArrayList<TextView> options = new ArrayList<>();
        options.add(0, tv_option_one);
        options.add(1, tv_option_two);
        options.add(2, tv_option_three);
        options.add(3, tv_option_four);
        for (TextView option : options) {
            option.setTextColor(Color.parseColor("#7A8089"));
            option.setBackground(ContextCompat.getDrawable(
                    ScreeningActivity.this,
                    R.drawable.default_option_border_bg
            ));
        }

    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    @SuppressLint("NonConstantResourceId")
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

            case R.id.btn_backquestion:
                CurrentForm--;
                SetForm();
                break;


            case R.id.btn_submit:
                isOptionSubmitted = true;
                switch (SelectedOptionPosititon) {
                    case 1:
                        // Jenis Kelamin
                        if (CurrentForm == 1) {
                            answer[CurrentForm - 1].setAnswer(forms[CurrentForm - 1].getOpt1());
                        } else {
                            answer[CurrentForm - 1].setAnswer("1");
                        }
                        SelectedOptionPosititon = 0;
                        break;
                    case 2:
                        // Jenis Kelamin
                        if (CurrentForm == 1) {
                            answer[CurrentForm - 1].setAnswer(forms[CurrentForm - 1].getOpt2());
                        } else {
                            answer[CurrentForm - 1].setAnswer("2");
                        }
                        SelectedOptionPosititon = 0;
                        break;
                    case 3:
                        answer[CurrentForm - 1].setAnswer("3");
                        SelectedOptionPosititon = 0;
                        break;
                    case 4:
                        switch (CurrentForm){
                            case 9:
                                tekanan_darah = true;
                                break;
                            case 11:
                                kadar_gula = true;
                                break;
                            case 12:
                                kadar_kolesterol = true;
                                break;
                        }
                        answer[CurrentForm - 1].setAnswer("4");
                        SelectedOptionPosititon = 0;
                        break;
                    case -1:
                        // Get answer from Textbar here
                        answer[CurrentForm - 1].setAnswer(answer_input.getText().toString());
                        // Add Date for MonthPicker
                        if (CurrentForm == 2)
                            answer[CurrentForm - 1].setAnswer(1 + "/" + answer_input.getText().toString());
                        SelectedOptionPosititon = 0;
                        dismissKeyboard(this);
                        break;
                    default:
                        // Failsafe Case
                        SelectedOptionPosititon = 1;
                        break;
                }
                // legacy code
                if (SelectedOptionPosititon == 0) {
                    isOptionSubmitted = false;
                    CurrentForm++;

                    if (CurrentForm <= 18) {
                        SetForm();
                    } else {
                        //Get the User ID from Main Activity
                        answer[CurrentForm - 1] = new FormAnswer("id_user");
                        int id_user = getIntent().getIntExtra("userid", 0);
                        String token = getIntent().getStringExtra("token");
                        String username = getIntent().getStringExtra("username");
//                        String role = getIntent().getStringExtra("role");
                        String clinicphone = getIntent().getStringExtra("clinicphone");
                        answer[CurrentForm - 1].setAnswer(Integer.toString(id_user));

                        Intent intent = new Intent(ScreeningActivity.this, ScreeningResultActivity.class);
                        ArrayList<FormAnswer> list = new ArrayList<>(Arrays.asList(answer));
                        intent.putParcelableArrayListExtra("Answers", list);
                        intent.putExtra("token", token);
                        intent.putExtra("username", username);
                        intent.putExtra("clinicname", ClinicName);
                        intent.putExtra("cliniclogo", ClinicLogo);
                        intent.putExtra("clinicphone", clinicphone);
//                        intent.putExtra("role", role);
                        intent.putExtra("tekanan_darah", tekanan_darah);
                        intent.putExtra("kadar_gula", kadar_gula);
                        intent.putExtra("kadar_kolesterol", kadar_kolesterol);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }

    private void selectedOptionView(TextView tv, int selectedOptionNum) {
        //Disable Other Options
        defaultOptionsView();
        
        //Update Selected Option & Enables Submit Button
        SelectedOptionPosititon = selectedOptionNum;
        btn_submit.setEnabled(true);
        tv.setTextColor(
                Color.parseColor("#363A43")
        );
        tv.setBackground(ContextCompat.getDrawable(
                ScreeningActivity.this,
                R.drawable.selected_option_border_bg
        ));
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.option_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                DialogFragment newFragment = new ConfirmLogOut();
                newFragment.show(getSupportFragmentManager(), "");
            default:
                return false;
        }
    }

}