package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarCuenta extends AppCompatActivity {

  EditText campoRegistroNombre;
  EditText campoRegistroApellidos;
  EditText campoRegistroEmail;
  EditText campoRegistroPwd;
  EditText campoVerificacionPwd;
  
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
      //TODO Operación de registrar nueva cuenta y obtener la cuenta creada desde Internet.
      Toast.makeText(this, R.string.alerta_cuentaCreada, Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(this, R.string.alerta_cuentaNoCreada, Toast.LENGTH_LONG).show();
    }
  }

  private boolean verificarDatosIngresados(String nombre, String apellidos, String eMail,
                                           String pwd, String pwd2) {
    if (nombre.isEmpty()) {
      Toast.makeText(this, R.string.alerta_NombreVacio, Toast.LENGTH_LONG).show();
      return false;
    }
    if (apellidos.isEmpty()) {
      Toast.makeText(this, R.string.alerta_ApellidosVacio, Toast.LENGTH_LONG).show();
      return false;
    }
    if (eMail.isEmpty()) {
      Toast.makeText(this, R.string.alerta_EmailVacio, Toast.LENGTH_LONG).show();
      return false;
    }
    if (!eMail.contains("@")) {
      Toast.makeText(this, R.string.alerta_EmailFormatoInvalido, Toast.LENGTH_LONG).show();
      return false;
    }
    if (pwd.isEmpty()) {
      Toast.makeText(this, R.string.alerta_PwdVacio, Toast.LENGTH_LONG).show();
      return false;
    }
    if (pwd2.isEmpty()) {
      Toast.makeText(this, R.string.alerta_Pwd2Vacio, Toast.LENGTH_LONG).show();
      return false;
    }
    if (!pwd.contentEquals(pwd2)) {
      Toast.makeText(this, R.string.alerta_PwdsDiferentes, Toast.LENGTH_LONG).show();
      return false;
    } else {
      return true;
    }
  }
}
