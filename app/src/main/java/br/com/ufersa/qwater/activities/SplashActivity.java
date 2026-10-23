package br.com.ufersa.qwater.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

/**
 * Tela de abertura, que exibe a marca por dois segundos e abre a tela
 * principal.
 *
 * A marca é desenhada pelo mecanismo de splash do sistema, mantido na tela até
 * o prazo de exibição terminar.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        final long deadline = SystemClock.uptimeMillis() + SPLASH_DISPLAY_MILLIS;
        splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() {
            @Override
            public boolean shouldKeepOnScreen() {
                return SystemClock.uptimeMillis() < deadline;
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainScreen();
            }
        }, SPLASH_DISPLAY_MILLIS);
    }

    private void openMainScreen() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
