package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultadoJuego extends AppCompatActivity {
  private String Id;
  private String puntaje;

  private int dificultad;  // 1 : Fácil, 2 : Media, 3 : Difícil
  private char tipoJuego;  // A : Antónimos, S : Sinónimos


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultado_juego);

    tipoJuego = '-';
    dificultad = '-';

    Intent intent = getIntent();
    Id = intent.getStringExtra("Id");


    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("puntuacion"))) {
        puntaje = Integer.toString(extras.getInt("puntuacion"));
        verPuntuacionEnPantalla();
        registrarPuntuacion();
      }
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getInt("dificultad");
      }
      if ((extras != null) && (extras.containsKey("tipoJuego"))) {
        tipoJuego = extras.getChar("tipoJuego");
      }
    }
  }

  private void verPuntuacionEnPantalla() {
    try {
      TextView textView = (TextView) findViewById(R.id.textoResultadoPuntuacion);
      if (textView != null) {
        textView.setText(this.puntaje);
      } else {
        throw new Exception();
      }
    } catch (Exception e) {
      Log.e("resultadoJuego-", "Puntuación no puede mostrarse en Activity");
    }
  }

  private void registrarPuntuacion() {
    try {
      String urlRegistro = "http://proyectosopaletras.esy.es/registrarPuntuacion.php?"
          + "id=" + Id + "&puntuacion=" + puntaje;

      String valores = new SolicitanteWeb(this, urlRegistro).execute().get();

      if (valores.equals("New record created successfully")) {
        Toast.makeText(this, R.string.alerta_PuntuacionRegistrada, Toast.LENGTH_LONG).show();
      } else {
        throw new Exception();
      }
    } catch (Exception e) {
      Toast.makeText(this, R.string.error_PuntuacionNoRegistrada, Toast.LENGTH_LONG).show();
    }
  }

  public void volverAJugar(View view) {
    try {
      Intent intent = new Intent(this, ActividadJuego.class);
      intent.putExtra("dificultad", dificultad);
      intent.putExtra("tipoJuego", tipoJuego);
      startActivity(intent);
      intent.putExtra("Id", Id);
      finish();
    } catch (Exception e) {
      errorVolverAJugar();
    }
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
    Toast.makeText(this, R.string.error_VolverAJugarInvalido, Toast.LENGTH_LONG).show();
    onBackPressed();
  }
}
