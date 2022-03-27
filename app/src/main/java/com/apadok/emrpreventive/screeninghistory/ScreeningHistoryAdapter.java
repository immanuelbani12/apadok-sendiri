package com.apadok.emrpreventive.screeninghistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;

import java.util.ArrayList;

public class ScreeningHistoryAdapter extends ArrayAdapter<PemeriksaanEntity> {

    // invoke the suitable constructor of the ArrayAdapter class
    public ScreeningHistoryAdapter(@NonNull Context context, ArrayList<PemeriksaanEntity> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_screening_history, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        PemeriksaanEntity currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
//        ImageView numbersImage = currentItemView.findViewById(R.id.imageView);
//        assert currentNumberPosition != null;
//        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.tv_history);
        textView1.setText("Riwayat "+ (position+1));

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.tv_timestamp);
        if (currentNumberPosition.getUpdated_at() != null) {
            textView2.setText(currentNumberPosition.getUpdated_at());
        } else if (currentNumberPosition.getCreated_at() != null){
            textView2.setText(currentNumberPosition.getCreated_at());
        }

        currentItemView.setTag(currentNumberPosition.getId_pemeriksaan());

        // then return the recyclable view
        return currentItemView;
    }
}
