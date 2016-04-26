package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class SeleccionTema extends AppCompatActivity {
  String Id;
  private char dificultad;  // a : Facil, b : Media, c : Dificil

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tema_juego);

    Intent intent = getIntent();
    Id = intent.getStringExtra("Id");

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
        iniciarJuego('s');
        break;
      case R.id.botonAntonimos:
        iniciarJuego('a');
        break;
      default:
        return;
    }
  }

  private void iniciarJuego(char tipoJuego) {
    Intent intent = new Intent(this, ActividadJuego.class);
    intent.putExtra("dificultad", dificultad);
    intent.putExtra("tipoJuego", tipoJuego);
    intent.putExtra("Id", Id);

    finish();
    startActivity(intent);
  }

  private void notificarErrorDificultad() {
    Toast.makeText(this, R.string.error_dificultadNoDetectada, Toast.LENGTH_LONG).show();
    Intent intent = new Intent(this, MenuPrincipal.class);
    intent.putExtra("Id", Id);
    finish();
    startActivity(intent);
  }
}
