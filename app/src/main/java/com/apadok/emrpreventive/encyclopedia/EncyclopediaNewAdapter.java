package com.apadok.emrpreventive.encyclopedia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.database.entity.EncyclopediaEntity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import me.grantland.widget.AutofitHelper;

public class EncyclopediaNewAdapter extends ArrayAdapter<EncyclopediaEntity> {

    // invoke the suitable constructor of the ArrayAdapter class
    public EncyclopediaNewAdapter(@NonNull Context context, ArrayList<EncyclopediaEntity> arrayList) {

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
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_list, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        EncyclopediaEntity currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.tv_article_title);
        textView1.setText((currentNumberPosition.getJudul_artikel()));

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.tv_article_type);
        if (Objects.equals(currentNumberPosition.getJenis_artikel(), "1")) {
            textView2.setText("Jenis Artikel : Teks");
        } else if (Objects.equals(currentNumberPosition.getJenis_artikel(), "2")) {
            textView2.setText("Jenis Artikel : Video");
        } else {
            textView2.setText("Jenis Artikel : Infografis");
        }

        ImageView imageView1 = currentItemView.findViewById(R.id.iv_article_image);
        if (currentNumberPosition.getGambar_artikel() != null) {
            String image = currentNumberPosition.getGambar_artikel();
            String url = "http://apadok.com/media/klinik/" + image;
            Picasso.get().load(url).into(imageView1, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.e("err",url);
                    imageView1.setImageResource(R.drawable.ic_doctor);
                }

            });
        }

        String positionstring = Integer.toString(position);
        currentItemView.setTag(positionstring);

        AutofitHelper.create(textView1);
        AutofitHelper.create(textView2);

        // then return the recyclable view
        return currentItemView;
    }
}
