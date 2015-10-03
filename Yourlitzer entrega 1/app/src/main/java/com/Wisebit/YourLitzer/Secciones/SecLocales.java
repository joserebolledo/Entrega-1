package com.Wisebit.YourLitzer.Secciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Wisebit.YourLitzer.R;

/**
 * Created by Alexis on 19-08-15.
 */
public class SecLocales extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_locales, container, false);
        return rootView;
    }
}
