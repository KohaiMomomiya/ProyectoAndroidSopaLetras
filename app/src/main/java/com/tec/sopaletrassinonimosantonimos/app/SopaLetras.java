package com.tec.sopaletrassinonimosantonimos.app;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ExecutionException;

class SopaLetras {

  private final int longitudDiagonal;    // Corresponde a medida de ambos lados de la matríz.
  private final int dificultad;
  private final char tipoJuego;
  private int cantPalabras;
  private int puntuacion;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private ArrayList<String> palabrasObjetivo;           // Palabras a buscar
  private ArrayList<String> palabrasCorrespondientes;   // Sinónimos/Antónimos de las palabras buscadas.
  private ArrayList<Boolean> listaIndicadoresPalabras;

  @SuppressWarnings("CanBeFinal")
  private char[][] matrizLetras;
  @SuppressWarnings("CanBeFinal")
  private Random generadorRandom;


  /**
   * Constructor de objeto.
   *
   * @param cantPalabras     Cantidad de palabras en la matríz.
   * @param longitudDiagonal Define la cantidad de filas y columnas en la matríz.
   * @param dificultad       Grado de dificultad de la matríz.
   * @param tipoJuego        Tipo de juego de la matríz: Sinónimos/Antónimos.
   */
  SopaLetras(int cantPalabras, int longitudDiagonal, int dificultad, char tipoJuego) {
    this.longitudDiagonal = longitudDiagonal;
    this.cantPalabras = cantPalabras;
    this.dificultad = dificultad;
    this.tipoJuego = tipoJuego;
    this.puntuacion = 0;

    this.generadorRandom = new Random();

    this.matrizLetras = new char[longitudDiagonal][];

    generarMatrizVacia();
    generarListaPalabras();
    agregarPalabras();
    llenarCeldasVacias();
  }

  /**
   * Obtiene la matríz de letras correspondiente a la sopa de letras.
   *
   * @return Arreglo de dos dimensiones (matríz) con las letras de la sopa de letras.
   */
  char[][] getMatrizLetras() {
    return matrizLetras;
  }

  public ArrayList<String> getPalabrasObjetivo() {
    return palabrasObjetivo;
  }

  public ArrayList<String> getPalabrasCorrespondientes() {
    return palabrasCorrespondientes;
  }

  /**
   * Obtiene el tamaño de la diagonal de la matríz. El tamaño de la diagonal equivale al tamaño
   * (cantidad de celdas) de un lado de la matríz cuadrada.
   *
   * @return Tamaño/Longitud o cantidad de celdas para cualquier lado de la matríz.
   */
  public int getLongitudDiagonal() {
    return longitudDiagonal;
  }

  /**
   * Obtiene la dificultad del juego como un valor numérico.
   *
   * @return Dificultad de juego: 1 (Fácil), 2 (Medio), 3 (Difícil).
   */
  public int getDificultad() {
    return dificultad;
  }

  /**
   * Obtiene el tipo de juego seleccionado.
   *
   * @return Carácter del tipo de juego: 'A' (Antónimos), 'S' (Sinónimos)
   */
  public char getTipoJuego() {
    return tipoJuego;
  }

