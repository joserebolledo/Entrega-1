package com.Wisebit.YourLitzer.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Wisebit.YourLitzer.Login.LoginActivity;
import com.Wisebit.YourLitzer.R;


/**
 * Created by Alexis on 01-10-15.
 */
public class fm1 extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lay_fm1, container, false);
        Button btnskip = (Button) rootView.findViewById(R.id.btnskip);
        btnskip.setOnClickListener(this);
        return rootView;
        }


    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}