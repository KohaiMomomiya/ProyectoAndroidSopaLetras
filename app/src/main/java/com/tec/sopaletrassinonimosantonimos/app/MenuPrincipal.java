package com.tec.sopaletrassinonimosantonimos.app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menuprincipal);

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
    
    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    cerrarSesion();
  }

  public void jugarAhora(View view) {
    Intent intent = new Intent(this, DificultadJuego.class);
    startActivity(intent);
  }

  // Cierra la sesión actual
  public void cerrarSesion() {
    Intent intent = new Intent(this, ActividadInicial.class);
    startActivity(intent);
    this.finish();
  }

  public void cerrarSesionConBoton(View view) {
    cerrarSesion();
  }
}
