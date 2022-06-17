package com.apadok.emrpreventive.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.apadok.emrpreventive.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LogOutAuthError extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Untuk keamanan, Anda perlu login kembali")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Context context = requireContext().getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            try {
                                KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
                                String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
                                sharedPref = EncryptedSharedPreferences.create(
                                        getString(R.string.preference_file_key),
                                        mainKeyAlias,
                                        requireContext().getApplicationContext(),
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                );
                            } catch (GeneralSecurityException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        requireActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
