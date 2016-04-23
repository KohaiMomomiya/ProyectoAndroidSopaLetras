package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SeleccionDificultad extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dificultad_juego);
  }

  public void seleccionarDificultad(View view) {
    Intent intent = new Intent(this, SeleccionTema.class);

    switch (view.getId()) {
      case R.id.botonDificultadFacil:
        intent.putExtra("dificultad", 'a');
        break;
      case R.id.botonDificultadMedia:
        intent.putExtra("dificultad", 'b');
        break;
      case R.id.botonDificultadDificil:
        intent.putExtra("dificultad", 'c');
        break;
      default:
        intent = new Intent(this, MenuPrincipal.class);
        break;
    }
    finish();
    startActivity(intent);
  }
}
