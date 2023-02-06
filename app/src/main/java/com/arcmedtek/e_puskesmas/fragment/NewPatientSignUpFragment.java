package com.arcmedtek.e_puskesmas.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.Login;
import com.arcmedtek.e_puskesmas.service.API;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.arcmedtek.e_puskesmas.service.RetroServer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPatientSignUpFragment extends Fragment {

    AutoCompleteTextView _gender;
    TextInputEditText _inetNik, _inetFirstName, _inetMiddleName, _inetLastName, _inetBirthPlace, _inetBirthDate, _inetAddress;
    LinearLayout _takePicture, _pictureContainer;
    ImageView _pictureResult;
    Button _takePictureAgain, _btnSaveDataRegistration;
    Bitmap captureImage;

    DatePickerDialog.OnDateSetListener tgl;
    Calendar myCalendar;
    ArrayAdapter<String> genderAdapter;

    String[] genderItems = {"Pria", "Wanita"};
    String _txtGender, _txtPhotoKtp;

    EPuskesmasDataService _ePuskesmasDataService;

    public NewPatientSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_patient_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _gender = view.findViewById(R.id.choose_gender);
        _takePicture = view.findViewById(R.id.take_picture);
        _pictureContainer = view.findViewById(R.id.picture_container);
        _pictureResult = view.findViewById(R.id.picture_result);
        _takePictureAgain = view.findViewById(R.id.take_picture_again);
        _btnSaveDataRegistration = view.findViewById(R.id.btn_save_data_registration);
        _inetNik = view.findViewById(R.id.inet_nik);
        _inetFirstName = view.findViewById(R.id.inet_first_name);
        _inetMiddleName = view.findViewById(R.id.inet_middle_name);
        _inetLastName = view.findViewById(R.id.inet_last_name);
        _inetBirthPlace = view.findViewById(R.id.inet_birth_place);
        _inetBirthDate = view.findViewById(R.id.inet_birth_date);
        _inetAddress = view.findViewById(R.id.inet_address);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        genderAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_gender, genderItems);
        _gender.setAdapter(genderAdapter);

        _gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _txtGender = parent.getItemAtPosition(position).toString();
            }
        });

        myCalendar = Calendar.getInstance();
        tgl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
                _inetBirthDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        _inetBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getContext(), tgl,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        _inetBirthDate.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), tgl,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        _takePicture.setOnClickListener(v -> {
            takeImage();
        });

        _btnSaveDataRegistration.setOnClickListener(v -> {
            saveDataRegistration(_inetNik.getText().toString(), _inetFirstName.getText().toString(), _inetMiddleName.getText().toString(), _inetLastName.getText().toString(), _txtGender, _inetBirthPlace.getText().toString(), _inetBirthDate.getText().toString(), _inetAddress.getText().toString(), _txtPhotoKtp);
        });
    }

    void takeImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public Bitmap getResizedBitmap(Bitmap img, int maxSize) {
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

            _txtPhotoKtp = getStringImage(captureImage);

            _takePictureAgain.setOnClickListener(v -> {
                takeImage();
            });
        }
    }

    private String convertToDBDate(String txtBirthDate) {
        String inputPattern = "dd MMMM yyyy";
        String outputPattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, new Locale("id", "ID"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(txtBirthDate);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void saveDataRegistration(String txtNik, String txtFirstName, String txtMiddleName, String txtLastName, String txtGender, String txtBirthPlace, String txtBirthDate, String txtAddress, String txtPhotoKtp) {
        _ePuskesmasDataService.createDataPatient(txtNik, txtFirstName, txtMiddleName, txtLastName, txtGender, txtBirthPlace, convertToDBDate(txtBirthDate), txtAddress, txtPhotoKtp, new EPuskesmasDataService.CreateDataPatient() {
            @Override
            public void onResponse(String message) {
                createAccount(txtNik);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

//        Toast.makeText(getContext(), txtGender, Toast.LENGTH_SHORT).show();

//        File imageFile = new File(String.valueOf(captureImage));
//
//        RequestBody reqBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
////        RequestBody reqBodyNik = RequestBody.create(MediaType.parse("text/plain"), txtNik);
////        RequestBody reqBodyFirstName = RequestBody.create(MediaType.parse("text/plain"), txtFirstName);
////        RequestBody reqBodyMiddleName = RequestBody.create(MediaType.parse("text/plain"), txtMiddleName);
////        RequestBody reqBodyLastName = RequestBody.create(MediaType.parse("text/plain"), txtLastName);
////        RequestBody reqBodyGender = RequestBody.create(MediaType.parse("text/plain"), txtGender);
////        RequestBody reqBodyBirthPlace = RequestBody.create(MediaType.parse("text/plain"), txtBirthPlace);
////        RequestBody reqBodyBirthDate = RequestBody.create(MediaType.parse("text/plain"), convertToDBDate(txtBirthDate));
////        RequestBody reqBodyAddress = RequestBody.create(MediaType.parse("text/plain"), txtAddress);
//
//        MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto_ktp", imageFile.getName(), reqBodyImage);
//
//        API api = RetroServer.getInstance().getAPI();
//        Call<ResponseBody> upload = api.createDataPatient(txtNik, txtFirstName, txtMiddleName, txtLastName, txtGender, txtBirthPlace, convertToDBDate(txtBirthDate), txtAddress, partImage);
////        Call<ResponseBody> upload = api.createDataPatient(reqBodyNik, reqBodyFirstName, reqBodyMiddleName, reqBodyLastName, reqBodyGender, reqBodyBirthPlace, reqBodyBirthDate, reqBodyAddress, partImage);
//        upload.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                Toast.makeText(getContext(), "Berhasil tambah data", Toast.LENGTH_SHORT).show();
//                Intent main = new Intent(getContext(), Login.class);
//                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(main);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void createAccount(String txtNik) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        View createAccountDialog = LayoutInflater.from(getContext()).inflate(R.layout.create_account, getView().findViewById(R.id.create_account));
        builder.setView(createAccountDialog);

        TextInputEditText inetNik = createAccountDialog.findViewById(R.id.inet_nik);
        TextInputEditText inetPass = createAccountDialog.findViewById(R.id.inet_pass);
        TextInputEditText inetRetypePass = createAccountDialog.findViewById(R.id.inet_retype_pass);
        TextInputLayout inlayRetypePass = createAccountDialog.findViewById(R.id.inlay_retype_pass);

        inetNik.setEnabled(false);
        inetNik.setText(txtNik);

        final AlertDialog alertDialog = builder.create();

        createAccountDialog.findViewById(R.id.btn_create_account).setOnClickListener(v -> {
            if (!inetRetypePass.getText().toString().equals(inetPass.getText().toString())) {
                inlayRetypePass.setError("Password tidak sama");
                inetRetypePass.setText("");
            } else {
                _ePuskesmasDataService.signUp(txtNik, inetPass.getText().toString(), new EPuskesmasDataService.SignUpListener() {
                    @Override
                    public void onResponse(String message) {
                        doneDialog("Data pasien berhasil teregistrasi");
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
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

            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}