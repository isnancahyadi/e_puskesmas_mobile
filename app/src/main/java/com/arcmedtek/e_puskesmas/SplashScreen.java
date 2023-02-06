package com.arcmedtek.e_puskesmas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.activity.DashboardPasien;
import com.arcmedtek.e_puskesmas.activity.Login;
import com.arcmedtek.e_puskesmas.config.SessionManager;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        int TIME_SPLASH = 3000;
        new Handler().postDelayed(this::checkSession, TIME_SPLASH);
    }

    private void checkSession() {
        SessionManager sessionManager = new SessionManager(SplashScreen.this);
        String privilege = sessionManager.getSession();

        if (privilege == null) {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if (privilege.equals("2")) {
                Intent intent = new Intent(SplashScreen.this, DashboardPasien.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}