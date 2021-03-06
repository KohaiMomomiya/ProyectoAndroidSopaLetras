package com.tec.sopaletrassinonimosantonimos.app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {

  private String Id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_principal);

    Intent intent = getIntent();
    Id = intent.getStringExtra("Id");


    ActionBar actionBar = this.getActionBar();
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setDisplayShowHomeEnabled(false);
    }
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_menuprincipal, menu);
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
    cerrarSesion();
  }

  public void jugarAhora(View view) {
    Intent intent = new Intent(this, SeleccionDificultad.class);
    intent.putExtra("Id", Id);
    startActivity(intent);
  }

  public void verPuntuaciones(View view) {
    Intent intent = new Intent(this, Resultados.class);
    startActivity(intent);
  }

  /**
   * Inicia la Activity con pantalla de Agradecimientos.
   */
  public void verAgradecimientos(MenuItem menuItem) {
    Intent intent = new Intent(this, Agradecimiento.class);
    startActivity(intent);
  }

  public void verInstrucciones(MenuItem menuItem) {
    Intent intent = new Intent(this, Instrucciones.class);
    startActivity(intent);
  }


  /**
   * Cierra la sesión actual
   */
  private void cerrarSesion() {
    Intent intent = new Intent(this, ActividadInicial.class);
    Toast.makeText(this, R.string.toast_CierreSesion, Toast.LENGTH_LONG).show();
    finish();
    startActivity(intent);
  }

  public void cerrarSesionConBoton(View view) {
    cerrarSesion();
  }
}