  /**
   * Retorna la palabra objetivo asociada al valor de índice ingresado.
   *
   * @param indice Índice de la palabra buscada.
   * @return String de la palabra objetivo. Null si el índice ingresado es inválido.
   */
  public String getPalabraObjetivo(int indice) {
    try {
      return palabrasObjetivo.get(indice);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Retorna la palabra correspondiente asociada al valor de índice ingresado.
   *
   * @param indice Indice de la palabra buscada.
   * @return String de la palabra correspondiente. Null si el índice ingresado es inválido.
   */
  public String getPalabraCorrespondiente(int indice) {
    try {
      return palabrasCorrespondientes.get(indice);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Obtiene la puntuación actual del juego.
   *
   * @return Puntuación del juego.
   */
  public int getPuntuacion() {
    return puntuacion;
  }

  /**
   * Incrementa la puntuación actual del juego en función de la dificultad.
   */
  void incrementarPuntuacion() {
    switch (dificultad) {
      case 3:               // Dificultad difícil
        puntuacion += 200;
        break;
      case 2:               // Dificultad media
        puntuacion += 100;
        break;
      default:              // Dificultad fácil/valor por defecto
        puntuacion += 50;
        break;
    }
  }


  /**
   * Busca una palabra en la sopa de letras.
   *
   * @param palabra Palabra a buscar.
   * @return Índice de la palabra encontrada. Si no existe, retorna -1.
   */
  public int buscarPalabra(String palabra) {
    return palabrasCorrespondientes.indexOf(palabra);
  }

  /**
   * Marca una palabra como encontrada.
   *
   * @param indicePalabra Índice de la palabra encontrada (en la lista de palabras
   *                      correspondientes).
   * @return Boolean con resultado de operación.
   */
  boolean marcarPalabraComoEncontrada(int indicePalabra) {
    try {
      listaIndicadoresPalabras.set(indicePalabra, true);
      return true;
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Genera un ArrayList con todas las palabras que hayan sido encontradas.
   *
   * @return ArrayList con objetos String de palabras encontradas.
   */
  public ArrayList<String> getListaPalabrasEncontradas() {
    ArrayList<String> listaPalabrasEncontradas = new ArrayList<String>();
    Iterator<String> iteradorPalabras = palabrasObjetivo.iterator();
    Iterator<Boolean> iteratorIndicadores = listaIndicadoresPalabras.iterator();

    while ((iteradorPalabras.hasNext()) && (iteratorIndicadores.hasNext())) {
      String palabraEncontrada = iteradorPalabras.next();
      boolean indicadorPalabra = iteratorIndicadores.next();

      if (indicadorPalabra) {
        listaPalabrasEncontradas.add(palabraEncontrada);
      }
    }
    return listaPalabrasEncontradas;
  }

  /**
   * Busca una palabra objetivo por medio de su índice y retorna su estado (si fue encontrada).
   *
   * @param indicePalabra Índice de la palabra buscada.
   * @return Estado de la palabra: True si ya fue encontrada; False en caso contrario.
   */
  public boolean palabraYaFueEncontrada(int indicePalabra) {
    try {
      return listaIndicadoresPalabras.get(indicePalabra);
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Busca una palabra objetivo por miedo de su palabra correspondiente y retorna su estado (si fue
   * encontrada).
   *
   * @param correspondiente Palabra correspondiente que será buscada.
   * @return Estado de la palabra: True si ya fue encontrada; False en caso contrario.
   */
  public boolean palabraYaFueEncontrada(String correspondiente) {
    int indice = palabrasCorrespondientes.indexOf(correspondiente);

    if (indice >= 0) {
      return listaIndicadoresPalabras.get(indice);
    }
    correspondiente = new StringBuilder(correspondiente).reverse().toString();
    indice = palabrasCorrespondientes.indexOf(correspondiente);

    if (indice >= 0) {
      return listaIndicadoresPalabras.get(indice);
    }

    return false;
  }

  /**
   * Verifica si todas las palabras en la sopa de letras fueron encontradas.
   *
   * @return Boolean con valor "true" confirmando la verificación, "false" en caso contrario.
   */
  public boolean verificarSopaCompletada() {
    for (boolean registro : listaIndicadoresPalabras) {
      if (!registro) {
        return false;
      }
    }
    return true;
  }

  /**
   * Genera una línea de coordenadas tomando un punto inicial y un punto final en la matríz. Las
   * coordenadas son int[] con dos valores: coordenada X (fila) y coordenada Y (columna).
   *
   * @param punto1 Coordenadas del punto inicial.
   * @param punto2 Coordenadas del punto final.
   * @return ArrayList de int[] con lista de coordenadas para trazar una línea en la matríz. Null si
   * no es posible trazar una línea.
   */
  ArrayList<int[]> trazarLineaCoordenadas(int[] punto1, int[] punto2) {
    // Desplazamientos en coordenadas X y coordenadas Y para el trazo a través de la matríz.
    int desplazamientoX;
    int desplazamientoY;

    // Definición de los desplazamientos:
    // Desplazamiento horizontal
    // 0 (Totalmente vertical), 1 (Arriba-abajo), -1 (Abajo-arriba)
    if (punto1[0] == punto2[0]) {
      desplazamientoX = 0;
    } else {
      desplazamientoX = (punto1[0] < punto2[0]) ? 1 : -1;
    }

    // Desplazamiento vertical
    // 0 (Totalmente vertical), 1 (Izquierda-Derecha), -1 (Derecha-Izquierda)
    if (punto1[1] == punto2[1]) {
      desplazamientoY = 0;
    } else {
      desplazamientoY = (punto1[1] < punto2[1]) ? 1 : -1;
    }

    ArrayList<int[]> listaPuntos = new ArrayList<int[]>();

    try {
      do {
        // La línea trazada sale de los límites de la matríz.
        if ((punto1[0] < 0) || (punto1[0] >= longitudDiagonal)
            || (punto1[1] < 0) || (punto2[1] >= longitudDiagonal)) {
          break;
        } else {
          listaPuntos.add(new int[]{punto1[0], punto1[1]});

          // Luego de agregar el punto en la lista, se realiza desplazamiento al siguiente punto.
          punto1[0] += desplazamientoX;
          punto1[1] += desplazamientoY;
        }
      } while ((punto1[0] != punto2[0]) && (punto1[1] != punto2[1]));

      // Si pudo llegarse al punto final, entonces se retorna la lista de puntos como
      // un ArrayList<int[]>
      if ((punto1[0] == punto2[0]) && (punto1[1] == punto2[1])) {
        listaPuntos.add(punto2);
        return listaPuntos;
      } else {
        return null;
      }
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Genera las dos posibles palabras asociadas a las combinaciones de letras (izquierda-derecha y
   * derecha-izquierda) asociadas a una línea trazada en la matríz, representada como un conjunto de
   * coordenadas.
   *
   * @param listaCoordenadas Lista con las coordenadas de las letras en la matríz.
   * @return Arreglo con dos String con las dos palabras generadas.
   */
  @Nullable
  public String[] obtenerPalabrasEnTrazo(ArrayList<int[]> listaCoordenadas) {
    if ((listaCoordenadas == null) || (listaCoordenadas.size() == 0)) {
      return null;
    }

    try {
      char[] listaLetras = new char[listaCoordenadas.size()];

      for (int contadorLetras = 0; contadorLetras < listaLetras.length; contadorLetras++) {
        int[] coordenadasLetra = listaCoordenadas.get(contadorLetras);
        listaLetras[contadorLetras] = matrizLetras[coordenadasLetra[0]][coordenadasLetra[1]];
      }

      // Combinaciones posibles de letras
      String combinacion1 = new String(listaLetras);
      String combinacion2 = new StringBuilder(combinacion1).reverse().toString();

      return new String[]{combinacion1, combinacion2};
    } catch (NullPointerException e) {
      return null;
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Inserta la palabra ingresada en la matríz de letras siguiendo una línea trazada como una lista
   * de coordenadas.
   *
   * @param palabra          Palabra que será insertada.
   * @param listaCoordenadas Lista de puntos donde se inserta cada letra.
   * @return Indicador de resultado: True si la inserción fue exitosa, False en caso contrario.
   */
  private boolean insertarPalabraEnMatriz(String palabra, ArrayList<int[]> listaCoordenadas) {
    // Verifica que existe una palabra y una lista de coordenadas asociadas a la palabra.
    // La cantidad de coordenadas debe ser la misma que la cantidad de letras de la palabra.
    if ((listaCoordenadas == null) || (listaCoordenadas.size() != palabra.length())) {
      return false;
    } else if (palabra.contentEquals("") || listaCoordenadas.isEmpty()) {
      return false;
    }

    try {
      // Primer recorrido - Revisa que haya celdas disponibles para insertar la palabra.
      for (int contador = 0; contador < palabra.length(); contador++) {
        int[] coordenadasLetra = listaCoordenadas.get(contador);
        char letraEncontrada = matrizLetras[coordenadasLetra[0]][coordenadasLetra[1]];

        // No es posible insertar la palabra si existe una celda ocupada por una letra de otra
        // palabra y no es posible reutilizar la misma letra para ambas palabras.
        if ((letraEncontrada != ' ') && (letraEncontrada != palabra.charAt(contador))) {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }

    // Recorrido final - Inserta las letras de la palabra en la matríz y almacena
    for (int contador = 0; contador < palabra.length(); contador++) {
      int[] coordenadasLetra = listaCoordenadas.get(contador);
      char letraEncontrada = palabra.charAt(contador);
      matrizLetras[coordenadasLetra[0]][coordenadasLetra[1]] = letraEncontrada;
    }
    return true;
  }

  /**
   * Inicializa la matríz de la sopa de letras, colocando un espacio (' ') en todas las celdas.
   */
  private void generarMatrizVacia() {
    for (int fila = 0; fila < longitudDiagonal; fila++) {
      matrizLetras[fila] = new char[longitudDiagonal];
      for (int columna = 0; columna < longitudDiagonal; columna++) {
        matrizLetras[fila][columna] = ' ';
      }
    }
  }

  /**
   * Inserta la lista de palabras correspondientes en la matríz.
   */
  private void agregarPalabras() {
    for (String palabraCorrespondiente : palabrasCorrespondientes) {
      int intentosInsercion = 4;  // Intentos para insertar palabra en matríz.

      while (intentosInsercion > 0) {
        boolean resultado = agregarPalabra(palabraCorrespondiente);
        if (resultado) {
          return;
        } else {
          --intentosInsercion;
        }
        Log.e("error_word_insertion", palabraCorrespondiente);
      }
    }
  }

  /**
   * Inserta una palabra en la matríz de la sopa de letras. Llama a un método auxiliar según el
   * tamaño de la palabra a insertar.
   *
   * @param palabra Palabra que será insertada.
   * @return Boolean confirmando éxito de operación.
   */
  private boolean agregarPalabra(String palabra) {
    // Convierte las letras de la palabra a mayúsculas
    palabra = palabra.toUpperCase();

    if (palabra.length() == longitudDiagonal) {
      return agregarPalabraTransversal(palabra);
    } else {
      return agregarPalabraCorta(palabra);
    }
  }

  /**
   * Agrega una palabra corta a la matríz. Una palabra corta tiene una longitud menor que la
   * diagonal de la matríz.
   *
   * @param palabra Palabra a insertar.
   * @return Boolean con resultado de operación.
   */
  private boolean agregarPalabraCorta(String palabra) {
    if (palabra.length() == longitudDiagonal) {
      return agregarPalabraTransversal(palabra);
    }
    if (palabra.length() > longitudDiagonal) {
      return false;
    }
    if (palabra.length() < 2) {
      return false;
    } else {
      // Coordenadas del punto inicial de la palabra
      int[] puntoInicial = new int[]{
          generadorRandom.nextInt(longitudDiagonal), generadorRandom.nextInt(longitudDiagonal)
      };

      // Línea de coordenadas donde se insertará la palabra
      ArrayList<int[]> listaCoordenadas = null;

      while (listaCoordenadas == null) {
        // Coordenadas del punto final de la palabra
        int[] puntoFinal = new int[]{-1, -1};

        // Selecciones aleatoria para determinar si la palabra se coloca en una sola fila o columna
        boolean mismaFila = (generadorRandom.nextInt(2)) > 0;
        boolean mismaColumna = (generadorRandom.nextInt(2)) > 0;

        if (mismaFila && mismaColumna) {
          continue;
        } else if (mismaFila) {
          puntoFinal[0] = puntoInicial[0];
          puntoFinal[1] = generarCoordenadaFaltantePuntoFinal(puntoInicial[1], palabra);
        } else if (mismaColumna) {
          puntoFinal[1] = puntoInicial[1];
          puntoFinal[0] = generarCoordenadaFaltantePuntoFinal(puntoInicial[0], palabra);
        } else {
          puntoFinal[0] = generarCoordenadaFaltantePuntoFinal(puntoInicial[0], palabra);
          puntoFinal[1] = generarCoordenadaFaltantePuntoFinal(puntoInicial[1], palabra);
        }
        listaCoordenadas = trazarLineaCoordenadas(puntoInicial, puntoFinal);
      }

      return insertarPalabraEnMatriz(palabra, listaCoordenadas);
    }
  }

  /**
   * Agrega una palabra transversal a la matríz. Una palabra transversal tiene una longitud
   * equivalente a la diagonal de la matríz.
   *
   * @param palabra Palabra a insertar.
   * @return Boolean con resultado de operación.
   */
  private boolean agregarPalabraTransversal(String palabra) {
    if (palabra.length() < longitudDiagonal) {
      return agregarPalabraCorta(palabra);
    }
    if (palabra.length() > longitudDiagonal) {
      return false;
    } else {
      int longitudCompacta = palabra.length() - 1;
      ArrayList<int[]> listaCoordenadas = null;

      int[][] combinaciones = new int[][]{{0, 0}, {0, longitudCompacta},
          {longitudCompacta, 0}, {longitudCompacta, longitudCompacta}};

      while (listaCoordenadas == null) {
        int[] puntoInicial = combinaciones[generadorRandom.nextInt(4)];
        int[] puntoFinal = combinaciones[generadorRandom.nextInt(4)];

        while (Arrays.equals(puntoInicial, puntoFinal)) {
          puntoFinal = combinaciones[generadorRandom.nextInt(4)];
        }

        listaCoordenadas = trazarLineaCoordenadas(puntoInicial, puntoFinal);
      }
      return insertarPalabraEnMatriz(palabra, listaCoordenadas);
    }
  }

  /**
   * Genera la coordenada final en función de una coordenada inicial y la palabra que desea
   * insertarse. La coordenada final puede ser menor o mayor que la coordenada inicial.
   *
   * @param coordenadaInicial Valor de la coordenada en el punto de inicio.
   * @param palabra           Palabra a ser insertada.
   * @return Valor de la coordenada en el punto final.
   */
  private int generarCoordenadaFaltantePuntoFinal(int coordenadaInicial, String palabra) {
    int longitudCompacta = palabra.length() - 1;

    int[] posiblesCoordenadas = new int[]{
        coordenadaInicial - longitudCompacta, coordenadaInicial + longitudCompacta
    };

    // No se puede colocar antes que la coordenada del punto inicial
    if (posiblesCoordenadas[0] < 0) {
      return posiblesCoordenadas[1];
    } else if (posiblesCoordenadas[1] >= longitudDiagonal) {
      return posiblesCoordenadas[0];
    } else {
      return posiblesCoordenadas[generadorRandom.nextInt(2)];
    }
  }

  /**
   * Inserta caracteres al azar en todas las celdas vacías de la matríz.
   */
  private void llenarCeldasVacias() {
    for (int i = 0; i < longitudDiagonal; i++) {
      for (int j = 0; j < longitudDiagonal; j++) {
        if (matrizLetras[i][j] == ' ') {
          char letra = (char) ('A' + generadorRandom.nextInt(26));
          matrizLetras[i][j] = letra;
        }
      }
    }
  }


  /**
   *
   */
  private void generarListaPalabras() {
    String juego;
    String nivel;

    if (tipoJuego == 'A') {
      juego = "ANTONIMO";
    } else {
      juego = "SINONIMO";
    }

    if (dificultad > 0) {
      nivel = Integer.toString(dificultad);
    } else {
      nivel = "1";
    }

    String numPalabras = Integer.toString(cantPalabras);

    try {
      getDatos datos = new getDatos();
      datos.setJson_url("http://proyectosopaletras.esy.es/selectPalabras.php?" +
          "juego=" + juego +
          "&dificultad=" + nivel +
          "&limite=" + numPalabras);

      String valores = datos.execute().get();

      JSONObject objetoPrincipal = new JSONObject(valores);
      JSONArray lista = objetoPrincipal.getJSONArray("Palabras");

      // Si el array JSON tiene menos palabras que la cantidad esperada, se cambia dicha cantidad.
      if (lista.length() < cantPalabras) {
        cantPalabras = lista.length();
      }

      // Se crean las listas de palabras
      palabrasObjetivo = new ArrayList<String>();
      palabrasCorrespondientes = new ArrayList<String>();
      listaIndicadoresPalabras = new ArrayList<Boolean>();


      for (int i = 0; i < cantPalabras; i++) {
        JSONObject objetoIndividual = lista.getJSONObject(i);
        palabrasObjetivo.add(objetoIndividual.getString("Palabra"));
        palabrasCorrespondientes.add(objetoIndividual.getString(juego));
        listaIndicadoresPalabras.add(false);
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
