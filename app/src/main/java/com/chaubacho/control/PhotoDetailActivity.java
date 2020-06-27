package com.chaubacho.control;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chaubacho.model.Photo;
import com.chaubacho.newflickbrowser.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import static com.chaubacho.Const.PHOTO_TRANSFER;

public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        final Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);

        if (photo != null) {
//            collapsingToolbarLayout.setTitle(getResources().getString(R.string.photo_title_text, photo.getTitle()));
            collapsingToolbarLayout.setTitleEnabled(false);
            ((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string.photo_title_text, photo.getTitle()));

            ImageView imageView = findViewById(R.id.backdrop);
            Picasso.get().load(createImgUri(photo.getFarm_id(),
                    photo.getServer_id(),
                    photo.getId(),
                    photo.getSecret(),
                    "b"))
                    .error(android.R.drawable.ic_menu_gallery)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(imageView);

        }
    }

    private String createImgUri(String farm_id, String server_id, String id, String secret, String size) {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg",
                farm_id, server_id, id, secret, size);
    }
}
