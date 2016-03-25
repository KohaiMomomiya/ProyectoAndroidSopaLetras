package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DificultadJuego extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dificultad_juego);
  }

  public void seleccionarDificultad(View view) {
    Intent intent = new Intent(this, TemaJuego.class);

    int idBoton = view.getId();
    switch (idBoton) {
      case R.id.botonDificultadFacil:
        intent.putExtra("dificultad", 1);
        startActivity(intent);
        break;
      case R.id.botonDificultadMedia:
        intent.putExtra("dificultad", 2);
        startActivity(intent);
        break;
      case R.id.botonDificultadDificil:
        intent.putExtra("dificultad", 3);
        startActivity(intent);
        break;
      default:
        break;
    }
  }
}
