package com.Wisebit.YourLitzer.Secciones;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Wisebit.YourLitzer.Conexion_BD.Httppostaux;
import com.Wisebit.YourLitzer.Mi_Cuenta.cambio_pass;
import com.Wisebit.YourLitzer.Mi_Cuenta.crear_cuenta;
import com.Wisebit.YourLitzer.Mi_Cuenta.editar_perfil;
import com.Wisebit.YourLitzer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecCuenta extends android.support.v4.app.Fragment {
    TextView mail,nombre,apellido,fecha;
    String nick,mail_actual,nombre_actual,apellido_actual,fecha_actual;
    Button editar,cambiar_pass,eliminar_cuenta;
    private ProgressDialog cargando; // MENSAJE DE CARGAR DATOS

    Httppostaux post;
    String IP_Server = "www.yourlitzer.com";
    String URL_connect = "http://" + IP_Server + "/archivos/mostrar.php";//ruta en donde estan nuestros archivos

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_cuenta, container, false);
        nick = "mauro";
        nombre = (TextView) rootView.findViewById(R.id.textNombre);
        apellido = (TextView) rootView.findViewById(R.id.textApellido);
        fecha = (TextView) rootView.findViewById(R.id.textFecha);
        mail = (TextView) rootView.findViewById(R.id.textMail);
        editar=(Button)rootView.findViewById(R.id.editar);
        cambiar_pass=(Button)rootView.findViewById(R.id.cambio_pass);
        eliminar_cuenta=(Button)rootView.findViewById(R.id.eliminar);

        post = new Httppostaux(); //CREA UNA INSTANCIA A LA CLASE PARA LA CONEXION

        new asynclogin().execute(nick); //LLAMA A FUNCION PARA CARGAR LOS DATOS

        editar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent e = new Intent(getActivity(), editar_perfil.class);
                e.putExtra("user", nick);
                e.putExtra("nombre", nombre_actual);
                e.putExtra("apellido", apellido_actual);
                e.putExtra("fecha", fecha_actual);
                startActivityForResult(e, 111);
            }
        });
        cambiar_pass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent c = new Intent(getActivity(),cambio_pass.class);
                c.putExtra("user", nick);
                startActivityForResult(c, 222);
            }
        });
        eliminar_cuenta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent d = new Intent(getActivity(),crear_cuenta.class);
                startActivity(d);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((requestCode == 111) && (resultCode == Activity.RESULT_OK)){
            nombre_actual=data.getExtras().getString("nombre_nuevo");
            nombre.setText(nombre_actual);
            apellido_actual=data.getExtras().getString("apellido_nuevo");
            apellido.setText(apellido_actual);
            fecha_actual=data.getExtras().getString("fecha_nueva");
            fecha.setText(fecha_actual);
            mostrar_error(data.getDataString());
        }
        else if ((requestCode == 222) && (resultCode == Activity.RESULT_OK)){
            mostrar_error(data.getDataString());
        }
        else if (resultCode == 4){
        }
        else{
            mostrar_error(data.getDataString());
        }
    }

    public boolean obtener_datos(String username) {

        ArrayList<NameValuePair> lista_enviar = new ArrayList<NameValuePair>();
        lista_enviar.add(new BasicNameValuePair("usuario", username));

        JSONObject jdata = post.getserverdata(lista_enviar, URL_connect);

        if (jdata != null && jdata.length() > 0) {

            try {
                if (!jdata.isNull("Nombre") && jdata.getString("Nombre").equals("0")) {
                    return false;
                } else {
                    if (jdata.isNull("Nombre")) { //NOMBRE
                        nombre_actual = "Actualizar Nombre";
                    } else {
                        nombre_actual = jdata.getString("Nombre");
                    }
                    if (jdata.isNull("Apellido")) { //APELLIDO
                        apellido_actual = "Actualizar Apellido";
                    } else {
                        apellido_actual = jdata.getString("Apellido");
                    }
                    if (jdata.isNull("Fecha")) { //FECHA
                        fecha_actual = "Actualizar Fecha de nacimiento";
                    } else {
                        fecha_actual = jdata.getString("Fecha");
                    }
                    if (jdata.isNull("Mail")) { //EMAIL
                        mail_actual = "Actualizar Email";
                    } else {
                        mail_actual = jdata.getString("Mail");
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {    //json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR");
            return false;
        }
        return true;
    }

    class asynclogin extends AsyncTask<String, String, String> {
        String user;

        protected void onPreExecute() {
            cargando = ProgressDialog.show(getActivity(), "Obteniendo datos", "Espere", true);
        }


        protected String doInBackground(String... params) {
            user = params[0];
            if (obtener_datos(user)) {
                return "ok";
            } else {
                return "fail";
            }

        }

        protected void onPostExecute(String result) {
            if (result.equals("ok")) {
                cargando.dismiss();
                nombre.setText(nombre_actual);
                apellido.setText(apellido_actual);
                fecha.setText(fecha_actual);
                mail.setText(mail_actual);

            } else {
                cargando.dismiss();
                mostrar_error("No se ha podido mostrar los datos");
            }

        }

    }

    //vibra y muestra un Toast
    public void mostrar_error(String mensaje){
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
        Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();
    }
}
