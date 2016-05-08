package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultadoJuego extends AppCompatActivity {
  private TextView textoResultadoPuntuacion;

  private int dificultad;  // 1 : Facil, 2 : Media, 3 : Dificil
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
        String strPuntuacion = Integer.toString(extras.getInt("puntuacion"));
        textoResultadoPuntuacion.setText(strPuntuacion);
        registrarPuntuacion();
      }
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getInt("dificultad");
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
    Intent intent = new Intent(this, ActividadJuego.class);
    intent.putExtra("dificultad", dificultad);
    intent.putExtra("tipoJuego", tipoJuego);
    startActivity(intent);
    finish();
  }

  public void onBackPressed() {
    Intent intent = new Intent(this, MenuPrincipal.class);
    startActivity(intent);
    finish();
  }

  public void volverAMenuPrincipal(View view) {
    onBackPressed();
  }

  private void errorVolverAJugar() {
    Toast.makeText(this, R.string.error_volverAJugar, Toast.LENGTH_LONG).show();
    onBackPressed();
  }
}
