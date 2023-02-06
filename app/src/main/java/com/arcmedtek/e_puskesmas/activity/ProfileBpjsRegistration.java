package com.arcmedtek.e_puskesmas.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.fragment.PolyRegistrationFragment;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

public class ProfileBpjsRegistration extends AppCompatActivity {

    TextInputEditText _inetNik, _inetIdBpjs, _inetFaskesLevel, _inetFaskesName;
    LinearLayout _takePicture, _pictureContainer;
    ImageView _pictureResult;
    Button _takePictureAgain, _btnSaveDataBpjs;
    Bitmap captureImage;

    String _nik, _txtPhotoBpjs;

    EPuskesmasDataService _ePuskesmasDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_bpjs_registration);

        _nik = getIntent().getStringExtra("nik");

        _inetNik = findViewById(R.id.inet_nik);
        _inetIdBpjs = findViewById(R.id.inet_id_bpjs);
        _inetFaskesLevel = findViewById(R.id.inet_faskes_level);
        _inetFaskesName = findViewById(R.id.inet_faskes_name);
        _takePicture = findViewById(R.id.take_picture);
        _pictureContainer = findViewById(R.id.picture_container);
        _pictureResult = findViewById(R.id.picture_result);
        _takePictureAgain = findViewById(R.id.take_picture_again);
        _btnSaveDataBpjs = findViewById(R.id.btn_save_data_bpjs);

        _ePuskesmasDataService = new EPuskesmasDataService(this);

        _takePicture.setOnClickListener(v -> {
            takeImage();
        });

        _inetNik.setText(_nik);
        _inetNik.setEnabled(false);

        _btnSaveDataBpjs.setOnClickListener(v -> {
            saveDataBpjs(_inetNik.getText().toString(), _inetIdBpjs.getText().toString(), _inetFaskesLevel.getText().toString(), _inetFaskesName.getText().toString(), _txtPhotoBpjs);
        });
    }

    private void saveDataBpjs(String txtNik, String txtIdBpjs, String txtFaskesLevel, String txtFaskesName, String txtPhotoBpjs) {
        _ePuskesmasDataService.createDataBpjs(txtIdBpjs, txtNik, txtFaskesLevel, txtFaskesName, txtPhotoBpjs, new EPuskesmasDataService.CreateDataBpjs() {
            @Override
            public void onResponse(String message) {
                doneDialog(message);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ProfileBpjsRegistration.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takeImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private Bitmap getResizedBitmap(Bitmap img, int maxSize) {
        int width = img.getWidth();
        int height = img.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(img, width, height, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            _pictureContainer.setVisibility(View.VISIBLE);
            _takePicture.setVisibility(View.GONE);

            captureImage = (Bitmap) data.getExtras().get("data");

            captureImage = getResizedBitmap(captureImage, 1024);
            _pictureResult.setImageBitmap(captureImage);

            _txtPhotoBpjs = getStringImage(captureImage);

            _takePictureAgain.setOnClickListener(v -> takeImage());
        }
    }

    private void doneDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        View doneDialog = LayoutInflater.from(this).inflate(R.layout.custom_done_dialog, findViewById(R.id.confirm_done_dialog));
        builder.setView(doneDialog);

        TextView txtMessage = doneDialog.findViewById(R.id.done_message);
        txtMessage.setText(message);

        final AlertDialog alertDialog = builder.create();

        doneDialog.findViewById(R.id.btn_confirm_done).setOnClickListener(v -> {
            alertDialog.dismiss();

            startActivity(new Intent(ProfileBpjsRegistration.this, DashboardPasien.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}