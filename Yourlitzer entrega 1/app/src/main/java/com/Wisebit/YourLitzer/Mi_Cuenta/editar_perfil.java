package com.Wisebit.YourLitzer.Mi_Cuenta;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Wisebit.YourLitzer.Conexion_BD.Httppostaux;
import com.Wisebit.YourLitzer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class editar_perfil extends ActionBarActivity {
    TextView nombre,apellido,fecha;
    Button aceptar,cancelar;
    String user,nombre_nuevo,apellido_nuevo,fecha_nueva;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    private ProgressDialog cargando; // MENSAJE DE CARGAR DATOS
    Httppostaux post;
    String IP_Server="www.yourlitzer.com";
    String URL_connect="http://"+IP_Server+"/archivos/actualizar_perfil.php";//ruta en donde estan nuestros archivos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);


        post = new Httppostaux();
        nombre= (EditText)findViewById(R.id.editNombre);
        apellido= (EditText)findViewById(R.id.editApellido);
        fecha= (EditText)findViewById(R.id.editFecha);
        fecha.setKeyListener(null);
        aceptar=(Button) findViewById(R.id.aceptar);
        cancelar=(Button)findViewById(R.id.cancelar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user  = extras.getString("user");//usuario
            nombre.setText(extras.getString("nombre"));
            apellido.setText(extras.getString("apellido"));
            fecha.setText(extras.getString("fecha"));
        }else{
            user="error";
        }

        aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(nombre.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(apellido.getWindowToken(), 0);

                if(!nombre.getText().toString().equals("") && !apellido.getText().toString().equals("") && !fecha.getText().toString().equals("") ) {
                    new async_Actualizar().execute(user, nombre.getText().toString(), apellido.getText().toString(), fecha.getText().toString());
                }
                else{
                    if(nombre.getText().toString().equals("")){
                        nombre.setError("Campo Requerido");
                    }
                    if(apellido.getText().toString().equals("")){
                        apellido.setError("Campo Requerido");
                    }
                    if(fecha.getText().toString().equals("")){
                        fecha.setError("Campo Requerido");
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

        myCalendar= Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        fecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(editar_perfil.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        fecha.setText(sdf.format(myCalendar.getTime()));
        fecha.setError(null);
    }

    public boolean actualizar_datos(String username,String nombre,String apellido,String fecha) {

        ArrayList<NameValuePair> lista_enviar= new ArrayList<NameValuePair>();
        lista_enviar.add(new BasicNameValuePair("usuario", username));
        lista_enviar.add(new BasicNameValuePair("nombre", nombre));
        lista_enviar.add(new BasicNameValuePair("apellido", apellido));
        lista_enviar.add(new BasicNameValuePair("fecha", fecha));

        JSONObject jdata = post.getserverdata(lista_enviar, URL_connect);

        if (jdata!=null && jdata.length() > 0){
            try {
                if (jdata.getString("Valor").equals("1")) {
                    return true;
                }
                else{
                    return false;
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{  //json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR22");
            return false;
        }
        return false;
    }


    class async_Actualizar extends AsyncTask< String, String ,String> {
        String user;
        protected void onPreExecute() {
            cargando= ProgressDialog.show(com.Wisebit.YourLitzer.Mi_Cuenta.editar_perfil.this, "Actualizando datos", "Espere", true);
        }

        protected String doInBackground(String... params) {
            user=params[0];
            nombre_nuevo=params[1];
            apellido_nuevo=params[2];
            fecha_nueva=params[3];
            if(actualizar_datos(user,nombre_nuevo,apellido_nuevo,fecha_nueva)) {
                return "ok";
            }
            else{
                return "fail";
            }

        }

        protected void onPostExecute(String result) {
            if (result.equals("ok")){
                Intent data = new Intent();
                data.putExtra("nombre_nuevo", nombre_nuevo);
                data.putExtra("apellido_nuevo", apellido_nuevo);
                data.putExtra("fecha_nueva", fecha_nueva);
                data.setData(Uri.parse("Datos Actualizados!"));
                setResult(RESULT_OK, data);
                cargando.dismiss();
                finish();
            }else{
                Intent data = new Intent();
                data.setData(Uri.parse("error, intentelo nuevamente!"));
                setResult(RESULT_CANCELED, data);
                cargando.dismiss();
                finish();
            }

        }

    }

    public void mostrar_error(String mensaje){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(com.Wisebit.YourLitzer.Mi_Cuenta.editar_perfil.this,mensaje, Toast.LENGTH_SHORT);
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