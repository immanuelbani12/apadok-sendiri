package com.apadok.emrpreventive.encyclopedia;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.apadok.emrpreventive.R;
import com.apadok.emrpreventive.common.AppApadokActivity;
import com.igreenwood.loupe.Loupe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EncyclopediaInfografisPhotoViewActivity extends AppApadokActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_infografis_photoview);
        setupItemView();
    }

    private void setupItemView() {
        String urlimage = getIntent().getStringExtra("urlimage");
        ImageView iv_image = (ImageView) findViewById(R.id.iv_image);
        ImageView back_icon = (ImageView) findViewById(R.id.back_icon);
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        Picasso.get().load(urlimage).into(iv_image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                iv_image.setImageResource(R.drawable.ic_doctor);
            }

        });
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
        Loupe loupe = new Loupe(iv_image, container);
        loupe.setOnViewTranslateListener(new Loupe.OnViewTranslateListener() {
            @Override
            public void onStart(@NonNull ImageView imageView) {

            }

            @Override
            public void onViewTranslate(@NonNull ImageView imageView, float v) {

            }

            @Override
            public void onDismiss(@NonNull ImageView imageView) {
                supportFinishAfterTransition();
            }

            @Override
            public void onRestore(@NonNull ImageView imageView) {

            }
        });
    }
}

