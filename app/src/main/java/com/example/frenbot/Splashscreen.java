package com.example.frenbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import kotlin.Pair;

public class Splashscreen extends AppCompatActivity {
    TextView greet,greet2;
    LottieAnimationView lottiesplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        greet=findViewById(R.id.greet);
        greet2=findViewById(R.id.greet2);
        lottiesplash=findViewById(R.id.lottiesplash);

        greet.setTranslationY(0);
        greet2.setTranslationY(0);
        float v=0;
        greet.setAlpha(v);
        greet2.setAlpha(v);

        greet.animate().translationY(200).alpha(1).setDuration(1000).setStartDelay(400).start();
        greet2.animate().translationY(-200).alpha(1).setDuration(1000).setStartDelay(400).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//                finish();

                //Pair pair1=new Pair<View,String>(blink,"logoimg");
                //Pair pair2=new Pair<View,String>(greet2,"logotxt");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Splashscreen.this, android.util.Pair.create(lottiesplash,"logoimg"),android.util.Pair.create(greet2,"logotxt"));
                startActivity(intent,options.toBundle());
                finish();
                //blink.setVisibility(View.VISIBLE);
            }
        },3000);
    }
}