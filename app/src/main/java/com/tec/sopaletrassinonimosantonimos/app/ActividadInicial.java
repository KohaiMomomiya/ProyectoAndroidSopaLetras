package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.concurrent.ExecutionException;

public class ActividadInicial extends AppCompatActivity {

  EditText campoTexto_eMail;
  EditText campoTexto_pwd;
  String JSON_String;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actividad_inicial);

    // Asocia los campos de texto del layout a variables internas de la clase.
    campoTexto_eMail = (EditText) findViewById(R.id.campoLoginEMail);
    campoTexto_pwd = (EditText) findViewById(R.id.campoLoginPwd);

  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  /* Intentar el ingreso a la aplicación */
  public void ingresarEnAplicacion(View view) {

    // Se obtiene el contenido de los campos de texto.
    String strEmail = campoTexto_eMail.getText().toString().trim();
    String strPwd = campoTexto_pwd.getText().toString().trim();


    if (strEmail.isEmpty()) {
      Toast.makeText(this, R.string.alerta_EmailVacio, Toast.LENGTH_LONG).show();
      return;
    }
    if (strPwd.isEmpty()) {
      Toast.makeText(this, R.string.alerta_PwdVacio, Toast.LENGTH_LONG).show();
      return;
    } else {

      try{
        getDatos datos = new getDatos();
        datos.setJson_url("http://proyectosopaletras.esy.es/comprobarUsuario.php?correo=" +
                strEmail+"&contrasena="+strPwd);
        String valores = datos.execute().get();
        if(!valores.equals("")){
          Intent intent = new Intent(this, MenuPrincipal.class);
          startActivity(intent);
        }else{
          Toast.makeText(this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
          return;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  /* Acción para abrir la Activity para registrar un nuevo usuario. */
  public void registrarNuevoUsuario(View view) {
    Intent intent = new Intent(this, RegistrarCuenta.class);
    startActivity(intent);
  }


  class getDatos extends AsyncTask<Void, Void, String>{

    String json_url;

    public String getJson_url() {
      return json_url;
    }

    public void setJson_url(String json_url) {
      this.json_url = json_url;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
          URL url = null;
          url = new URL(json_url);
          HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
          InputStream inputStream = httpURLConnection.getInputStream();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
          StringBuilder stringBuilder = new StringBuilder();
          while((JSON_String=bufferedReader.readLine())!=null){
            stringBuilder.append(JSON_String+"\n");
          }
          bufferedReader.close();
          inputStream.close();
          httpURLConnection.disconnect();
          return stringBuilder.toString().trim();


        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }


      return null;
    }

    @Override
    protected void onPostExecute(String result){
      super.onPostExecute(result);
    }
  }
}
