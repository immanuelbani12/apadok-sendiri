package com.apadok.emrpreventive.encyclopedia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmArticleFormat extends DialogFragment {
    // Intent Variables
    private Encyclopedia data;
    private String clinicname;
    private String cliniclogo;
    private int position;

    public Encyclopedia getData() {
        return data;
    }

    public void setData(Encyclopedia data) {
        this.data = data;
    }

    public String getClinicname() {
        return clinicname;
    }

    public void setClinicname(String clinicname) {
        this.clinicname = clinicname;
    }

    public String getCliniclogo() {
        return cliniclogo;
    }

    public void setCliniclogo(String cliniclogo) {
        this.cliniclogo = cliniclogo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Pilih bentuk artikel yang ingin ditampilkan")
                .setPositiveButton("Video", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        String videoid = getVideoId(data.getLink_artikel());
                        if (videoid == null) {
                            //Toast
                            Toast toast = Toast.makeText(getContext(), "Video Artikel Terkait Gagal Ditemukan", Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videoid));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } catch (ActivityNotFoundException e) {
                            // youtube is not installed.Will be opened in other available apps
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + videoid));
                            startActivity(i);
                        }
                    }
                })
                .setNegativeButton("Teks", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), EncyclopediaDetailActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("judul_artikel", data.getJudul_artikel());
                        intent.putExtra("isi_artikel", data.getIsi_artikel());
                        intent.putExtra("kategori_artikel", data.getKategori_artikel());
                        intent.putExtra("clinicname", clinicname);
                        intent.putExtra("cliniclogo", cliniclogo);
                        startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    //Regex
    public static String getVideoId(String videoUrl) {
        final String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F|shorts)[^#\\&\\?\\n]*";
        if (videoUrl == null || videoUrl.trim().length() <= 0) {
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
