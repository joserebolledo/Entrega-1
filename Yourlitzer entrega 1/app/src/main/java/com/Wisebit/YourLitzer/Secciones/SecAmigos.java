package com.Wisebit.YourLitzer.Secciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Wisebit.YourLitzer.R;

/**
 * Created by Alexis on 24-08-15.
 */
public class SecAmigos extends android.support.v4.app.Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_amigos, container, false);
        return rootView;
    }
}