package com.tec.sopaletrassinonimosantonimos.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SopaLetras {

  private int cantPalabras;
  private int longitudDiagonal;    // Corresponde a medida de ambos lados de la matríz.

  private char dificultad;
  private char tipoJuego;

  private int horizontal;
  private int vertical;
  private int diagonal_izquierda_derecha;
  private int alreves;

  private String[] listaPalabras;          // Palabra a buscar
  private String[] listaCorrespondientes;   // Sinónimo/Antónimo de palabra
  private boolean[] palabrasEncontradas;

  private char[][] matrizLetras;
  private int[][] coordenadasPalabras;   // [x1, y1, x2, y2]


  //constructor
  public SopaLetras(int cantPalabras, int longitudDiagonal, char dificultad, char tipoJuego) {
    this.longitudDiagonal = longitudDiagonal;
    this.cantPalabras = cantPalabras;
    this.dificultad = dificultad;
    this.tipoJuego = tipoJuego;

    this.matrizLetras = new char[longitudDiagonal][];

    horizontal = 0;
    vertical = 1;
    diagonal_izquierda_derecha = 2;
    alreves = 1;

    generarMatrizVacia();
    generarListaPalabras();
    agregarPalabrasAmatriz();
    completarSopa();
  }

  public char[][] getMatrizLetras() {
    return matrizLetras;
  }

  public String[] getListaPalabras() {
    return listaPalabras;
  }

  public String[] getPalabrasCorrespondientes() {
    return listaCorrespondientes;
  }

  public int[][] getCoordenadasPalabras() {
    return coordenadasPalabras;
  }

  public int[] getCoordenadasPalabra(String palabra) {
    for (int i = 0; i < listaPalabras.length; i++) {
      if (palabra.contentEquals(listaPalabras[i])) {
        // coordenadasPalabras[i] = [x1, y1, x2, y2] (primer punto, segundo punto)
        return coordenadasPalabras[i];
      }
    }
    return new int[]{-1, -1, -1, -1};  // Error
  }

  public boolean[] getRegistroPalabrasEncontradas() {
    return palabrasEncontradas;
  }

  public boolean palabraYaFueEncontrada(int indicePalabra) {
    return palabrasEncontradas[indicePalabra] == true;
  }

  public boolean verificarSopaCompletada() {
    for (boolean registro : palabrasEncontradas) {
      if (registro == false) {
        return false;
      }
    }
    return true;
  }

  public String[] getListaCorrespondientes() {
    return listaCorrespondientes;
  }

  public void setListaCorrespondientes(String[] listaCorrespondientes) {
    this.listaCorrespondientes = listaCorrespondientes;
  }

  /**
   * Obtiene el valor índice de la palabra encontrada entre los siguientes puntos.
   *
   * @param x1 Coordenada de fila del punto 1
   * @param y1 Coordenada de columna del punto 1
   * @param x2 Coordenada de fila del punto 2
   * @param y2 Coordenada de columna del punto 2
   * @return Indice de palabra encontrada
   */
  public int encontrarIndicePalabraPorCoordenadas(int x1, int y1, int x2, int y2) {
    for (int contador = 0; contador < cantPalabras; contador++) {
      int[] coordenadasEncontradas = coordenadasPalabras[contador];

      if ((coordenadasEncontradas[0] == x1) && (coordenadasEncontradas[1] == y1)
          && (coordenadasEncontradas[2] == x2) && (coordenadasEncontradas[3] == y2)) {
        return contador;
      }
      if ((coordenadasEncontradas[0] == x2) && (coordenadasEncontradas[1] == y2)
          && (coordenadasEncontradas[2] == x1) && (coordenadasEncontradas[3] == y1)) {
        return contador;
      }
    }
    return -1;    // No se pudo encontrar
  }

  public String getPalabra(int indice) {
    return listaPalabras[indice];
  }

  public String getPalabraCorrespondiente(int indice) {
    return listaCorrespondientes[indice];
  }


  public void marcarPalabraComoEncontrada(int indice) {
    palabrasEncontradas[indice] = true;
  }

  /**
   * Verifica si dos puntos están en la misma fila (línea horizontal)
   *
   * @param x1 Fila del punto 1
   * @param x2 Fila del punto 2
   * @return Indicador de verdad
   */
  public boolean verificarPuntosEnMismaFila(int x1, int x2) {
    return x1 == x2;
  }

  /**
   * Verifica si dos puntos están en la misma columna (línea vertical)
   *
   * @param y1 Columna del punto 1
   * @param y2 Columna del punto 2
   * @return Indicador de verdad
   */
  public boolean verificarPuntosEnMismaColumna(int y1, int y2) {
    return y1 == y2;
  }

  /**
   * Verifica si dos puntos están en la misma línea diagonal
   *
   * @param x1 Fila del punto 1
   * @param y1 Columna del punto 1
   * @param x2 Fila del punto 2
   * @param y2 Columna del punto 2
   * @return Indicador de verdad
   */
  public boolean verificarPuntosEnDiagonal(int x1, int y1, int x2, int y2) {
    int movimiento_x;
    int movimiento_y;

    if (x1 < x2) {
      movimiento_x = 1;
      if (y1 < y2) {
        movimiento_y = 1;
      } else {
        movimiento_y = -1;
      }
    } else {
      movimiento_x = -1;
      if (y2 < y1) {
        movimiento_y = -1;
      } else {
        movimiento_y = 1;
      }
    }

    while ((x1 >= 0) && (x1 < longitudDiagonal) && (y1 >= 0) && (y1 < longitudDiagonal)) {
      if ((x1 == x2) && (y1 == y2)) {
        return true;
      } else {
        x1 += movimiento_x;
        y1 += movimiento_y;
      }
    }
    return false;
  }

  //crea una sopa con espacios en blanco

  private void generarMatrizVacia() {
    for (int i = 0; i < longitudDiagonal; i++) {
      matrizLetras[i] = new char[longitudDiagonal];
      for (int j = 0; j < longitudDiagonal; j++) {
        matrizLetras[i][j] = ' ';
      }
    }
  }

  //agrega las palabras de la lista a la sopa de letras

  private void agregarPalabrasAmatriz() {
    for (int indice = 0; indice < listaPalabras.length; indice++) {
      agregarPalabraAmatriz(listaCorrespondientes[indice], indice);
    }
  }

  //agrega las palabras en la sopa
  private void agregarPalabraAmatriz(String palabra, int indice) {

    //pasa la palabra a mayúscula
    palabra = palabra.toUpperCase();

    //crea un objeto random (me da un número aleatorio)
    Random random = new Random();

    int fila = 0;
    int columna = 0;

    //contador para verificar que la palabra ya se ha terminado de agregar toda
    int contador_letras = 0;

    //verifica que la palabra se ha agregado
    int bandera = longitudDiagonal;

    while (bandera > 0) {

      //orientación de la palabra
      int orientacion = random.nextInt(2);

      //dirección de la palabra
      int direccion = random.nextInt(3);

      if (orientacion == alreves) {

        //Pasa la palabra al revés
        StringBuilder builder = new StringBuilder(palabra);
        palabra = builder.reverse().toString();
      } else if (direccion == horizontal) {

        fila = random.nextInt(longitudDiagonal);
        columna = random.nextInt(longitudDiagonal - palabra.length());
      } else if (direccion == vertical) {

        fila = random.nextInt(longitudDiagonal - palabra.length());
        columna = random.nextInt(longitudDiagonal);
      } else if (direccion == diagonal_izquierda_derecha) {

        fila = random.nextInt(longitudDiagonal - palabra.length());
        columna = random.nextInt(longitudDiagonal - palabra.length());
      }

      int[] nuevasCoordenadas = new int[]{fila, columna, 0, 0};

      for (int i = 0; i < palabra.length(); i++) {

        //verifica que no haya nada en esa posicion
        if (matrizLetras[fila][columna] == ' ') {

          //pone en esa posición la letra
          matrizLetras[fila][columna] = palabra.charAt(i);
          contador_letras++;


          if ((direccion == horizontal) && (i + 1 < palabra.length())) {
            // se posiciona en la siguiente columna
            columna++;
          } else if ((direccion == vertical) && (i + 1 < palabra.length())) {  // se posiciona en la siguiente fila
            fila++;
          } else if ((direccion == diagonal_izquierda_derecha) && (i + 1 < palabra.length())) {
            //se mueve hacia la siguiente fila y hacia la siguiente columna
            columna++;
            fila++;
          }
        } else {  // ingresa aquí cuando ya hay una palabra en cierta posición
          contador_letras = 0;
          break;
        }
      }

      // Si la palabra completa fue insertada, se detiene la iteración
      if (contador_letras == palabra.length()) {
        nuevasCoordenadas[2] = fila;
        nuevasCoordenadas[3] = columna;
        coordenadasPalabras[indice] = nuevasCoordenadas;

        bandera--;
        break;
      }
    }
  }

  //completa el resto de la sopa

  private void completarSopa() {

    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();

    for (int i = 0; i < longitudDiagonal; i++) {
      for (int j = 0; j < longitudDiagonal; j++) {
        if (matrizLetras[i][j] == ' ') {
          matrizLetras[i][j] = characters.charAt(random.nextInt(characters.length()));
        }
      }
    }
  }

  private void generarListaPalabras() {
    String juego;
    String nivel;

    if (tipoJuego == 'a') {
      juego = "ANTONIMO";
    } else {
      juego = "SINONIMO";
    }

    switch (dificultad) {
      case 'a':
        nivel = "1";
        break;
      case 'b':
        nivel = "2";
        break;
      case 'c':
        nivel = "3";
        break;
      default:
        nivel = "1";
        break;
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
      listaPalabras = new String[cantPalabras];
      listaCorrespondientes = new String[cantPalabras];
      coordenadasPalabras = new int[cantPalabras][];
      palabrasEncontradas = new boolean[cantPalabras];

      for (int i = 0; i < cantPalabras; i++) {
        JSONObject objetoIndividual = lista.getJSONObject(i);
        listaPalabras[i] = objetoIndividual.getString("Palabra");
        listaCorrespondientes[i] = objetoIndividual.getString(juego);
        palabrasEncontradas[i] = false;
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    String contenido = "";

    for (char[] fila : matrizLetras) {
      for (char columna : fila) {
        contenido += columna;
      }
      contenido += '\n';
    }

    return contenido;
  }
}
