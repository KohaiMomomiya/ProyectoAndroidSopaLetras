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
  private int direccion;
  private int orientacion;

  private String[] listaPalabras;          // Palabra a buscar
  private String[] correspondiente;   // Sinónimo/Antónimo de palabra
  private boolean[] palabrasEncontradas;

  private char[][] matrizLetras;
  private int[][] coordenadasPalabras;   // [x1, y1, x2, y2]


  //constructor
  public SopaLetras(int cantPalabras, int longitudDiagonal, char dificultad, char tipoJuego) {
    this.longitudDiagonal = longitudDiagonal;
    this.cantPalabras = cantPalabras;
    this.dificultad = dificultad;
    this.tipoJuego = tipoJuego;

    this.matrizLetras = new char[longitudDiagonal][longitudDiagonal];
    this.coordenadasPalabras = null;

    horizontal = 0;
    vertical = 1;
    diagonal_izquierda_derecha = 2;
    alreves = 1;

    generarMatrizVacia();
    generarListaPalabras();
    agregarPalabras();
    completarSopa();
  }

  public char[][] getMatrizLetras() {
    return matrizLetras;
  }

  public String[] getListaPalabras() {
    return listaPalabras;
  }

  public String[] getPalabrasCorrespondientes() {
    return correspondiente;
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


  //crea una sopa con espacios en blanco

  private void generarMatrizVacia() {

    for (int i = 0; i < longitudDiagonal; i++) {
      for (int j = 0; j < longitudDiagonal; j++) {
        matrizLetras[i][j] = ' ';
      }
    }
  }

  //agrega las palabras de la lista a la sopa de letras

  private void agregarPalabras() {
    for (String palabra : listaPalabras) {
      agregarPalabra(palabra);
    }
  }

  //agrega las palabras en la sopa
  private void agregarPalabra(String palabra) {

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
      orientacion = random.nextInt(2);

      //dirección de la palabra
      direccion = random.nextInt(3);

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

      for (int i = 0; i < palabra.length(); i++) {

        //verifica que no haya nada en esa posicion
        if (matrizLetras[fila][columna] == ' ') {

          //pone en esa posición la letra
          matrizLetras[fila][columna] = palabra.charAt(i);
          contador_letras++;


          if (direccion == horizontal) {  // se posiciona en la siguiente columna
            columna++;
          } else if (direccion == vertical) {  // se posiciona en la siguiente fila
            fila++;
          } else if (direccion == diagonal_izquierda_derecha) {
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

    int numeroPalabras = 8;

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

    String numPalabras = Integer.toString(numeroPalabras);

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
      if (lista.length() < longitudDiagonal) {
        longitudDiagonal = lista.length();
      }

      // Se crean las listas de palabras
      listaPalabras = new String[numeroPalabras];
      correspondiente = new String[numeroPalabras];
      palabrasEncontradas = new boolean[numeroPalabras];

      for (int i = 0; i < longitudDiagonal; i++) {
        JSONObject objetoIndividual = lista.getJSONObject(i);
        listaPalabras[i] = objetoIndividual.getString("Palabra");
        correspondiente[i] = objetoIndividual.getString(juego);
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