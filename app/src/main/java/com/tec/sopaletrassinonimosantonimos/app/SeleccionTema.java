package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class SeleccionTema extends AppCompatActivity {
  private char dificultad;  // a : Facil, b : Media, c : Dificil
  private char tipoJuego;  // a : Antónimos, s : Sinónimos

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tema_juego);

    dificultad = '-';

    if (savedInstanceState == null) {
      // Obtiene el grado de dificultad de la actividad anterior
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getChar("dificultad");
      } else {
        notificarErrorDificultad();
      }
    }
  }


  public void temaSeleccionado(View view) {
    switch (view.getId()) {
      case R.id.botonSinonimos:
        tipoJuego = 's';
        iniciarJuego();
        break;
      case R.id.botonAntonimos:
        tipoJuego = 'a';
        iniciarJuego();
        break;
      default:
        return;
    }
  }

  private void iniciarJuego() {
    Intent intent = new Intent(this, ActividadJuego.class);

    intent.putExtra("dificultad", dificultad);
    intent.putExtra("tipoJuego", tipoJuego);

    startActivity(intent);
  }

  private void notificarErrorDificultad() {
    Toast.makeText(this, R.string.error_dificultadNoDetectada, Toast.LENGTH_LONG).show();
    Intent intent = new Intent(this, SeleccionDificultad.class);
    startActivity(intent);
    this.finish();
  }
}
