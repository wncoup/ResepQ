package com.alvin.resepqu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alvin.resepqu.Adapter.ResepAdapter;
import com.alvin.resepqu.Listener.ResepItemClick;
import com.alvin.resepqu.Object.ResepObject;
import com.alvin.resepqu.Sqlite.ResepDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.AlertDialog.*;

public class MainActivity extends AppCompatActivity {

    private ResepAdapter resepAdapter;
    private List<ResepObject> resepObjectsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView emptyResep;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    ImageView iv_imgItem = null, img;

    Uri imguri = null;

    private ResepDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



        init();
        toogleEmptyResep();

        resepObjectsList.addAll(db.getAllResep());

        resepAdapter = new ResepAdapter(this, resepObjectsList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1,
                GridLayoutManager.VERTICAL, false));

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(resepAdapter);
        recyclerView.addOnItemTouchListener(new ResepItemClick(this, recyclerView,
                new ResepItemClick.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        showActionsDialog(position);
                    }
                }));


//        toogleEmptyResep();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResepDialog(false, null, -1);
            }
        });

    }

    public void init(){
        db = new ResepDB(this);
        recyclerView = findViewById(R.id.rv_item);
        emptyResep = findViewById(R.id.tv_emptyItem);
    }

    private void toogleEmptyResep(){

        if (db.getResepCount() > 0){
            emptyResep.setVisibility(View.GONE);
        }else{
            emptyResep.setVisibility(View.VISIBLE);
        }
    }

    private void createResep(ResepObject resep){

        long id = db.insertResep(resep);

        ResepObject n = db.getResep(id);

        if (n != null){
            resepObjectsList.add(0, n);
            resepAdapter.notifyDataSetChanged();
        }

        toogleEmptyResep();
        Toast.makeText(MainActivity.this, "Resep Telah ditambah", Toast.LENGTH_LONG).show();
        db.close();
    }

    private void updateResep(ResepObject resep, int position){

        ResepObject ro = resepObjectsList.get(position);

        ro.setJudul(resep.getJudul());
        ro.setDeskripsi(resep.getDeskripsi());
//        ro.setImg(resep.getImg());
        ro.setCara(resep.getCara());

        if(resep.getImg() != null){
            ro.setImg(resep.getImg());
        }

        db.updateResep(ro);

        resepObjectsList.set(position, ro);
        resepAdapter.notifyItemChanged(position);

        Toast.makeText(MainActivity.this, "Resep Telah diubah", Toast.LENGTH_LONG).show();

        db.close();
    }

    private void deleteResep(int position){

        db.deleteResep(resepObjectsList.get(position));

        resepObjectsList.remove(position);
        resepAdapter.notifyItemRemoved(position);


        Toast.makeText(MainActivity.this, "Resep Telah dihapus", Toast.LENGTH_LONG).show();
    }

//    private void showQuestionUbah(final int position, final ResepObject resep){
//        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
//        View view = layoutInflater.inflate(R.layout.question_ubah,null);
//
//        final AlertDialog.Builder alertUbah = new AlertDialog.Builder(MainActivity.this);
//        alertUbah.setCancelable(false).setView(view).setMessage("Apakah ingin mengubah Resep?").setTitle("Ubah Resep!");
//
//        final Button btnUbah, btnBatal;
//        btnBatal = view.findViewById(R.id.btn_questionUbah_BATAL);
//        btnUbah = view.findViewById(R.id.btn_questionUbah_UBAH);
//
//        final AlertDialog alertDialog = alertUbah.create();
//
//        alertDialog.setOnShowListener(new OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                btnBatal.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                btnUbah.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//
//                    }
//                });
//            }
//        });
//
//        alertDialog.show();
//    }

    private void showQuestionHapus(final int position){
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.question_hapus,null);

        final AlertDialog.Builder alertUbah = new AlertDialog.Builder(MainActivity.this);
        alertUbah.setCancelable(false).setView(view).setMessage("Apakah ingin menghapus Resep?").setTitle("Hapus Resep!");

        final Button btnHapus, btnBatal;
        btnBatal = view.findViewById(R.id.btn_questionHapus_BATAL);
        btnHapus = view.findViewById(R.id.btn_questionHapus_HAPUS);

        final AlertDialog alertDialog = alertUbah.create();

        alertDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                btnHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        deleteResep(position);
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void showActionsDialog(final int position){

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.option_crud, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

//        builder.setTitle("Choose Option");
        builder.setView(view);
        builder.setCancelable(true);

        final Button btnUbah, btnHapus;
        btnUbah = view.findViewById(R.id.btn_optionUbah);
        btnHapus = view.findViewById(R.id.btn_optionHapus);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // Ubah
                btnUbah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        showResepDialog(true, resepObjectsList.get(position), position);
//                        showQuestionUbah(position);
                    }
                });

                // Hapus
                btnHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
//                        deleteResep(position);
                        showQuestionHapus(position);

                    }
                });
            }
        });

        alertDialog.show();

//        CharSequence crud[] = new CharSequence[]{"Ubah Resep", "Hapus Resep"};

//        builder.setItems(crud,new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (i == 0){
//                    showResepDialog(true, resepObjectsList.get(position), position);
//                }else {
//                    deleteResep(position);
//                }
//            }
//        });

//        builder.show();


    }

