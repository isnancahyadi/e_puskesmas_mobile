package com.arcmedtek.e_puskesmas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.arcmedtek.e_puskesmas.R;

public class EditProfile extends AppCompatActivity {

    ImageView _btnCancelEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        _btnCancelEditProfile = findViewById(R.id.btn_cancel_edit_profile);

        _btnCancelEditProfile.setOnClickListener(v -> onBackPressed());
    }
}