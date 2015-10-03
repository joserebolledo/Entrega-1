package com.Wisebit.YourLitzer.Mi_Cuenta;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class crear_cuenta extends ActionBarActivity {
    EditText editUsuario,editNombre,editApellido,editMail,editFecha,editPass,editConfirmar;
    Button aceptar,facebook;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String susuario,snombre,sapellido,sfecha,spass,smail;

    private ProgressDialog cargando; // MENSAJE DE CARGAR DATOS
    Httppostaux post;
    String IP_Server="www.yourlitzer.com";
    String URL_connect="http://"+IP_Server+"/archivos/crear_cuenta.php";//ruta en donde estan nuestros archivos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        post = new Httppostaux();
        editUsuario= (EditText)findViewById(R.id.editUser);
        editNombre= (EditText)findViewById(R.id.editNombre);
        editApellido= (EditText)findViewById(R.id.editApellido);
        editMail=(EditText)findViewById(R.id.editMail);
        editFecha= (EditText)findViewById(R.id.editFecha);
        editFecha.setKeyListener(null);
        editPass= (EditText)findViewById(R.id.editPass);
        editConfirmar= (EditText)findViewById(R.id.editConfirmar);
        aceptar=(Button) findViewById(R.id.aceptar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!editUsuario.getText().toString().equals("") && !editNombre.getText().toString().equals("") && !editApellido.getText().toString().equals("") && !editMail.getText().toString().equals("") && !editFecha.getText().toString().equals("") && !editPass.getText().toString().equals("") && !editConfirmar.getText().toString().equals("")) {
                    if(editMail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        if(editPass.getText().toString().length()<6){
                            editPass.setError("El pass debe contener 6 caracteres como min");
                        }
                        else if (editPass.getText().toString().equals(editConfirmar.getText().toString())) {
                            new async_Crear().execute(editUsuario.getText().toString(), editNombre.getText().toString(), editApellido.getText().toString(), editMail.getText().toString(), editFecha.getText().toString(), editPass.getText().toString());
                        }
                        else{
                            editConfirmar.setError("Pass no coincide");
                        }
                    }
                    else{
                        editMail.setError("mail invalido");
                    }
                }
                else{
                    if(editUsuario.getText().toString().equals("")){
                        editUsuario.setError("Campo Requerido");
                    }
                    if(editNombre.getText().toString().equals("")){
                        editNombre.setError("Campo Requerido");
                    }
                    if(editApellido.getText().toString().equals("")){
                        editApellido.setError("Campo Requerido");
                    }
                    if(editMail.getText().toString().equals("")){
                        editMail.setError("Campo Requerido");
                    }
                    if(editFecha.getText().toString().equals("")){
                        editFecha.setError("Campo Requerido");
                    }
                    if(editPass.getText().toString().equals("")){
                        editPass.setError("Campo Requerido");
                    }
                    if(editConfirmar.getText().toString().equals("")){
                        editConfirmar.setError("Campo Requerido");
                    }
                }
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

        editFecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(crear_cuenta.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        editFecha.setText(sdf.format(myCalendar.getTime()));
        editFecha.setError(null);
    }

    public String Crear(String username,String nombre,String apellido,String mail,String fecha,String pass) {
        String aux;
        ArrayList<NameValuePair> lista_enviar= new ArrayList<NameValuePair>();
        lista_enviar.add(new BasicNameValuePair("usuario", username));
        lista_enviar.add(new BasicNameValuePair("nombre", nombre));
        lista_enviar.add(new BasicNameValuePair("apellido", apellido));
        lista_enviar.add(new BasicNameValuePair("mail", mail));
        lista_enviar.add(new BasicNameValuePair("fecha", fecha));
        lista_enviar.add(new BasicNameValuePair("pass", pass));

        JSONObject jdata = post.getserverdata(lista_enviar, URL_connect);

        if (jdata!=null && jdata.length() > 0){
            try {
                aux=jdata.getString("Valor");
                return(aux);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{  //json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR22");
            return "0";
        }
        return "0";
    }

    class async_Crear extends AsyncTask< String, String ,String> {
        protected void onPreExecute() {
            cargando= ProgressDialog.show(com.Wisebit.YourLitzer.Mi_Cuenta.crear_cuenta.this, "Creando perfil", "Espere", true);
        }

        protected String doInBackground(String... params) {
            susuario=params[0];
            snombre=params[1];
            sapellido=params[2];
            smail=params[3];
            sfecha=params[4];
            spass=params[5];
            return Crear(susuario, snombre, sapellido, smail, sfecha, spass);
        }

        protected void onPostExecute(String result) {
            if (result.equals("1")){
                cargando.dismiss();
                mostrar_error("listoco");
            }
            else if(result.equals("2")){
                cargando.dismiss();
                mostrar_error("mail repetido");
            }
            else if(result.equals("3")){
                cargando.dismiss();
                mostrar_error("usuario repetido");
            }
            else{
                cargando.dismiss();
                mostrar_error("mala vola");
            }
        }
    }

    public void mostrar_error(String mensaje){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(com.Wisebit.YourLitzer.Mi_Cuenta.crear_cuenta.this, mensaje, Toast.LENGTH_SHORT);
        toast1.show();
    }

}