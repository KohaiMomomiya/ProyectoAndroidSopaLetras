package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActividadInicial extends AppCompatActivity {

  String JSON_String;
  private EditText campoTexto_eMail;
  private EditText campoTexto_pwd;
  
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
  public void iniciarSesion(View view) {

    // Se obtiene el contenido de los campos de texto.
    String strEmail = campoTexto_eMail.getText().toString().trim();
    String strPwd = campoTexto_pwd.getText().toString().trim();
    ValidacionDatosUsuario validadorEntradas = new ValidadorDatosUsuarioApp();


    if (!validadorEntradas.validarEmail(strEmail)) {
      if (strEmail.isEmpty()) {
        Toast.makeText(this, R.string.error_eMailVacio, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.error_eMailNoValido, Toast.LENGTH_LONG).show();
      }
    } else if (!validadorEntradas.validarPwd(strPwd)) {
      if (strPwd.isEmpty()) {
        Toast.makeText(this, R.string.error_PwdVacio, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.error_PwdInvalido, Toast.LENGTH_LONG).show();
      }
    } else {
      try {
        String urlLogin = "http://proyectosopaletras.esy.es/comprobarUsuario.php?correo="
            + strEmail + "&contrasena=" + strPwd;
        String resultados = new SolicitanteWeb(this, urlLogin).execute().get();

        if (!resultados.equals("")) {
          JSONObject objeto = new JSONObject(resultados);
          JSONArray lista = objeto.getJSONArray("Usuario");
          String val = lista.get(0).toString();

          Intent intent = new Intent(this, MenuPrincipal.class);
          intent.putExtra("Id", val);
          startActivity(intent);
        } else {
          Toast.makeText(this, R.string.error_UsuarioPwdInvalidos, Toast.LENGTH_LONG).show();
          return;
        }
      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this,
            "Hubo un error al ingresar a la aplicación. Inténtelo nuevamente.",
            Toast.LENGTH_LONG).show();
      }
    }
  }

  /* Acción para abrir la Activity para registrar un nuevo usuario. */
  public void registrarNuevoUsuario(View view) {
    Intent intent = new Intent(this, RegistrarCuenta.class);
    startActivity(intent);
  }
}
