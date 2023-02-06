package com.arcmedtek.e_puskesmas.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.Login;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

public class BpjsRegistrationFragment extends Fragment {

    TextInputEditText _inetNik, _inetIdBpjs, _inetFaskesLevel, _inetFaskesName;
    LinearLayout _takePicture, _pictureContainer;
    ImageView _pictureResult;
    Button _takePictureAgain, _btnSaveDataBpjs;
    Bitmap captureImage;

    String _nik, _polyName, _patientType, _txtPhotoBpjs;

    EPuskesmasDataService _ePuskesmasDataService;

    public BpjsRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bpjs_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            _nik = getArguments().getString("nik");
            _polyName = getArguments().getString("Poly_Name");
            _patientType = getArguments().getString("Patient_Type");
        }

        _inetNik = view.findViewById(R.id.inet_nik);
        _inetIdBpjs = view.findViewById(R.id.inet_id_bpjs);
        _inetFaskesLevel = view.findViewById(R.id.inet_faskes_level);
        _inetFaskesName = view.findViewById(R.id.inet_faskes_name);
        _takePicture = view.findViewById(R.id.take_picture);
        _pictureContainer = view.findViewById(R.id.picture_container);
        _pictureResult = view.findViewById(R.id.picture_result);
        _takePictureAgain = view.findViewById(R.id.take_picture_again);
        _btnSaveDataBpjs = view.findViewById(R.id.btn_save_data_bpjs);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

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
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doneDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        View doneDialog = LayoutInflater.from(getContext()).inflate(R.layout.custom_done_dialog, getView().findViewById(R.id.confirm_done_dialog));
        builder.setView(doneDialog);

        TextView txtMessage = doneDialog.findViewById(R.id.done_message);
        txtMessage.setText(message);

        final AlertDialog alertDialog = builder.create();

        doneDialog.findViewById(R.id.btn_confirm_done).setOnClickListener(v -> {
            alertDialog.dismiss();

            Bundle bundle = new Bundle();
            bundle.putString("Poly_Name", _polyName);
            bundle.putString("Patient_Type", _patientType);

            Fragment fragment = new PolyRegistrationFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            fragment.setArguments(bundle);
            transaction.replace(R.id.fragment_poly_registration, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void takeImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
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
}