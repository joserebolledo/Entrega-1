package com.Wisebit.YourLitzer.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Wisebit.YourLitzer.Main;
import com.Wisebit.YourLitzer.Mi_Cuenta.crear_cuenta;
import com.Wisebit.YourLitzer.R;
import com.Wisebit.YourLitzer.Recuperar_clave.RecuperarClave;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.Wisebit.YourLitzer.Recuperar_clave.RecuperarClave;
//import com.Wisebit.YourLitzer.Registro.Registro;


public class LoginActivity extends Activity {
    /** Called when the activity is first created. */

    EditText user;
    EditText pass;
    Button blogin, btn_crearcuenta, btn_recuperarpass;
    TextView registrar, recuperar;
    Httppostaux post;
    // String URL_connect="http://www.scandroidtest.site90.com/acces.php";
    String IP_Server="www.yourlitzer.com";//IP DE NUESTRO PC
    String URL_connect="http://"+IP_Server+"/archivos/acces.php";//ruta en donde estan nuestros archivos

    boolean result_back;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        post=new Httppostaux();
        String usuario2;

        user= (EditText) findViewById(R.id.edusuario);
        pass= (EditText) findViewById(R.id.edpassword);
        blogin= (Button) findViewById(R.id.Blogin);
        btn_crearcuenta= (Button) findViewById(R.id.btn_crearcuenta);
        btn_recuperarpass= (Button) findViewById(R.id.btn_recuperarpass);

        final configuracion conf = new configuracion(this);
        //registrar=(TextView) findViewById(R.id.link_to_register);
        if (conf.getUserNick() != null){
            Intent i=new Intent(LoginActivity.this, Main.class);
            user.setText(conf.getUserNick());
            usuario2=user.getText().toString();
            i.putExtra("user",usuario2);
            startActivity(i);

            //user.setText(conf.getUserEmail());
        }

        //Login button action

        //String valor=user.getText().toString();
		/*if (valor != null){
			Intent i=new Intent(Login.this, HiScreen.class);
			//i.putExtra("user",user);
			startActivity(i);
		}*/

        blogin.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                conf.setUserNick(user.getText().toString());

                //Extreamos datos de los EditText
                String usuario=user.getText().toString();
                String passw=pass.getText().toString();

                if(user.getText().toString().equals("")){
                    user.setError("Campo requerido");
                }
                if(pass.getText().toString().equals("")){
                    pass.setError("Campo requerido");
                }
                if(!pass.getText().toString().equals("") && !user.getText().toString().equals("")) {
                    new asynclogin().execute(usuario, passw);
                }
            }
        });

        btn_crearcuenta.setOnClickListener(new View.OnClickListener() {
            String usuario3;

            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, crear_cuenta.class);
                usuario3 = user.getText().toString();
                i.putExtra("user", usuario3);
                startActivity(i);
            }
        });

        btn_recuperarpass.setOnClickListener(new View.OnClickListener() {
            String usuario3;

            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RecuperarClave.class);
                usuario3 = user.getText().toString();
                i.putExtra("user", usuario3);
                startActivity(i);
            }
        });

    }

    //vibra y muestra un Toast
    public void err_login(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
        toast1.show();
    }

    /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean loginstatus(String username ,String password ) {
        int logstatus=-1;

    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("usuario",username));
        postparameters2send.add(new BasicNameValuePair("password",password));

        //realizamos una peticion y como respuesta obtenes un array JSON
        JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

      		/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
      		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
      		 * observar el progressdialog
      		 * la podemos eliminar si queremos
      		 */
        SystemClock.sleep(950);

        //si lo que obtuvimos no es null
        if (jdata!=null && jdata.length() > 0){

            JSONObject json_data; //creamos un objeto JSON
            try {
                json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                logstatus=json_data.getInt("logstatus");//accedemos al valor
                Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //validamos el valor obtenido
            if (logstatus==0){// [{"logstatus":"0"}]
                Log.e("loginstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("loginstatus ", "valido");
                return true;
            }

        }else{	//json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR");
            return false;
        }

    }


    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){

        if 	(username.equals("") || password.equals("")){
            Log.e("LoginActivity ui", "checklogindata user or pass error");
            return false;

        }else{

            return true;
        }

    }

/*		CLASE ASYNCTASK
 *
 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir
 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
 * ademas observariamos el mensaje de que la app no responde.
 */

    class asynclogin extends AsyncTask< String, String, String > {

        String user,pass;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            //obtnemos usr y pass
            user=params[0];
            pass=params[1];

            //enviamos y recibimos y analizamos los datos en segundo plano.
            if (loginstatus(user,pass)==true){
                return "ok"; //login valido
            }else{
                return "err"; //login invalido
            }
        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);

            if (result.equals("ok")){

                Intent i=new Intent(LoginActivity.this, Main.class);
                i.putExtra("user",user);
                startActivity(i);

            }else{
                err_login();
            }

        }

    }

}