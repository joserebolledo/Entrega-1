package com.Wisebit.YourLitzer.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.Wisebit.YourLitzer.Login.LoginActivity;
import com.Wisebit.YourLitzer.R;


public class fm3 extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_fm3, container, false);
        ImageButton imgbtn = (ImageButton) rootView.findViewById(R.id.imgbtn);
        imgbtn.setOnClickListener(this);
        return rootView;
    }
    public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        }


