package com.example.frenbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Onboarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dots;
    SliderAdapter sliderAdapter;
    TextView[] dot;
    Button startbtn;
    int curpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.slider);
        dots = findViewById(R.id.dots);
        sliderAdapter= new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        adddots(0);
        viewPager.addOnPageChangeListener(changeListener);
        startbtn=findViewById(R.id.startbtn);
    }
    public void skip(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void next(View view){

        viewPager.setCurrentItem(curpos+1);

    }

    private void adddots(int position){

        dot = new TextView[4];
        dots.removeAllViews();
        for(int i=0;i<dot.length;i++){
            dot[i] =new TextView(this);
            dot[i].setText(Html.fromHtml("&#8226"));
            dot[i].setTextSize(35);

            dots.addView(dot[i]);
        }
        if(dot.length>0){
            dot[position].setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
    }

    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adddots(position);
            curpos=position;

            if(position==0){

                startbtn.setVisibility(View.INVISIBLE);

            } else if (position==1) {
                startbtn.setVisibility(View.INVISIBLE);

            } else if (position==2) {
                startbtn.setVisibility(View.INVISIBLE);

            }
            else{
                startbtn.setVisibility(View.VISIBLE);

            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}