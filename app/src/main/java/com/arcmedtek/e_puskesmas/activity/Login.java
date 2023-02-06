package com.arcmedtek.e_puskesmas.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextView _btnSignUp;
    TextInputEditText _inetNik, _inetPass;
    ProgressBar _loadingLogin;
    LinearLayout _signUpContainer;
    Button _btnLogin;

    EPuskesmasDataService _ePuskesmasDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _btnSignUp = findViewById(R.id.btn_sign_up);
        _inetNik = findViewById(R.id.inet_nik);
        _inetPass = findViewById(R.id.inet_pass);
        _btnLogin = findViewById(R.id.btn_login);
        _loadingLogin = findViewById(R.id.loading_login);
        _signUpContainer = findViewById(R.id.sign_up_container);

        _ePuskesmasDataService = new EPuskesmasDataService(Login.this);

        _btnLogin.setOnClickListener(v -> {
            String mUsername = String.valueOf(_inetNik.getText());
            String mPassword = String.valueOf(_inetPass.getText());

            if (!mUsername.isEmpty() && !mPassword.isEmpty()) {
                isLogin(mUsername, mPassword);
            } else {
                if (mUsername.isEmpty() && !mPassword.isEmpty()) {
                    _inetNik.setError("Masukkan NIK/No.KTP");
                } else if (!mUsername.isEmpty()) {
                    _inetPass.setError("Masukkan Password");
                } else {
                    _inetNik.setError("Masukkan NIK/No.KTP");
                    _inetPass.setError("Masukkan Password");
                }
            }
        });

        _btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        });
    }

    private void isLogin(String mUsername, String mPassword) {
        _loadingLogin.setVisibility(View.VISIBLE);
        _btnLogin.setVisibility(View.GONE);
        _signUpContainer.setVisibility(View.GONE);

        _ePuskesmasDataService.logIn(mUsername, mPassword, new EPuskesmasDataService.LoginListener() {
            @Override
            public void onResponse(String privilege) {
                if (privilege.equals("2")) {
                    moveToDashboardpasien();
                }
                _loadingLogin.setVisibility(View.GONE);
                _btnLogin.setVisibility(View.VISIBLE);
                _signUpContainer.setVisibility(View.VISIBLE);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(String message) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AlertDialogStyle);
                View warningDialog = LayoutInflater.from(Login.this).inflate(R.layout.custom_warning_dialog, findViewById(R.id.confirm_warning_dialog));
                View notFoundDialog = LayoutInflater.from(Login.this).inflate(R.layout.custom_404_dialog, findViewById(R.id.confirm_404_dialog));
                View forbiddenDialog = LayoutInflater.from(Login.this).inflate(R.layout.custom_forbidden_dialog, findViewById(R.id.confirm_forbidden_dialog));

                TextView txtMessage;

                switch (message) {
                    case "400":
                        builder.setView(warningDialog);
                        txtMessage = warningDialog.findViewById(R.id.warning_message);
                        txtMessage.setText("NIK/No.KTP atau password salah");
                        break;
                    case "404":
                        builder.setView(notFoundDialog);
                        txtMessage = notFoundDialog.findViewById(R.id.notfound_message);
                        txtMessage.setText("NIK/No.KTP tidak ditemukan");
                        break;
                    default:
                        builder.setView(forbiddenDialog);
                        txtMessage = forbiddenDialog.findViewById(R.id.forbidden_message);
                        txtMessage.setText("Terjadi kesalahan sistem");
                        break;
                }

                final AlertDialog alertDialog = builder.create();

                warningDialog.findViewById(R.id.btn_confirm_warning).setOnClickListener(v -> alertDialog.dismiss());

                notFoundDialog.findViewById(R.id.btn_confirm_404).setOnClickListener(v -> alertDialog.dismiss());

                forbiddenDialog.findViewById(R.id.btn_confirm_forbidden).setOnClickListener(v -> alertDialog.dismiss());

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }

                alertDialog.show();

                _loadingLogin.setVisibility(View.GONE);
                _btnLogin.setVisibility(View.VISIBLE);
                _signUpContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void moveToDashboardpasien() {
        Intent intent = new Intent(Login.this, DashboardPasien.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}