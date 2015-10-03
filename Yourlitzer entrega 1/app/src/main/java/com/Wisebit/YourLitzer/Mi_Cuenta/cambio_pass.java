package com.Wisebit.YourLitzer.Mi_Cuenta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Wisebit.YourLitzer.Conexion_BD.Httppostaux;
import com.Wisebit.YourLitzer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cambio_pass extends ActionBarActivity {
    TextView actual,nueva,confirmar;
    Button aceptar,cancelar;
    String user,pass_actual,pass_nueva,aux;

    private ProgressDialog cargando; // MENSAJE DE CARGAR DATOS
    Httppostaux post;
    String IP_Server="www.yourlitzer.com";
    String URL_connect="http://"+IP_Server+"/archivos/cambiar_pass.php";//ruta en donde estan nuestros archivos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pass);

        post = new Httppostaux();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user  = extras.getString("user");//usuario
        }else{
            user="error";
        }

        actual= (EditText)findViewById(R.id.pass_actual);
        nueva= (EditText)findViewById(R.id.pass_nueva);
        confirmar= (EditText)findViewById(R.id.pass_confirmar);
        aceptar=(Button)findViewById(R.id.aceptar);
        cancelar=(Button)findViewById(R.id.cancelar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(actual.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(nueva.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(confirmar.getWindowToken(), 0);

                if(!actual.getText().toString().equals("") && !nueva.getText().toString().equals("") && !confirmar.getText().toString().equals("") ) {
                    if(!actual.getText().toString().equals("") && nueva.getText().toString().equals(confirmar.getText().toString())){
                        new async_cambio_pass().execute(user,actual.getText().toString(),nueva.getText().toString() );
                    }
                    else {
                        mostrar_error("datos mal ingresados");
                    }
                }
                else{
                    if(actual.getText().toString().equals("")){
                        actual.setError("Campo Requerido");
                    }
                    if(nueva.getText().toString().equals("")){
                        nueva.setError("Campo Requerido");
                    }
                    if(confirmar.getText().toString().equals("")){
                        confirmar.setError("Campo Requerido");
                    }
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                data.setData(Uri.parse(""));
                setResult(4, data);
                finish();
            }
        });

    }

    public String cambiar_pass(String username,String actual,String  nueva) {

        ArrayList<NameValuePair> lista_enviar= new ArrayList<NameValuePair>();
        lista_enviar.add(new BasicNameValuePair("usuario", username));
        lista_enviar.add(new BasicNameValuePair("pass_actual", actual));
        lista_enviar.add(new BasicNameValuePair("pass_nueva", nueva));

        JSONObject jdata = post.getserverdata(lista_enviar, URL_connect);

        if (jdata!=null && jdata.length() > 0){
            try {
                if (jdata.getString("Valor").equals("1")) {
                    return "ok";
                }
                else if(jdata.getString("Valor").equals("2")){
                    return "incorrecta";
                }
                else{
                    return "error";
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{  //json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR22");
            return "error";
        }
        return "error";
    }


    class async_cambio_pass extends AsyncTask< String, String ,String> {
        String user;
        protected void onPreExecute() {
            cargando= ProgressDialog.show(com.Wisebit.YourLitzer.Mi_Cuenta.cambio_pass.this, "Actualizando datos", "Espere", true);
        }

        protected String doInBackground(String... params) {
            user=params[0];
            pass_actual=params[1];
            pass_nueva=params[2];
            aux=cambiar_pass(user,pass_actual, pass_nueva);
            return aux;
        }

        protected void onPostExecute(String result) {
            if (result.equals("ok")){
                Intent data = new Intent();
                data.setData(Uri.parse("Cambio exitoso!"));
                setResult(RESULT_OK, data);
                cargando.dismiss();
                finish();
            }
            else if(result.equals("incorrecta")) {
                actual.setText("");
                cargando.dismiss();
                mostrar_error("pass incorrecta");
            }
            else{
                Intent data = new Intent();
                data.setData(Uri.parse("error, intentelo nuevamente!"));
                setResult(3, data);
                cargando.dismiss();
                finish();
            }

        }

    }

    public void mostrar_error(String mensaje){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(com.Wisebit.YourLitzer.Mi_Cuenta.cambio_pass.this,mensaje, Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent data = new Intent();
            data.setData(Uri.parse(""));
            setResult(4, data);
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }

}