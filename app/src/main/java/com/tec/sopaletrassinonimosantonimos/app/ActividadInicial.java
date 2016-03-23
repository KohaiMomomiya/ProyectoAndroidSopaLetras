package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class ActividadInicial extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actividad_inicial);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  /* Acción para abrir la Activity para registrar un nuevo usuario. */
  public void eventoRegistrarNuevoUsuario(View view) {
    Intent intent = new Intent(this, RegistrarCuenta.class);
    startActivity(intent);
  }
}
