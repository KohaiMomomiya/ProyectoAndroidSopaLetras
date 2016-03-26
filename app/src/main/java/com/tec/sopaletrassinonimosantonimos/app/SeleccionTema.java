package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class SeleccionTema extends AppCompatActivity {
  byte dificultadJuego;   // 1: Fácil, 2: Media, 3: Difícil
  byte temaJuego;         // 1: Sinónimos, 2: Antónimos

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tema_juego);

    dificultadJuego = 0;
    temaJuego = 0;

    if (savedInstanceState == null) {
      // Obtiene el grado de dificultad de la actividad anterior
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultadJuego = extras.getByte("dificultad");
      } else {
        Toast.makeText(this, "No se pudo detectar el grado de dificultad",
            Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SeleccionDificultad.class);
        startActivity(intent);
        this.finish();
      }
    }
  }

  public void temaSeleccionado(View view) {
    switch (view.getId()) {
      case R.id.botonSinonimos:
        temaJuego = 1;
        toastSeleccion();
        break;
      case R.id.botonAntonimos:
        temaJuego = 2;
        toastSeleccion();
        break;
      default:
        break;
    }
  }

  // Método de prueba
  public void toastSeleccion() {
    switch (dificultadJuego) {
      case 1:
        Toast.makeText(this, "Dificultad seleccionada: Fácil", Toast.LENGTH_LONG).show();
        break;
      case 2:
        Toast.makeText(this, "Dificultad seleccionada: Media", Toast.LENGTH_LONG).show();
        break;
      case 3:
        Toast.makeText(this, "Dificultad seleccionada: Difícil", Toast.LENGTH_LONG).show();
        break;
      default:
        break;
    }
    switch (temaJuego) {
      case 1:
        Toast.makeText(this, "Tema seleccionado: Sinónimos", Toast.LENGTH_LONG).show();
        break;
      case 2:
        Toast.makeText(this, "Tema seleccionado: Antónimos", Toast.LENGTH_LONG).show();
        break;
      default:
        break;
    }
  }

  // TODO Método para iniciar el juego con el tema y dificultad seleccionadas.
}
