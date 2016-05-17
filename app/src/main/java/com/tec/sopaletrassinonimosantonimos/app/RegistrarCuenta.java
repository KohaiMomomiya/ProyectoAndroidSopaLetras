package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class RegistrarCuenta extends AppCompatActivity {

  private EditText campoRegistroNombre;
  private EditText campoRegistroApellidos;
  private EditText campoRegistroEmail;
  private EditText campoRegistroPwd;
  private EditText campoVerificacionPwd;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registrar_cuenta);

    campoRegistroNombre = (EditText) findViewById(R.id.campo_registro_nombre);
    campoRegistroApellidos = (EditText) findViewById(R.id.campo_registro_apellidos);
    campoRegistroEmail = (EditText) findViewById(R.id.campo_registro_eMail);
    campoRegistroPwd = (EditText) findViewById(R.id.campo_registro_pwd);
    campoVerificacionPwd = (EditText) findViewById(R.id.campo_verificacion_pwd);
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, ActividadInicial.class);
    startActivity(intent);
    this.finish();
  }

  public void solicitarRegistro(View view) {
    String strNombre = campoRegistroNombre.getText().toString().trim();
    String strApellidos = campoRegistroApellidos.getText().toString().trim();
    String strEmail = campoRegistroEmail.getText().toString().trim();
    String strPwd = campoRegistroPwd.getText().toString().trim();
    String strVerificarPwd = campoVerificacionPwd.getText().toString().trim();

    if (verificarDatosIngresados(strNombre, strApellidos, strEmail, strPwd, strVerificarPwd)) {
      try {
        String urlRegistrarUsuario = "http://proyectosopaletras.esy.es/registrarUsuario.php?" +
            "nombre='" + strNombre + "%20" + strApellidos + "'" +
            "&correo='" + strEmail + "'" +
            "&contrasena='" + strPwd + "'";
        String resultados = new SolicitanteWeb(this, urlRegistrarUsuario).execute().get();

        if (resultados.equals("New record created successfully")) {
          Toast.makeText(this, R.string.alerta_CuentaCreada, Toast.LENGTH_LONG).show();
          finish();
        } else {
          Toast.makeText(this, "La cuenta no pudo ser creada.", Toast.LENGTH_LONG).show();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean verificarDatosIngresados(String nombre, String apellidos, String eMail,
                                           String pwd, String pwd2) {
    ValidacionDatosUsuario validadorEntradas = new ValidadorDatosUsuarioApp();

    if (!validadorEntradas.validarNombre(nombre)) {
      if (nombre.isEmpty()) {
        Toast.makeText(this, R.string.error_NombreVacio, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.error_NombreInvalido, Toast.LENGTH_LONG).show();
      }
      return false;
    }
    if (!validadorEntradas.validarNombre(apellidos)) {
      if (apellidos.isEmpty()) {
        Toast.makeText(this, R.string.error_ApellidosVacio, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.error_ApellidosInvalidos, Toast.LENGTH_LONG).show();
      }
      return false;
    }
    if (!validadorEntradas.validarEmail(eMail)) {
      if (eMail.isEmpty()) {
        Toast.makeText(this, R.string.error_eMailVacio, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.error_eMailNoValido, Toast.LENGTH_LONG).show();
      }
      return false;
    }

    if (!validadorEntradas.validarPwd(pwd)) {
      if (pwd.isEmpty()) {
        Toast.makeText(this, R.string.error_PwdVacio, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.error_PwdInvalido, Toast.LENGTH_LONG).show();
      }
      return false;
    }
    if (pwd2.isEmpty()) {
      Toast.makeText(this, R.string.error_Pwd2Vacio, Toast.LENGTH_LONG).show();
      return false;
    }
    if (!pwd.contentEquals(pwd2)) {
      Toast.makeText(this, R.string.error_PwdsDiferentes, Toast.LENGTH_LONG).show();
      return false;
    } else {
      return true;
    }
  }
}
