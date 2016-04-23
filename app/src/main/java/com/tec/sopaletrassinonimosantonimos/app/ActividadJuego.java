package com.tec.sopaletrassinonimosantonimos.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadJuego extends Activity {
    SopaLetras sopa;
    private char dificultad;  // a : Facil, b : Media, c : Dificil
    private char tipoJuego;  // a : Antónimos, s : Sinónimos
    private TextView textoPuntuacion;
    private TextView textoTemporizador;
    private int puntuacion;
    private int tiempoInicial_ms;
    private int[] celda1Seleccionada;
    private int[] celda2Seleccionada;
    private int numeroPalabras = 8;
    private TableLayout matrizSopa;
    private CountDownTimer temporizador;


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

        sopa = new SopaLetras(8,12,dificultad,tipoJuego);

        llenarMatrizGrafica();
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

                    textoTemporizador.setText(strMinutos + ":" + strSegundos);

                }

                @Override
                public void onFinish() {
                    textoTemporizador.setText(R.string.temporizador_cero);
                    Toast.makeText(getApplicationContext(), R.string.alerta_finTiempo, Toast.LENGTH_LONG).show();
                    finalizarJuego();
                }
            };
            temporizador.start();
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

    private void agregarPuntos() {
        switch (dificultad) {
            case 'a':
                puntuacion += 100;
                textoPuntuacion.setText(Integer.toString(puntuacion));
                break;
            case 'b':
                puntuacion += 300;
                textoPuntuacion.setText(Integer.toString(puntuacion));
                break;
            case 'c':
                puntuacion += 500;
                textoPuntuacion.setText(Integer.toString(puntuacion));
                break;
            default:
                break;
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

    public void llenarMatrizGrafica(){
        char[][] matrizLetras = sopa.getMatrizLetras();
        for (int fila = 0; fila < matrizSopa.getChildCount(); fila++) {
            TableRow filaActual = (TableRow) matrizSopa.getChildAt(fila);

            for (int columna = 0; columna < filaActual.getChildCount(); columna++) {
                Button botonEncontrado = (Button) filaActual.getChildAt(columna);
                String valor = Character.toString(matrizLetras[fila][columna]);
                botonEncontrado.setText(valor);
            }
        }
    }

    public void detectarLetraPresionada(View view) {
        Button botonSeleccionado = (Button) view;

        for (int fila = 0; fila < matrizSopa.getChildCount(); fila++) {
            TableRow filaActual = (TableRow) matrizSopa.getChildAt(fila);

            for (int columna = 0; columna < filaActual.getChildCount(); columna++) {
                Button botonEncontrado = (Button) filaActual.getChildAt(columna);

                if (botonEncontrado.getId() == botonSeleccionado.getId()) {
                    if (celda1Seleccionada == null) {
                        celda1Seleccionada = new int[]{fila, columna};
                        return;
                    }
                    if (celda2Seleccionada == null) {
                        celda2Seleccionada = new int[]{fila, columna};
                        int palabraEncontrada =
                            sopa.encontrarIndicePalabraPorCoordenadas(celda1Seleccionada[0],
                                celda1Seleccionada[1], celda2Seleccionada[0],
                                celda2Seleccionada[1]);
                        if (palabraEncontrada > -1) {
                            if (!sopa.palabraYaFueEncontrada(palabraEncontrada)) {
                                sopa.marcarPalabraComoEncontrada(palabraEncontrada);
                                marcarPalabraEncontrada();
                                agregarPuntos();
                            }
                        }
                        borrarCeldasSeleccionadas();
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

    public void volverAmenuPrincipal(View view) {
        temporizador.cancel();
        finish();
    }

    private void marcarPalabraEncontrada(){
        int filaCelda1 = celda1Seleccionada[0];
        int columnaCelda1 = celda1Seleccionada[1];
        int filaCelda2= celda2Seleccionada[0];
        int columnaCelda2= celda2Seleccionada[1];
        if(filaCelda1==filaCelda2){
            if(columnaCelda1>columnaCelda2){
                TableRow filaActual = (TableRow) matrizSopa.getChildAt(filaCelda1);
                for(int i=columnaCelda2; i<=columnaCelda1; i++){
                    Button botonEncontrado = (Button) filaActual.getChildAt(i);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                }
            }else if(columnaCelda1<columnaCelda2){
                TableRow filaActual = (TableRow) matrizSopa.getChildAt(filaCelda1);
                for(int i=columnaCelda1; i<=columnaCelda2; i++){
                    Button botonEncontrado = (Button) filaActual.getChildAt(i);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                }
            }
        }else if(columnaCelda1==columnaCelda2){
            if(filaCelda1>filaCelda2){
                for(int i=filaCelda2; i<=filaCelda1; i++){
                    TableRow filaActual = (TableRow) matrizSopa.getChildAt(i);
                    Button botonEncontrado = (Button) filaActual.getChildAt(columnaCelda1);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                }
            }else if(filaCelda1<filaCelda2){
                for(int i=filaCelda1; i<=filaCelda2; i++){
                    TableRow filaActual = (TableRow) matrizSopa.getChildAt(i);
                    Button botonEncontrado = (Button) filaActual.getChildAt(columnaCelda1);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                }
            }
        }else{
            if(filaCelda1>filaCelda2 && columnaCelda1>columnaCelda2){
                 int cont = columnaCelda2;
                for(int i=filaCelda2; i<=filaCelda1; i++){
                    TableRow filaActual = (TableRow) matrizSopa.getChildAt(i);
                    Button botonEncontrado = (Button) filaActual.getChildAt(cont);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                    cont++;
                }
            }else if(filaCelda1<filaCelda2 && columnaCelda1<columnaCelda2){
                int cont = columnaCelda1;
                for(int i=filaCelda1; i<=filaCelda2; i++){
                    TableRow filaActual = (TableRow) matrizSopa.getChildAt(i);
                    Button botonEncontrado = (Button) filaActual.getChildAt(cont);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                    cont++;
                }
            }else if(filaCelda1<filaCelda2 && columnaCelda1>columnaCelda2){
                int cont = columnaCelda2;
                for(int i=filaCelda1; i<=filaCelda2; i++){
                    TableRow filaActual = (TableRow) matrizSopa.getChildAt(i);
                    Button botonEncontrado = (Button) filaActual.getChildAt(cont);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                    cont++;
                }
            }else if(filaCelda1>filaCelda2 && columnaCelda1<columnaCelda2){
                int cont = columnaCelda1;
                for(int i=filaCelda2; i<=filaCelda1; i++){
                    TableRow filaActual = (TableRow) matrizSopa.getChildAt(i);
                    Button botonEncontrado = (Button) filaActual.getChildAt(cont);
                    botonEncontrado.setBackgroundColor(0xff00ff00);
                    cont++;
                }
            }

        }
    }
}
