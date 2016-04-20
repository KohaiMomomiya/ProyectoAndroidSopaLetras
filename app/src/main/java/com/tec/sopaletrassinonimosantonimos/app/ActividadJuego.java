package com.tec.sopaletrassinonimosantonimos.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadJuego extends Activity {

  private TextView textoPuntuacion;
  private TextView textoTemporizador;
  private CountDownTimer temporizador;

  private int puntuacion;
  private int tiempoInicial_ms;

  private char dificultad;  // a : Facil, b : Media, c : Dificil
  private char tipoJuego;  // a : Antónimos, s : Sinónimos


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actividad_juego);

    textoPuntuacion = (TextView) findViewById(R.id.puntuacionJuego);
    textoTemporizador = (TextView) findViewById(R.id.temporizadorJuego);
    temporizador = null;

    puntuacion = 0;
    tiempoInicial_ms = 0;
    tipoJuego = '-';
    dificultad = '-';

    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("tiempo"))) {
        tiempoInicial_ms = extras.getInt("tiempo") + 1; // Segundo adicional de retraso
      }
      if ((extras != null) && (extras.containsKey("tipoJuego"))) {
        tipoJuego = extras.getChar("tipoJuego");
      }
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getChar("dificultad");
      }
    }
    verificarDatos();
    setTiempoInicial_ms();
    iniciarTemporizador();
  }


  public void iniciarTemporizador() {
    try {
      temporizador = new CountDownTimer(tiempoInicial_ms, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
          Long minutos = millisUntilFinished / 60000;
          Long segundos = (millisUntilFinished / 1000) % 60;

          String strMinutos = minutos.toString();
          String strSegundos = (segundos < 10) ? ("0" + segundos.toString()) : segundos.toString();

          // Prueba puntos
          agregarPuntos(10);
          textoPuntuacion.setText(Integer.toString(puntuacion));
          //

          textoTemporizador.setText(strMinutos + ":" + strSegundos);

        }

        @Override
        public void onFinish() {
          textoTemporizador.setText(R.string.temporizador_cero);
          Toast.makeText(getApplicationContext(), R.string.alerta_finTiempo, Toast.LENGTH_LONG).show();
          finalizarJuego();
        }
      }.start();
    } catch (Exception e) {
      errorInicio();
    }
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, MenuPrincipal.class);
    startActivity(intent);
    finish();
  }

  private void setTiempoInicial_ms() {
    switch (dificultad) {
      // Se agrega un segundo para compensar retrasos de tiempo causados por el SO
      case 'a':
        tiempoInicial_ms = 121 * 1000;  // Dificultad fácil empieza con 2 minutos.
        break;
      case 'b':
        tiempoInicial_ms = 91 * 1000;  // Dificultad media empieza con 1 minuto y 30 segundos.
        break;
      case 'c':
        tiempoInicial_ms = 61 * 1000;  // Dificultad difícil empieza con 1 minuto.
        break;
      default:
        break;
    }
  }

  private void agregarPuntos(int incremento) {
    if (incremento > 0) {
      puntuacion += incremento;
    }
  }

  private void finalizarJuego() {
    Intent intent = new Intent(this, ResultadoJuego.class);
    intent.putExtra("puntuacion", puntuacion);
    intent.putExtra("dificultad", dificultad);
    intent.putExtra("tipoJuego", tipoJuego);
    startActivity(intent);
    finish();
  }

  private void verificarDatos() {
    if ((dificultad == '-') || (tipoJuego == '-')) {
      errorInicio();
    }
  }

  private void errorInicio() {
    Toast.makeText(this, R.string.error_iniciarJuego, Toast.LENGTH_LONG).show();
    onBackPressed();
  }
}
