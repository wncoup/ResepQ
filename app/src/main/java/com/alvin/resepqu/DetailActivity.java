package com.alvin.resepqu;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.alvin.resepqu.Adapter.ResepAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_JUDUL = "judul";
    public static final String KEY_DESKRIPSI = "deskripsi";
    public static final String KEY_IMG = "img";
    public static final String KEY_TIME = "time";
    public static final String KEY_CARA = "cara";

    ImageView ivImgDetial;
    TextView tvJudul, tvDeskripsi, tvCara, tvTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        getSupportActionBar().setTitle(getResources().getString(R.string.detail));

        ivImgDetial = findViewById(R.id.iv_imgCardItem_Detail);
        tvJudul = findViewById(R.id.tv_judulResepCardItem_Detail);
        tvDeskripsi = findViewById(R.id.tv_deskripsiResepCardItem_Detail);
        tvCara = findViewById(R.id.tv_caraResepCardItem_Detail);


        ivImgDetial.setImageURI(Uri.parse(getIntent().getStringExtra(KEY_IMG)));
        tvJudul.setText(getIntent().getStringExtra(KEY_JUDUL));
        tvDeskripsi.setText(getIntent().getStringExtra(KEY_DESKRIPSI));
        tvCara.setText(getIntent().getStringExtra(KEY_CARA));
    }

}
