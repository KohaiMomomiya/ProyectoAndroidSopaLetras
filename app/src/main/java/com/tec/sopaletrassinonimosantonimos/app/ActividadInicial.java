package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActividadInicial extends AppCompatActivity {

  EditText campoTexto_eMail;
  EditText campoTexto_pwd;
  
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

      //TODO Verificación del nombre de usuario y contraseña

      Intent intent = new Intent(this, MenuPrincipal.class);
      startActivity(intent);
    }
  }

  /* Acción para abrir la Activity para registrar un nuevo usuario. */
  public void registrarNuevoUsuario(View view) {
    Intent intent = new Intent(this, RegistrarCuenta.class);
    startActivity(intent);
  }
}
