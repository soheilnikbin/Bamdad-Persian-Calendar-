package com.byagowi.persiancalendar.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.byagowi.persiancalendar.R;
import com.squareup.picasso.Picasso;


public class AddActivity extends AppCompatActivity {

    TextView discription_view,ad_title;
    Button button;
    String add_Image_url, title, discription1, discription2, discription3, discription4, discription5, discription6, ad_url, button_title;
    ImageView ad_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_Image_url = getIntent().getExtras().getString("BAMDAD_AD_IMAGE");
        title = getIntent().getExtras().getString("BAMDAD_AD_TITLE");
        discription1 = getIntent().getExtras().getString("BAMDAD_AD_DISCRIPTION1");
        discription2 = getIntent().getExtras().getString("BAMDAD_AD_DISCRIPTION2");
        discription3 = getIntent().getExtras().getString("BAMDAD_AD_DISCRIPTION3");
        discription4 = getIntent().getExtras().getString("BAMDAD_AD_DISCRIPTION4");
        discription5 = getIntent().getExtras().getString("BAMDAD_AD_DISCRIPTION5");
        discription6 = getIntent().getExtras().getString("BAMDAD_AD_DISCRIPTION6");
        ad_url = getIntent().getExtras().getString("BAMDAD_AD_URL");
        button_title = getIntent().getExtras().getString("BAMDAD_AD_BUTTON_TITLE");

        discription_view = (TextView) findViewById(R.id.ad_discription);
        ad_title = (TextView) findViewById(R.id.ad_title);
        button = (Button) findViewById(R.id.button_link);
        ad_image = (ImageView) findViewById(R.id.ad_image_view);

        ad_title.setText(title);
        Picasso.with(getBaseContext())
                .load(add_Image_url)
                .placeholder(R.drawable.ad_background)
                .error(R.drawable.ad_background)
                .resize(900, 563)
                .into(ad_image);


        discription_view.setText(discription1+
                            "\t"+discription2+
                            "\t"+discription3+
                            "\t"+discription4+
                            "\t"+discription5+
                            "\t"+discription6);

        button.setText(button_title);

        button.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(ad_url));
            startActivity(i);
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
