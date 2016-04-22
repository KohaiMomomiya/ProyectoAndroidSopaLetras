package com.tec.sopaletrassinonimosantonimos.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadJuego extends Activity {
    private char dificultad;  // a : Facil, b : Media, c : Dificil
    private char tipoJuego;  // a : Antónimos, s : Sinónimos

    private TextView textoPuntuacion;
    private TextView textoTemporizador;

    private int puntuacion;
    private int tiempoInicial_ms;
    private int[] celda1Seleccionada;
    private int[] celda2Seleccionada;

    private TableLayout matrizSopa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_juego);

        textoPuntuacion = (TextView) findViewById(R.id.puntuacionJuego);
        textoTemporizador = (TextView) findViewById(R.id.temporizadorJuego);
        this.matrizSopa = (TableLayout) findViewById(R.id.matrizSopa);

        puntuacion = 0;
        tiempoInicial_ms = 0;
        tipoJuego = '-';
        dificultad = '-';

        celda1Seleccionada = null;
        celda2Seleccionada = null;

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
            new CountDownTimer(tiempoInicial_ms, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Long minutos = millisUntilFinished / 60000;
                    Long segundos = (millisUntilFinished / 1000) % 60;

                    String strMinutos = minutos.toString();
                    String strSegundos = (segundos < 10) ? ("0" + segundos.toString()) : segundos.toString();

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
            textoPuntuacion.setText(Integer.toString(puntuacion));
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

    public void recibirBotonEnSopaLetras(View view) {
        Button botonSeleccionado = (Button) view;

        for (int fila = 0; fila < matrizSopa.getChildCount(); fila++) {
            TableRow filaActual = (TableRow) matrizSopa.getChildAt(fila);

            for (int columna = 0; columna < filaActual.getChildCount(); columna++) {
                Button botonEncontrado = (Button) filaActual.getChildAt(columna);

                if (botonEncontrado.getId() == botonSeleccionado.getId()) {
                    // Hacer operaciones
                    Log.d("D1", "Boton encontrado en "
                            + Integer.toString(fila) + " " + Integer.toString(columna));

                    if (celda1Seleccionada == null) {
                        celda1Seleccionada = new int[]{fila, columna};
                        return;
                    }
                    if (celda2Seleccionada == null) {
                        celda2Seleccionada = new int[]{fila, columna};
                        // Hacer operaciones de busqueda y comparación.
                        return;
                    }
                }
            }
        }
    }

    private Button encontrarBotonEnSopaLetras(int fila, int columna) {
        TableRow filaEncontrada = (TableRow) matrizSopa.getChildAt(fila);
        Button botonEncontrado = (Button) filaEncontrada.getChildAt(columna);
        return botonEncontrado;
    }

    private void borrarCeldasSeleccionadas() {
        celda1Seleccionada = null;
        celda2Seleccionada = null;
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
