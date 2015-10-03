package com.Wisebit.YourLitzer.Secciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Wisebit.YourLitzer.Login.LoginActivity;
import com.Wisebit.YourLitzer.Login.configuracion;
import com.Wisebit.YourLitzer.R;

/**
 * Created by Alexis on 17-08-15.
 */
public class SecHome extends android.support.v4.app.Fragment implements View.OnClickListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_home, container, false);
        Button btn_logoff = (Button) rootView.findViewById(R.id.btn_logoff);
        btn_logoff.setOnClickListener(this);
        return rootView;
    }


    public void onClick(View v) {
        configuracion.borrar();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}