//    ====================================================================================================
//    ====================================================================================================

    private void showResepDialog(final boolean shouldUpdate, final ResepObject resep, final int position){

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_insert, null);

        final AlertDialog.Builder alertDialogBuilderUserInput =
                new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilderUserInput.setView(view);


        iv_imgItem = view.findViewById(R.id.iv_imgItem);
        img = view.findViewById(R.id.iv_imgLogo);

        final EditText judul = view.findViewById(R.id.et_judulResep);
        final EditText deskripsi = view.findViewById(R.id.et_deskripsiResep);
        final EditText cara = view.findViewById(R.id.et_caraResep);


        iv_imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION);
                }else {
                    CropImage.startPickImageActivity(MainActivity.this);
                }
            }
        });

        // Dialog Title - Dialog Insert
        TextView dialogTitle = view.findViewById(R.id.tv_dialogEmpty);
        dialogTitle.setText(!shouldUpdate ? "Tambah Resep" : "Ubah Resep");

        if (shouldUpdate && resep != null){
            judul.setText(resep.getJudul());
            deskripsi.setText(resep.getDeskripsi());
            //
            // gambar.setImageURI(Uri.parse(resep.getImg()));
            //
            cara.setText(resep.getCara());
        }

        // Button Alert - Dialog Insert
//        alertDialogBuilderUserInput
//                .setCancelable(true)
//                .setPositiveButton(shouldUpdate ? "Ubah" : "Tambah", new OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });

        final Button btnBatal, btnTambah, btnUbah;
        btnBatal = view.findViewById(R.id.btnBatal);
        btnTambah = view.findViewById(R.id.btnTambah);
        btnUbah = view.findViewById(R.id.btnUbah);


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();


        alertDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                if (shouldUpdate){
                    btnTambah.setVisibility(View.GONE);
                    btnUbah.setVisibility(View.VISIBLE);

                    btnUbah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(judul.getText().toString())) {
                                Toast.makeText(MainActivity.this, "Masukkan Judul Resep!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                alertDialog.dismiss();
                            }

                            ResepObject ro = new ResepObject();
                            ro.setJudul(judul.getText().toString());
                            ro.setDeskripsi(deskripsi.getText().toString());
                            ro.setCara(cara.getText().toString());


                            if (imguri != null) {
                                ro.setImg(imguri.toString());
                            }

                            updateResep(ro, position);
                            imguri = null;
                        }
                    });

                }else{
                    btnUbah.setVisibility(View.GONE);
                    btnTambah.setVisibility(View.VISIBLE);

                    btnTambah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(judul.getText().toString())) {
                                Toast.makeText(MainActivity.this, "Masukkan Judul Resep!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ResepObject ro = new ResepObject();

                            if (imguri == null) {

                                final AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog1.setMessage("Masukkan Foto");
                                alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE,"Oke",new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog1.dismiss();
                                    }
                                });
                                alertDialog1.show();
                            }else{
                                ro.setJudul(judul.getText().toString());
                                ro.setDeskripsi(deskripsi.getText().toString());
                                ro.setCara(cara.getText().toString());
                                ro.setImg(imguri.toString());

                                createResep(ro);
                                imguri = null;

                                alertDialog.dismiss();
                            }

                        }
                    });
                }

            }
        });

        alertDialog.show();
//
//        // Color Button (BATAL | SIMPAN | UBAH) Alert Dialog
//        // =========================================================================================
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//
//                // BATAL
//                neutralButton.setBackgroundColor(Color.rgb(248 ,212,  24));
//                neutralButton.setTextColor(Color.rgb(30, 30, 30));
//
//
//                // SIMPAN | UBAH
//                positiveButton.setBackgroundColor(Color.rgb(248 ,200,  10));
//                positiveButton.setTextColor(Color.rgb(30, 30, 30));
//            }
//        });




//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (TextUtils.isEmpty(judul.getText().toString())) {
//                    Toast.makeText(MainActivity.this, "Masukkan Judul Resep!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else {
//                    alertDialog.dismiss();
//                }
//
//                ResepObject ro = new ResepObject();
//                ro.setJudul(judul.getText().toString());
//                ro.setDeskripsi(deskripsi.getText().toString());
//                ro.setCara(cara.getText().toString());
//
//
//                if (shouldUpdate){
//                    if (imguri != null){
//                        ro.setImg(imguri.toString());
//                    }
//                    else {
//                        ro.setImg(resep.getImg());
//                    }
//
//                    updateResep(ro, position);
//
//                }else {
//                    if (imguri != null ){
//                        ro.setImg(imguri.toString());
//                    }
//                    else {
//                        ro.setImg(iv_imgItem.toString());
//                    }
//
//                    createResep(ro);
//                }
//
//            }
//        });
    }




    // =============== Intent Crop

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            CropImage.startPickImageActivity(MainActivity.this);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // Kurang
//                imguri  = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 0);
            } else {
                Crop(imageUri);

            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                iv_imgItem.setImageURI(result.getUri());
                imguri = result.getUri();
                img.setVisibility(View.GONE);
            }
        }
    }

    private void Crop(Uri img_Uri){
        CropImage.activity(img_Uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(500, 500)
                .setMultiTouchEnabled(true)
                .setAspectRatio(5, 3)
                .start(MainActivity.this);
    }

}
