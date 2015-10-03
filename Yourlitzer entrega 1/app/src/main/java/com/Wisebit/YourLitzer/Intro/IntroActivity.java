package com.Wisebit.YourLitzer.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.Wisebit.YourLitzer.Login.LoginActivity;
import com.Wisebit.YourLitzer.Login.configuracion;
import com.Wisebit.YourLitzer.R;
import com.viewpagerindicator.CirclePageIndicator;


public class IntroActivity extends FragmentActivity {
    public static ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        PagerAdapater padapter = new PagerAdapater(getSupportFragmentManager());
        viewpager =(ViewPager)findViewById(R.id.pager);
        viewpager.setAdapter(padapter);
        CirclePageIndicator indicator =(CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        final float density=getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        indicator.setFillColor(0xFF2EFF20);
        indicator.setStrokeColor(0xFF2EFF20);
        final configuracion conf = new configuracion(this);

        if (conf.getUserIntro() != null){
            Intent i=new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(i);

            //user.setText(conf.getUserEmail());
        }
        conf.setUserIntro("si");
    }

}