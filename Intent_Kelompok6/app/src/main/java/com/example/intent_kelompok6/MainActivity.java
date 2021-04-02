package com.example.intent_kelompok6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {
    private ImageView showImg;
    private Button OpenImage;

    //Request Code Digunakan Untuk Menentukan Permintaan dari User
    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALLERY = 002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showImg = findViewById(R.id.showImg);
        OpenImage = findViewById(R.id.open_image);
        OpenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestImage();
            }
        });
    }
    private void setRequestImage(){
        CharSequence[] item = {"Kamera", "Galeri"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Pilih Gambar dari")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Membuka Kamera Untuk Mengambil Gambar
                                EasyImage.openCamera(MainActivity.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                //Membuaka Galeri Untuk Mengambil Gambar
                                EasyImage.openGallery(MainActivity.this, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        request.create();
        request.show();
    }
    //Method Ini Digunakan Untuk Menapatkan Hasil pada Method setRequestImage()
    //Dan Mendapatkan Hasil File Gamba dari Galeri atau Kamera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Mengangani Error pada Gambar
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Mengangani Gambar
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        Glide.with(MainActivity.this)
                                .load(imageFile)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(showImg);
                        break;

                    case REQUEST_CODE_GALLERY:
                        Glide.with(MainActivity.this)
                                .load(imageFile)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(showImg);
                        break;
                }
            }
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Digunakan untuk menghapus foto yang diambil jika ingin dibatalkan
            }
        });
    }
}