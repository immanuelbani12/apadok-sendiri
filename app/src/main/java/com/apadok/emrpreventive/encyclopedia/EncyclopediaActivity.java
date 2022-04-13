package com.apadok.emrpreventive.encyclopedia;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.SetupToolbar;
import com.apadok.emrpreventive.common.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EncyclopediaActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private ArrayList<Encyclopedia> ecl = new ArrayList<Encyclopedia>();
    private ListView l;
    private JsonObject returnvalue;
    private ArrayList<Encyclopedia> eclnew;
    private int diabetval, strokeval, cardioval;
    private String ClinicName,ClinicLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia);
        // Code to Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        SetupToolbar.changeToolbarFont(myToolbar, this);
        String clinicname = getIntent().getStringExtra("clinicname");

        diabetval = getIntent().getIntExtra("categorydiabetes", 0);
        strokeval = getIntent().getIntExtra("categorystroke", 0);
        cardioval = getIntent().getIntExtra("categorykardio", 0);
        TextView clinic = (TextView) findViewById(R.id.tv_clinic);
        clinic.setText(clinicname);

        // Init Logo RS
        String logo = getIntent().getStringExtra("cliniclogo");
        ImageView cliniclogo = (ImageView) findViewById(R.id.iv_cliniclogo);
        String url = "http://178.128.25.139:8080/media/klinik/" + logo;
        Picasso.get().load(url).into(cliniclogo);

        CreateFormList();
        eclnew = FilterEncyclopedia();
        l = findViewById(R.id.history_screening);
        Typeface helvetica_font = ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_neue);
        EncyclopediaAdapter numbersArrayAdapter = new EncyclopediaAdapter(getBaseContext(), eclnew);
        l.setAdapter(numbersArrayAdapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String idhistory = (String) view.getTag();
                int id_history = Integer.parseInt(idhistory);
                DialogFragment newFragment = new ConfirmArticleFormat();
                //Pass the User ID to next activity
                ((ConfirmArticleFormat) newFragment).setPosition(position+1);
                ((ConfirmArticleFormat) newFragment).setData(eclnew.get(id_history));
                ((ConfirmArticleFormat) newFragment).setClinicname(clinicname);
                ((ConfirmArticleFormat) newFragment).setCliniclogo(logo);
                newFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private ArrayList<Encyclopedia> FilterEncyclopedia() {
        ArrayList<Encyclopedia> eclparsed = new ArrayList<Encyclopedia>();
        if (strokeval <= 2) {
            String filtered = "1";
            for (int counter = 0; counter < ecl.size(); counter++) {
                if (ecl.get(counter).kategori_artikel == filtered) {
                    eclparsed.add(ecl.get(counter));
                }
            }
        }
        if (diabetval <= 2) {
            String filtered = "2";
            for (int counter = 0; counter < ecl.size(); counter++) {
                if (ecl.get(counter).kategori_artikel == filtered) {
                    eclparsed.add(ecl.get(counter));
                }
            }
        }
        if (cardioval <= 2) {
            String filtered = "3";
            for (int counter = 0; counter < ecl.size(); counter++) {
                if (ecl.get(counter).kategori_artikel == filtered) {
                    eclparsed.add(ecl.get(counter));
                }
            }
        }
        return eclparsed;
    }


    private void createCalls(String json, final VolleyCallBack callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Temporarily Get ID Pemeriksan From Main Activity
        int id_user = getIntent().getIntExtra("user", 0);
        String URL = "http://178.128.25.139:8080/pemeriksaan/userAll/"+id_user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Type screenhistory = new TypeToken<List<Encyclopedia>>() {}.getType();
                //FailSafe
                if (response.charAt(response.length()-1) != ']'){
                    response = response + "]";
                }
                ecl = gson.fromJson(response, screenhistory);
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                callback.onError();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    private final void CreateFormList(){
        ecl.add(new Encyclopedia("1","Artikel Stroke","Stroke merupakan penyebab kematian tersering kedua di dunia. Setiap tahun, lebih dari 795.000 orang di Amerika Serikat mengalami stroke. Stroke terjadi ketika pembuluh darah di otak pecah dan berdarah, atau ketika ada penyumbatan suplai darah ke otak. Pecahnya atau penyumbatan mencegah darah dan oksigen mencapai jaringan otak. Tanpa oksigen, sel-sel otak dan jaringan menjadi rusak dan mulai mati dalam beberapa menit. \n\n" +
                "Hilangnya aliran darah ke otak akan merusak jaringan di dalam otak. Gejala stroke muncul di bagian tubuh yang dikendalikan oleh area otak yang rusak. Gejala stroke dapat meliputi:\n" +
                "•\tmati rasa atau kelemahan pada lengan, wajah, dan kaki, terutama pada satu sisi tubuh,\n" +
                "•\tkesulitan berbicara atau memahami orang lain\n" +
                "•\tbicara tidak jelas\n" +
                "•\tkebingungan, disorientasi, atau kurang responsif\n" +
                "•\tperubahan perilaku yang tiba-tiba, terutama peningkatan agitasi\n" +
                "•\tmasalah penglihatan, seperti kesulitan melihat pada satu atau kedua mata dengan penglihatan menghitam atau kabur, atau penglihatan ganda\n" +
                "•\tkesulitan berjalan\n" +
                "•\tkehilangan keseimbangan atau koordinasi\n" +
                "•\tpusing parah, sakit kepala mendadak dengan penyebab yang tidak diketahui\n" +
                "•\tkejang\n" +
                "•\tmual atau muntah\n\n" +
                "Perubahan gaya hidup dapat mencegah dan menurunkan risiko stroke. Perubahan gaya hidup tersebut antara lain sebagai berikut:\n" +
                "•\tBerhenti merokok. Jika Anda merokok, berhenti sekarang akan menurunkan risiko stroke. Anda dapat berkonsultasi dengan dokter untuk membuat rencana berhenti merokok.\n" +
                "•\tBatasi penggunaan alkohol. Konsumsi alkohol berat dapat meningkatkan tekanan darah Anda, yang pada gilirannya meningkatkan risiko stroke. Jika mengurangi asupan Anda sulit, hubungi dokter Anda untuk meminta bantuan.\n" +
                "•\tPertahankan berat badan yang ideal. Kegemukan dan obesitas meningkatkan risiko stroke. Untuk membantu mengelola berat badan Anda, makan makanan yang seimbang dan tetap aktif secara fisik lebih sering daripada tidak. Kedua langkah tersebut juga dapat menurunkan tekanan darah dan kadar kolesterol.\n" +
                "•\tPeriksa rutin. Bicarakan dengan dokter Anda tentang seberapa sering Anda harus memeriksakan tekanan darah, kolesterol, dan kondisi apa pun yang mungkin Anda miliki\n","1","https://www.youtube.com/watch?v=6iCdi5ANXLg","",""));
        ecl.add(new Encyclopedia("2","Artikel Diabetes","Diabetes mellitus dapat menyebabkan berbagai komplikasi yang membahayakan bila tidak segera ditangani. Komplikasi tersebut diantaranya adalah stroke, penyakit jantung, penyakit ginjal, gangguan penglihatan, infeksi kaki yang tidak segera sembuh hingga menyebabkan bagian tersebut harus diamputasi.\n" +
                "\n" +
                "Diabetes mellitus, yang biasa dikenal dengan penyakit kencing manis, adalah penyakit metabolik yang menyebabkan gula darah tinggi. Hormon insulin memindahkan gula dari darah ke sel-sel Anda untuk disimpan atau digunakan untuk energi. Dengan diabetes, tubuh Anda tidak membuat cukup insulin atau tidak dapat secara efektif menggunakan insulin yang dihasilkannya.\n" +
                " \n" +
                "Gejala umum diabetes yang sering ditemukan antara lain:\n" +
                "•\tpeningkatan rasa lapar\n" +
                "•\tpeningkatan rasa haus\n" +
                "•\tpenurunan berat badan\n" +
                "•\tsering buang air kecil\n" +
                "•\tpenglihatan kabur\n" +
                "•\tkelelahan ekstrim\n" +
                "•\tLuka yang tak kunjung sembuh\n" +
                " \n" +
                "Diabetes tipe 1 tidak dapat dicegah karena disebabkan oleh masalah pada sistem kekebalan tubuh. Beberapa penyebab diabetes tipe 2, seperti gen atau usia Anda, juga tidak dapat Anda kendalikan.  Namun banyak faktor risiko diabetes lainnya dapat dikendalikan. Sebagian besar strategi pencegahan diabetes melibatkan penyesuaian sederhana pada diet dan rutinitas kebugaran Anda. Berikut adalah beberapa hal yang dapat Anda lakukan untuk mencegah diabetes tipe 2: \n" +
                "•\tLakukan olah raga aerobic setidaknya 150 menit per minggu, seperti berjalan kaki atau bersepeda.\n" +
                "•\tKurangi lemak jenuh dan trans , bersama dengan karbohidrat olahan, dari diet Anda. \n" +
                "•\tMakan lebih banyak buah, sayuran, dan biji-bijian.\n" +
                "•\tMakan dengan porsi yang lebih kecil.\n" +
                "•\tCobalah untuk menurunkan 7 persen berat badan Anda jika Anda kelebihan berat badan atau obesitas. \n" +
                "•\tMengurangi konsumsi makanan dan minuman yang manis\n" +
                "•\tHindari makanan junkfood, alcohol, dan mengandung kolesterol tinggi\n","2","https://youtube.com/shorts/vgQL3cdiFXU?feature=share","",""));
        ecl.add(new Encyclopedia("3","Judul Artikel Kardiovaskular","Penyakit jantung merupakan penyebab kematian utama di dunia. Penyakit jantung mengacu pada setiap kondisi yang mempengaruhi jantung. Ada banyak jenis penyakit jantung, diantaranya adalah: penyakit jantung coroner, gagal jantung, kardiomiopati, aritmia, dan penyakit jantung bawaaan.\n" +
                "\n" +
                "Gejala yang sering muncul pada penyakit jantung:\n" +
                "•\tangina, atau nyeri dada\n" +
                "•\tkesulitan bernapas\n" +
                "•\tkelelahan dan pusing\n" +
                "•\tbengkak karena retensi cairan, atau edema.\n" +
                "•\tjantung berdebar\n" +
                "•\tmual\n" +
                "•\tnyeri perut\n" +
                "•\tberkeringat\n" +
                "•\tlengan, rahang, punggung, atau kaki nyeri\n" +
                "•\tdetak jantung tidak teratur\n" +
                "\n" +
                "Serangan jantung dapat menyebabkan henti jantung, yaitu ketika jantung jantung berhenti dan tubuh tidak dapat berfungsi lagi. Seseorang membutuhkan perhatian medis segera jika mereka memiliki gejala serangan jantung.\n" +
                "Jika serangan jantung terjadi, orang tersebut akan membutuhkan:\n" +
                "•\tbantuan medis segera \n" +
                "•\tresusitasi kardiopulmoner segera\n" +
                "•\tkejutan dari defibrillator eksternal otomatis, jika tersedia\n\n" +
                "Beberapa tindakan gaya hidup dapat membantu mengurangi risiko penyakit jantung. Diantaranya adalah :\n" +
                "•\tMakan makanan seimbang: Pilihlah makanan sehat jantung yang kaya serat dan menyukai biji-bijian dan buah-buahan dan sayuran segar. Diet Mediterania dan DASH mungkin baik untuk kesehatan jantung. Juga, mungkin membantu membatasi asupan makanan olahan dan menambahkan lemak, garam, dan gula.\n" +
                "•\tBerolahraga secara teratur: Ini dapat membantu memperkuat jantung dan sistem peredaran darah, mengurangi kolesterol, dan menjaga tekanan darah. Seseorang mungkin ingin berolahraga 150 menit per minggu. \n" +
                "•\tMempertahankan berat badan sedang: Indeks massa tubuh (BMI) yang sehat biasanya antara 20 dan 25. Orang dapat memeriksa BMI mereka di sini.\n" +
                "•\tBerhenti atau menghindari merokok: Merokok merupakan faktor risiko utama untuk kondisi jantung dan kardiovaskular.\n" +
                "•\tMembatasi asupan alkohol: Wanita tidak boleh mengonsumsi lebih dari satu minuman standar per hari, dan pria tidak boleh mengonsumsi lebih dari dua minuman standar per hari.\n" +
                "•\tMengelola kondisi yang mendasari: Carilah pengobatan untuk kondisi yang mempengaruhi kesehatan jantung, seperti tekanan darah tinggi, obesitas, dan diabetes.\n","3","https://youtube.com/shorts/WiwXDhLkTrg?feature=share","",""));
    }
}