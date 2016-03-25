package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_registrar_cuenta, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    return super.onOptionsItemSelected(item);
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

    if (strNombre.isEmpty()) {
      Toast.makeText(this, R.string.alerta_NombreVacio, Toast.LENGTH_LONG).show();
      return;
    }
    if (strApellidos.isEmpty()) {
      Toast.makeText(this, R.string.alerta_ApellidosVacio, Toast.LENGTH_LONG).show();
      return;
    }
    if (strEmail.isEmpty()) {
      Toast.makeText(this, R.string.alerta_EmailVacio, Toast.LENGTH_LONG).show();
      return;
    }
    if (strPwd.isEmpty()) {
      Toast.makeText(this, R.string.alerta_PwdVacio, Toast.LENGTH_LONG).show();
      return;
    }
    if (strVerificarPwd.isEmpty()) {
      Toast.makeText(this, R.string.alerta_Pwd2Vacio, Toast.LENGTH_LONG).show();
      return;
    }
    if (!strPwd.contentEquals(strVerificarPwd)) {
      Toast.makeText(this, R.string.alerta_PwdsDiferentes, Toast.LENGTH_LONG).show();
      return;
    } else {
      //TODO Operación de registrar nueva cuenta y obtener la cuenta creada desde Internet.
      Toast.makeText(this, R.string.mensaje_cuentaCreada, Toast.LENGTH_LONG).show();
    }
  }
}
