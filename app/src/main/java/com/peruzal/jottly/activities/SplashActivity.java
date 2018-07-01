package com.peruzal.jottly.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.peruzal.jottly.R;

public class SplashActivity extends AppCompatActivity {

    private static final long DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(() -> {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if (account == null){
                startActivity(new Intent(this,LoginActivity.class));
            }else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        }).start();
    }
}
