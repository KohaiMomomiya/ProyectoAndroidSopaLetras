package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

      try {
        getDatos datos = new getDatos();
        datos.setJson_url("http://proyectosopaletras.esy.es/comprobarUsuario.php?correo=" +
            strEmail + "&contrasena=" + strPwd);
        String valores = datos.execute().get();
        if (!valores.equals("")) {
          JSONObject objeto = new JSONObject(valores);
          JSONArray lista = objeto.getJSONArray("Usuario");
          String val = lista.get(0).toString();
          Intent intent = new Intent(this, MenuPrincipal.class);
          intent.putExtra("Id", val);
          startActivity(intent);
        } else {
          Toast.makeText(this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
          return;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
        return;
      } catch (ExecutionException e) {
        e.printStackTrace();
        return;
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  /* Acción para abrir la Activity para registrar un nuevo usuario. */
  public void registrarNuevoUsuario(View view) {
    Intent intent = new Intent(this, RegistrarCuenta.class);
    startActivity(intent);
  }
}
