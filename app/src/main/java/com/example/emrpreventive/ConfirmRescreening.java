package com.example.emrpreventive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.emrpreventive.shorting.stroke.ScreeningActivity;
import com.example.emrpreventive.shorting.stroke.StrokeFormActivity;

public class ConfirmRescreening extends DialogFragment {
    private int user_id;
    private String Token;

    private String clinicname;
    private String cliniclogo;
    private String username;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Anda sudah melakukan skrining dalam waktu kurang dari 3 hari,\n\nLakukan kembali skrining?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), ScreeningActivity.class);
                        intent.putExtra("userid", user_id);
                        intent.putExtra("token", Token);
                        intent.putExtra("clinicname", clinicname);
                        intent.putExtra("cliniclogo", cliniclogo);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setClinicname(String clinicname) {
        this.clinicname = clinicname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCliniclogo(String cliniclogo) {
        this.cliniclogo = cliniclogo;
    }

    public void setToken(String token) {
        this.Token = token;
    }
}
