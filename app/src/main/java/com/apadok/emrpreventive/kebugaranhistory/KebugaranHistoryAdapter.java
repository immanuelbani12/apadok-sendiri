package com.apadok.emrpreventive.kebugaranhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.StringToTimeStampFormatting;
import com.apadok.emrpreventive.database.entity.PemeriksaanKebugaranEntity;

import java.util.ArrayList;

import me.grantland.widget.AutofitHelper;

public class KebugaranHistoryAdapter extends ArrayAdapter<PemeriksaanKebugaranEntity> {
    // invoke the suitable constructor of the ArrayAdapter class
    public KebugaranHistoryAdapter(@NonNull Context context, ArrayList<PemeriksaanKebugaranEntity> arrayList) {

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
        PemeriksaanKebugaranEntity currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
//        ImageView numbersImage = currentItemView.findViewById(R.id.imageView);
//        assert currentNumberPosition != null;
//        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());

        // then according to the position of the view assign the desired Title History for the same
        TextView tv_title_history = currentItemView.findViewById(R.id.tv_history);
        tv_title_history.setText("Riwayat " + (position + 1));

        // then according to the position of the view assign the desired Timestamp for the same
        TextView tv_timestamp = currentItemView.findViewById(R.id.tv_timestamp);
        if (currentNumberPosition.getUpdated_at() != null) {
            tv_timestamp.setText(StringToTimeStampFormatting.changeFormat(currentNumberPosition.getUpdated_at(),"yyyy-MM-dd HH:mm:ss", "dd MMMM yyyy HH.mm"));
        } else if (currentNumberPosition.getCreated_at() != null) {
            tv_timestamp.setText(StringToTimeStampFormatting.changeFormat(currentNumberPosition.getCreated_at(),"yyyy-MM-dd HH:mm:ss", "dd MMMM yyyy HH.mm"));
        }

        currentItemView.setTag(currentNumberPosition.getId_pemeriksaan_kebugaran());

        AutofitHelper.create(tv_title_history);
        AutofitHelper.create(tv_timestamp);

        // then return the recyclable view
        return currentItemView;
    }
}
