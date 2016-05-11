package com.tec.sopaletrassinonimosantonimos.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActividadJuego extends Activity {
  private SopaLetras sopa;
  private String Id;

  private TextView textoPuntuacion;
  private TextView textoTemporizador;

  private int tiempoInicial_ms;
  private CountDownTimer temporizador;

  private ArrayList<int[]> letrasSeleccionadas;
  private TableLayout matrizSopa;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actividad_juego);

    int dificultad = 0;
    char tipoJuego = 0;

    textoPuntuacion = (TextView) findViewById(R.id.puntuacionJuego);
    textoTemporizador = (TextView) findViewById(R.id.temporizadorJuego);
    this.matrizSopa = (TableLayout) findViewById(R.id.matrizSopa);

    tiempoInicial_ms = 0;

    letrasSeleccionadas = new ArrayList<int[]>();

    Intent intent = getIntent();
    Id = intent.getStringExtra("Id");

    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("tipoJuego"))) {
        tipoJuego = extras.getChar("tipoJuego");
      }
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getInt("dificultad");
      }
    }

    verificarDatos(dificultad, tipoJuego);

    try {
      sopa = new SopaLetras(8, 10, dificultad, tipoJuego);
      llenarMatrizGrafica();
      llenarMatrizActivity();
      llenarListaObjetivos(sopa.getPalabrasObjetivo());
      setTiempoInicial_ms(dificultad);
      iniciarTemporizador();

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


  /**
   * Detiene el temporizador activo antes de terminar la Activity y regresar al menú prinicipal.
   *
   * @param view Invocador de función.
   */
  public void volverAmenuPrincipal(View view) {
    temporizador.cancel();
    onBackPressed();
  }

  /**
   * Detecta la celda (botón) seleccionada en la matríz de letras.
   *
   * @param view Vista (botón) seleccionada.
   */
  public void seleccionarLetra(View view) {
    Button botonSeleccionado = (Button) view;
    int[] coordenadasBoton = encontrarCoordenadasBotonLetra(botonSeleccionado);

    if (coordenadasBoton == null) {
      Toast.makeText(this, R.string.error_noSeleccionLetra, Toast.LENGTH_LONG).show();
    } else {
      if (revisaLetraYaSeleccionada(coordenadasBoton)) {
        Toast.makeText(this, R.string.error_letraYaSeleccionada, Toast.LENGTH_SHORT).show();
      } else {
        buscarPalabraEnSopa(coordenadasBoton);
      }
    }
  }


  // TODO Llenar palabras objetivo en Activity
  private void llenarListaObjetivos(ArrayList<String> palabras) {
    TableLayout tabla = (TableLayout) findViewById(R.id.tablaPalabrasObjetivo);
    int cantidadPalabras = 8;

    if (palabras.size() < cantidadPalabras) {
      cantidadPalabras = palabras.size();
    }

    int numFila = 0;

    for (int contadorPalabra = 0; contadorPalabra < cantidadPalabras; contadorPalabra++) {
      if (((contadorPalabra / 2) > 0) && ((contadorPalabra % 2) == 0)) {
        ++numFila;
      }
      TableRow fila = (TableRow) tabla.getChildAt(numFila);
      int columna = contadorPalabra % 2;
      TextView texto = (TextView) fila.getChildAt(columna);
      texto.setText(palabras.get(contadorPalabra));
    }
  }


  /**
   * Revisa si una letra siendo seleccionada ya fue seleccionada anteriormente.
   *
   * @param coordenadasLetra Coordenadas de la letra.
   * @return Boolean indicando resultado de búsqueda: True si ya fue seleccionada antes, False en
   * caso contrario.
   */
  private boolean revisaLetraYaSeleccionada(int[] coordenadasLetra) {
    for (int[] coordenadasEncontradas : letrasSeleccionadas) {
      if ((coordenadasEncontradas[0] == coordenadasLetra[0])
          && (coordenadasEncontradas[1] == coordenadasLetra[1])) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retorna las coordenadas de una letra seleccionada en la matríz.
   *
   * @param letra Botón asociado a letra seleccionada.
   * @return Coordenadas (eje X, eje Y) asociada al botón de la matríz.
   */
  @Nullable
  private int[] encontrarCoordenadasBotonLetra(Button letra) {
    for (int fila = 0; fila < matrizSopa.getChildCount(); fila++) {
      TableRow filaActual = (TableRow) matrizSopa.getChildAt(fila);

      for (int columna = 0; columna < matrizSopa.getChildCount(); columna++) {
        Button letraEncontrada = (Button) filaActual.getChildAt(columna);
        if (letraEncontrada == letra) {
          return new int[]{fila, columna};
        }
      }
    }
    return null;
  }

  /**
   * Marca letra como disponible. Si la letra ya fue utilizada (al encontrar otra palabra), vuelve a
   * marcarse como utilizada.
   *
   * @param celda Botón asociado a letra.
   */
  private void marcarLetraDisponible(Button celda) {
    String estadoBoton = celda.getTag().toString();

    if (estadoBoton.contentEquals("utilizada-seleccionada")
        || estadoBoton.contentEquals("utilizada")) {
      cambiarColorBoton(celda, R.drawable.letra_utilizada);
      celda.setTag("utilizada");
    } else {
      cambiarColorBoton(celda, R.drawable.letra_sopa);
      celda.setTag("disponible");
    }
  }

  /**
   * Marca letra como seleccionada. Si la letra ya fue utilizada (al encontrar otra palabra), se
   * marca como "utilizada-seleccionada".
   *
   * @param celda Boton asociado a letra.
   */
  private void marcarLetraSeleccionada(Button celda) {
    String estadoBoton = celda.getTag().toString();
    cambiarColorBoton(celda, R.drawable.letra_seleccionada);

    if (estadoBoton.contentEquals("utilizada")) {
      celda.setTag("utilizada-seleccionada");
    } else {
      celda.setTag("seleccionada");
    }
  }

  /**
   * Marca letra como utilizada (por una palabra que ya fue encontrada).
   *
   * @param celda Botón asociado a letra.
   */
  private void marcarLetraUtilizada(Button celda) {
    cambiarColorBoton(celda, R.drawable.letra_utilizada);
    celda.setTag("utilizada");
  }

  /**
   * Marca un conjunto de letras como disponibles.
   */
  private void marcarLetrasDisponibles(ArrayList<int[]> coordenadasBotones) {

    for (int[] coordenadasBoton : coordenadasBotones) {
      try {
        Button boton = encontrarBotonLetra(coordenadasBoton[0], coordenadasBoton[1]);
        marcarLetraDisponible(boton);
      } catch (Exception e) {
        Log.e("error_activity", e.getMessage());
        Log.e("error_activity", e.toString());
      }
    }
  }

  /**
   * Marca un conjunto de letras como seleccionadas.
   *
   * @param coordenadasBotones Lista de botones asociados a letras.
   */
  private void marcarLetrasSeleccionadas(ArrayList<int[]> coordenadasBotones) {

    for (int[] coordenadasBoton : coordenadasBotones) {
      try {
        Button boton = encontrarBotonLetra(coordenadasBoton[0], coordenadasBoton[1]);
        marcarLetraSeleccionada(boton);
      } catch (Exception e) {
        Log.e("error_activity", e.getMessage());
        Log.e("error_activity", e.toString());
      }
    }
  }

  /**
   * Marca un conjunto de letras como utilizadas (se utilizaron para encontrar una palabra
   * existente).
   */
  private void marcarLetrasUtilizadas(ArrayList<int[]> coordenadasBotones) {
    for (int[] coordenadasBoton : coordenadasBotones) {
      try {
        Button boton = encontrarBotonLetra(coordenadasBoton[0], coordenadasBoton[1]);
        marcarLetraUtilizada(boton);
      } catch (Exception e) {
        Log.e("error_activity", e.getMessage());
        Log.e("error_activity", e.toString());
      }
    }
  }

  /**
   * Verifica que los datos ingresados son válidos para generar una sopa de letras. Si se detecta un
   * datos inválido, se termina la Activity.
   *  @param dificultad Dificultad del juego. Debe ser 1, 2 o 3 para validar la prueba.
   * @param tipoJuego  Tipo de juego. Debe ser A (Antónimos) o S (Sinónimos) para validar la
   */
  private void verificarDatos(int dificultad, char tipoJuego) {
    if ((dificultad < 1) || (dificultad > 3)) {
      errorInicio();
    }
    if ((tipoJuego != 'A') && (tipoJuego != 'S')) {
      errorInicio();
    }
  }

  /**
   * Notificación sobre error al iniciar la Activity antes de terminarla. Luego de mostrar Toast, se
   * termina la Activity.
   */
  private void errorInicio() {
    Toast.makeText(this, R.string.error_iniciarJuego, Toast.LENGTH_LONG).show();
    onBackPressed();
  }

  /**
   * Determina si las letras seleccionadas en la Activity permiten obtener una cadena (línea) de
   * carácteres y busca las combinaciones de carácteres en la lógica de la sopa de letras.
   *
   * @param ultimaLetra Coordenadas de la última letra seleccionada en la Activity.
   */
  private void buscarPalabraEnSopa(int[] ultimaLetra) {
    try {
      // La cadena de letras seleccionadas está vacía. Se inserta una nueva letra y se marca
      // como seleccionada.
      if (letrasSeleccionadas.isEmpty()) {
        letrasSeleccionadas.add(ultimaLetra);
        Button botonLetra = encontrarBotonLetra(ultimaLetra[0], ultimaLetra[1]);
        marcarLetraSeleccionada(botonLetra);
      }

      // Búsqueda en sopa de letras
      else {
        marcarLetrasDisponibles(letrasSeleccionadas);
        int[] primeraLetra = letrasSeleccionadas.get(0);

        // Traza una línea de puntos entre el primer y último punto seleccionados.
        ArrayList<int[]> lineaLetras = sopa.trazarLineaCoordenadas(primeraLetra, ultimaLetra);

        // No es posible trazar una línea de letras con la selección actual de letras.
        // Se inicia una nueva secuencia de letras.
        if (lineaLetras == null) {
          Log.d("juegoSopa", "No se puede formar una palabra");
          letrasSeleccionadas.clear();
          Button botonLetraSeleccionada = encontrarBotonLetra(ultimaLetra[0], ultimaLetra[1]);

          if (botonLetraSeleccionada == null) {
            Toast.makeText(this, R.string.error_seleccioneLetraDeNuevo, Toast.LENGTH_SHORT).show();
          } else {
            marcarLetraSeleccionada(botonLetraSeleccionada);
            letrasSeleccionadas.add(ultimaLetra);
          }
        }

        // Se puede trazar una línea de letra. Se intentará buscar la palabra en la sopa de letras.
        // Si la palabra no existe en la sopa, se dejan las letras como seleccionadas.
        else {
          Log.d("juegoSopa", "Se puede formar una palabra");
          letrasSeleccionadas = lineaLetras;
          marcarLetrasSeleccionadas(letrasSeleccionadas);
          String[] combinaciones = sopa.obtenerPalabrasEnTrazo(letrasSeleccionadas);
          buscarCombinacionesLetrasEnSopa(combinaciones);
        }
      }
    } catch (Exception e) {
      Log.e("error_activity", e.getMessage());
      Log.e("error_activity", e.toString());
    }
  }

  /**
   * Busca las palabras generadas por las letras seleccionadas en la lógica de la sopa de letras. Se
   * verifica si alguna de las palabras existe y si ya fue encontrada antes. El resultado de esta
   * función modifica el fondo de los botones en la Activity.
   *
   * @param combinaciones String[] con las combinaciones de palabras.
   */
  private void buscarCombinacionesLetrasEnSopa(String[] combinaciones) {
    try {
      if (combinaciones != null) {
        for (String combinacion : combinaciones) {
          int indicePalabra = sopa.buscarPalabra(combinacion);

          if (indicePalabra >= 0) {
            Log.d("juegoSopa", "Se pudo encontrar una palabra");
            if (!sopa.palabraYaFueEncontrada(indicePalabra)) {
              Log.d("juegoSopa", "Agregar puntos");
              sopa.marcarPalabraComoEncontrada(indicePalabra);
              sopa.incrementarPuntuacion();
              actualizarPuntuacion();
              marcarLetrasUtilizadas(letrasSeleccionadas);
            } else {
              Toast.makeText(this, R.string.error_palabraYaEncontrada, Toast.LENGTH_SHORT).show();
              marcarLetrasDisponibles(letrasSeleccionadas);
            }
            letrasSeleccionadas.clear();
            return;
          }
        }
      }
    } catch (Exception e) {
      Log.e("error_activity", e.getMessage());
      Log.e("error_activity", e.toString());
    }
  }

  /**
   * Coloca las letras de la sopa de letras en pantalla.
   */
  private void llenarMatrizActivity() {
    char[][] letrasMatriz = sopa.getMatrizLetras();

    for (int fila = 0; fila < sopa.getLongitudDiagonal(); fila++) {
      TableRow filaSopa = (TableRow) matrizSopa.getChildAt(fila);
      for (int columna = 0; columna < sopa.getLongitudDiagonal(); columna++) {
        Button botonLetra = (Button) filaSopa.getChildAt(columna);
        String letra = String.valueOf(letrasMatriz[fila][columna]);
        botonLetra.setText(letra);
      }
    }
  }

  /**
   * Inicia el temporizador.
   */
  private void iniciarTemporizador() {
    try {
      temporizador = new CountDownTimer(tiempoInicial_ms, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
          Long tiempo = millisUntilFinished;
          Long minutos = (tiempo / 1000) / 60;
          Long segundos = (tiempo / 1000) % 60;

          String strTiempo = String.format("%02d:%02d", minutos, segundos);
          textoTemporizador.setText(strTiempo);
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

  /**
   * Asigna el tiempo para la sesión de juego. Mayor dificultad implica menos tiempo.
   */
  private void setTiempoInicial_ms(int nivelDificultad) {
    switch (nivelDificultad) {
      // Se agrega un segundo para compensar retrasos de tiempo causados por el SO
      case 1:
        tiempoInicial_ms = 181 * 1000;  // Dificultad fácil empieza con 3 minutos.
        break;
      case 2:
        tiempoInicial_ms = 121 * 1000;  // Dificultad media empieza con 2 minutos.
        break;
      case 3:
        tiempoInicial_ms = 91 * 1000;  // Dificultad difícil empieza con 1 minuto y 30 segundos.
        break;
      default:
        tiempoInicial_ms = 121 * 1000;
        break;
    }
  }

  /**
   * Actualiza la puntuación en pantalla. Se consulta la puntuación almacenada en la lógica de la
   * sopa de letras.
   */
  private void actualizarPuntuacion() {
    String strPuntuacion = Integer.toString(sopa.getPuntuacion());
    textoPuntuacion.setText(strPuntuacion);
  }

  /**
   * Finaliza la sesión de juego y pasa los resultados a la Activity ResultadoJuego.
   */
  private void finalizarJuego() {
    Intent intent = new Intent(this, ResultadoJuego.class);

    // Datos para iniciar una nueva ronda si el usuario lo desea.
    intent.putExtra("puntuacion", sopa.getPuntuacion());
    intent.putExtra("dificultad", sopa.getDificultad());
    intent.putExtra("tipoJuego", sopa.getTipoJuego());
    intent.putExtra("Id", Id);

    finish();
    startActivity(intent);
  }


  /**
   * Se llena la matríz visual (GUI) con los datos de la matríz lógica.
   */
  private void llenarMatrizGrafica() {
    char[][] matrizLetras = sopa.getMatrizLetras();

    // Recorrido a través de filas
    for (int fila = 0; fila < matrizSopa.getChildCount(); fila++) {
      TableRow filaActual = (TableRow) matrizSopa.getChildAt(fila);

      // Recorrido a través de columnas de la fila actual
      for (int columna = 0; columna < filaActual.getChildCount(); columna++) {
        Button botonEncontrado = (Button) filaActual.getChildAt(columna);
        char letra = matrizLetras[fila][columna];
        botonEncontrado.setText(String.valueOf(letra));
      }
    }
  }

  /**
   * Encuentra la letra seleccionada (botón en layout) en la matríz de letras.
   *
   * @param fila    Número de fila.
   * @param columna Número de columna.
   */
  @Nullable
  private Button encontrarBotonLetra(int fila, int columna) {
    try {
      TableRow filaEncontrada = (TableRow) matrizSopa.getChildAt(fila);
      return (Button) filaEncontrada.getChildAt(columna);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Subrutina para cambiar el fondo de un botón seleccionado utilizando la función de API adecuada
   * para diferentes versiones de Android.
   *
   * @param boton              Botón seleccionado.
   * @param identificadorFondo Identificador de recurso Drawable que será usado como fondo.
   */
  private void cambiarColorBoton(Button boton, int identificadorFondo) {
    try {
      // Versiones anteriores a Lollipop (5.0, API 23).
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        boton.setBackgroundDrawable(getResources().getDrawable(identificadorFondo));
      } else {
        boton.setBackground(getDrawable(identificadorFondo));
      }
    } catch (Resources.NotFoundException e) {
      Toast.makeText(this, R.string.error_fondoBoton, Toast.LENGTH_LONG).show();
    }
  }
}
