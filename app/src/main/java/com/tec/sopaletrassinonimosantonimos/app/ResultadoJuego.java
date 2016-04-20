package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultadoJuego extends AppCompatActivity {
  private TextView textoResultadoPuntuacion;

  private char dificultad;  // a : Facil, b : Media, c : Dificil
  private char tipoJuego;  // a : Antónimos, s : Sinónimos


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultado_juego);

    textoResultadoPuntuacion = (TextView) findViewById(R.id.textoResultadoPuntuacion);
    tipoJuego = '-';
    dificultad = '-';


    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("puntuacion"))) {
        textoResultadoPuntuacion.setText(Integer.toString(extras.getInt("puntuacion")));
        registrarPuntuacion();
      }
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getChar("dificultad");
      }
      if ((extras != null) && (extras.containsKey("tipoJuego"))) {
        tipoJuego = extras.getChar("tipoDificultad");
      }
    }
  }

  private void registrarPuntuacion() {
    // TODO Registrar puntuación
  }

  public void volverAJugar(View view) {

  }

  public void volverAMenuPrincipal(View view) {
    Intent intent = new Intent(this, MenuPrincipal.class);
    startActivity(intent);
    this.finish();
  }

  private void errorVolverAJugar() {
    Toast.makeText(this, R.string.error_volverAJugar, Toast.LENGTH_LONG).show();
    Intent intent = new Intent(this, MenuPrincipal.class);
    startActivity(intent);
    this.finish();
  }
}
