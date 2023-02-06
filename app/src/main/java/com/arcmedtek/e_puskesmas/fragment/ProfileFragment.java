package com.arcmedtek.e_puskesmas.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.DashboardPasien;
import com.arcmedtek.e_puskesmas.activity.EditProfile;
import com.arcmedtek.e_puskesmas.activity.Login;
import com.arcmedtek.e_puskesmas.activity.ProfileBpjsRegistration;
import com.arcmedtek.e_puskesmas.config.SessionManager;
import com.arcmedtek.e_puskesmas.model.BpjsModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileFragment extends Fragment {

    TextView _profileName, _profileNik, _btnAddDataBpjs;
    TextInputEditText _profileBirthPlace, _profileBirthDate, _profileAddress, _profileIdBpjs, _profileFaskesLevel, _profileFaskesName;
    AutoCompleteTextView _profileGender;
    LinearLayout _takePicture, _takePictureBpjs, _pictureBpjsContainer, _pictureContainer, _dataBpjsContainer, _dataBpjsNotFoundContainer;
    ImageView _pictureResult, _pictureBpjsResult, _btnSetting;
//    MenuBuilder menuBuilder;

    String _nik, _firstName, _middleName, _lastName, _gender, _birthPlace, _birthDate, _address, _photoKtp;

    EPuskesmasDataService _ePuskesmasDataService;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        menuBuilder = new MenuBuilder(getContext());

        _profileName = view.findViewById(R.id.profile_name);
        _profileNik = view.findViewById(R.id.profile_nik);
        _profileGender = view.findViewById(R.id.profile_inet_gender);
        _profileBirthPlace = view.findViewById(R.id.profile_inet_birth_place);
        _profileBirthDate = view.findViewById(R.id.profile_inet_birth_date);
        _profileAddress = view.findViewById(R.id.profile_inet_address);
        _profileIdBpjs = view.findViewById(R.id.profile_inet_id_bpjs);
        _profileFaskesLevel = view.findViewById(R.id.profile_inet_faskes_level);
        _profileFaskesName = view.findViewById(R.id.profile_inet_faskes_name);
        _takePicture = view.findViewById(R.id.take_picture);
        _takePictureBpjs = view.findViewById(R.id.take_picture_bpjs);
        _pictureContainer = view.findViewById(R.id.picture_container);
        _pictureBpjsContainer = view.findViewById(R.id.picture_bpjs_container);
        _pictureResult = view.findViewById(R.id.picture_result);
        _pictureBpjsResult = view.findViewById(R.id.picture_bpjs_result);
        _btnSetting = view.findViewById(R.id.btn_setting);
        _dataBpjsContainer = view.findViewById(R.id.data_bpjs_container);
        _dataBpjsNotFoundContainer = view.findViewById(R.id.data_bpjs_not_found_container);
        _btnAddDataBpjs = view.findViewById(R.id.btn_add_data_bpjs);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        if (getArguments() != null) {
            _nik = getArguments().getString("NIK");
            _firstName = getArguments().getString("First_Name");
            _middleName = getArguments().getString("Middle_Name");
            _lastName = getArguments().getString("Last_Name");
            _gender = getArguments().getString("Gender");
            _birthPlace = getArguments().getString("Birth_Place");
            _birthDate = getArguments().getString("Birth_Date");
            _address = getArguments().getString("Address");
            _photoKtp = getArguments().getString("Photo_KTP");
        }

        showProfile(_nik, _firstName, _middleName, _lastName, _gender, _birthPlace, _birthDate, _address, _photoKtp);

        profileSetting(_nik, _firstName, _middleName, _lastName, _gender, _birthPlace, _birthDate, _address, _photoKtp);

        _btnAddDataBpjs.setOnClickListener(v -> {
            bpjsRegistration();
        });
    }

    private void bpjsRegistration() {
        Intent intent = new Intent(getContext(), ProfileBpjsRegistration.class);
        intent.putExtra("nik", _nik);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    private void profileSetting(String nik, String firstName, String middleName, String lastName, String gender, String birthPlace, String birthDate, String address, String photoKtp) {
        _btnSetting.setOnClickListener(v -> {
            PopupMenu settingMenu = new PopupMenu(getContext(), _btnSetting);
            settingMenu.getMenuInflater().inflate(R.menu.popup_menu_setting_patient_profile, settingMenu.getMenu());

            settingMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.edit_profile) {
                        editProfile(nik, firstName, middleName, lastName, gender, birthPlace, birthDate, address, photoKtp);
                        return true;
                    } else if (menuItem.getItemId() == R.id.logout) {
                        _ePuskesmasDataService.logout(new EPuskesmasDataService.LogoutListener() {
                            @Override
                            public void onResponse(String message) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
                                View dialog = LayoutInflater.from(getContext()).inflate(R.layout.confirm_logout_dialog, getView().findViewById(R.id.confirm_logout_dialog));
                                builder.setView(dialog);

                                final AlertDialog alertDialog = builder.create();

                                dialog.findViewById(R.id.logout_yes).setOnClickListener(v -> {
                                    alertDialog.dismiss();
                                    SessionManager sessionManager = new SessionManager(getContext());
                                    sessionManager.removeSession();

                                    Intent intent = new Intent(getContext(), Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                });

                                dialog.findViewById(R.id.logout_no).setOnClickListener(v -> alertDialog.dismiss());

                                if (alertDialog.getWindow() != null) {
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                }

                                alertDialog.show();
                            }

                            @Override
                            public void onError(String message) {

                            }
                        });
                    }
                    return false;
                }
            });
            settingMenu.show();
        });
    }

    private void editProfile(String nik, String firstName, String middleName, String lastName, String gender, String birthPlace, String birthDate, String address, String photoKtp) {
//        Intent intent = new Intent(this, EditProfile.class);
//        intent.
//        editTextDisable(true);
//        _btnSaveEditProfile.setVisibility(View.VISIBLE);
//        _btnCancelEditProfile.setVisibility(View.VISIBLE);
//        _btnSetting.setVisibility(View.GONE);
//        _btnBack.setVisibility(View.GONE);
//
//        _btnCancelEditProfile.setOnClickListener(v -> {
//            getActivity().getSupportFragmentManager().beginTransaction().detach(this).commit();
//            getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitNow();
//        });
    }

    private void showProfile(String nik, String firstName, String middleName, String lastName, String gender, String birthPlace, String birthDate, String address, String photoKtp) {
        String fullname = "";
        if (middleName.equals("")) {
            fullname = firstName + " " + lastName;
        } else {
            fullname = firstName + " " + middleName + " " + lastName;
        }

        String gen = "";
        if (gender.equals("1")) {
            gen = "Pria";
        } else if (gender.equals("0")) {
            gen = "Wanita";
        }

        _takePicture.setVisibility(View.GONE);
        _pictureContainer.setVisibility(View.VISIBLE);

        _profileNik.setText(nik);
        _profileName.setText(fullname);
        _profileGender.setText(gen);
        _profileBirthPlace.setText(birthPlace);
        _profileBirthDate.setText(convertToAppDate(birthDate));
        _profileAddress.setText(address);
//        Glide.with(ProfileFragment.this).load(photoKtp).into(_pictureResult);
        Picasso.get().load(photoKtp).into(_pictureResult);
        editTextDisable(false);

        _ePuskesmasDataService.getPatientBpjs(new EPuskesmasDataService.GetPatientBpjs() {
            @Override
            public void onResponse(ArrayList<BpjsModel> dataPatientBpjs) {
                if (!dataPatientBpjs.isEmpty()) {
                    _dataBpjsContainer.setVisibility(View.VISIBLE);
                    _dataBpjsNotFoundContainer.setVisibility(View.GONE);
                    _takePicture.setVisibility(View.GONE);
                    _pictureBpjsContainer.setVisibility(View.VISIBLE);

                    _profileIdBpjs.setText(dataPatientBpjs.get(0).getIdBpjs());
                    _profileFaskesLevel.setText(dataPatientBpjs.get(0).getFaksesLevel());
                    _profileFaskesName.setText(dataPatientBpjs.get(0).getFaskesLevelName());
                    Picasso.get().load(dataPatientBpjs.get(0).getPhotoBpjs()).into(_pictureBpjsResult);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editTextDisable(Boolean disable) {
        _profileGender.setEnabled(disable);
        _profileBirthPlace.setEnabled(disable);
        _profileBirthDate.setEnabled(disable);
        _profileAddress.setEnabled(disable);
        _profileIdBpjs.setEnabled(disable);
        _profileFaskesLevel.setEnabled(disable);
        _profileFaskesName.setEnabled(disable);
    }

    private String convertToDBDate(String txtBirthDate) {
        String inputPattern = "dd MMMM yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

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

    private String convertToAppDate(String txtBirthDate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

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
}