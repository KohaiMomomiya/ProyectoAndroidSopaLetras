package com.tec.sopaletrassinonimosantonimos.app;

import java.util.Random;

public class Sopa_de_Letras {

  public int tamaño_sopa;
  public char[][] matrizLetras;
  private int horizontal;
  private int vertical;
  private int diagonal_izquierda_derecha;
  private int alreves;
  private int direccion;
  private int orientacion;
  private String[] listaPalabras;
  private boolean[] palabrasEncontradas;

  //constructor
  public Sopa_de_Letras(int tamaño_sopa, String[] listaPalabras) {

    this.tamaño_sopa = tamaño_sopa;
    horizontal = 0;
    vertical = 1;
    diagonal_izquierda_derecha = 2;
    alreves = 1;
    this.listaPalabras = listaPalabras;
    matrizLetras = new char[tamaño_sopa][tamaño_sopa];

  }

  public char[][] getSopa_letras() {
    return matrizLetras;
  }

  //crea una sopa con espacios en blanco

  public void sopa_en_blanco(Sopa_de_Letras sopa) {

    for (int i = 0; i < matrizLetras.length; i++) {
      for (int j = 0; j < matrizLetras.length; j++) {
        sopa.matrizLetras[i][j] = ' ';
      }
    }
  }

  //agrega las palabras de la lista a la sopa de letras

  public void agregar_palabras(Sopa_de_Letras sopa) {

    for (int i = 0; i < sopa.listaPalabras.length; i++) {

      agregar_palabra(sopa.listaPalabras[i], sopa.matrizLetras, sopa.tamaño_sopa);
      System.out.println(listaPalabras[i] + "se agregó");
    }

  }

  //agrega las palabras en la sopa
  public void agregar_palabra(String palabra, char sopa_letras[][], int tamaño_sopa) {

    //pasa la palabra a mayúscula
    palabra = palabra.toUpperCase();

    //crea un objeto random (me da un número aleatorio)
    Random random = new Random();

    int fila = 0;
    int columna = 0;

    //contador para verificar que la palabra ya se ha terminado de agregar toda
    int contador_letras = 0;

    //verifica que la palabra se ha agregado
    int bandera = tamaño_sopa;

    while (bandera != 0) {

      //orientación de la palabra
      orientacion = random.nextInt(2);

      //dirección de la palabra
      direccion = random.nextInt(3);

      if (orientacion == alreves) {

        //Pasa la palabra al revés
        StringBuilder builder = new StringBuilder(palabra);
        palabra = builder.reverse().toString();
        System.out.println("La palabra al revés es:" + palabra);
      }
      if (direccion == horizontal) {

        fila = random.nextInt(tamaño_sopa);
        columna = random.nextInt(tamaño_sopa - palabra.length());
      } else if (direccion == vertical) {

        fila = random.nextInt(tamaño_sopa - palabra.length());
        columna = random.nextInt(tamaño_sopa);
      } else if (direccion == diagonal_izquierda_derecha) {

        fila = random.nextInt(tamaño_sopa - palabra.length());
        columna = random.nextInt(tamaño_sopa - palabra.length());
      }

      for (int i = 0; i < palabra.length(); i++) {

        //verifica que no haya nada en esa posicion
        if (sopa_letras[fila][columna] == ' ') {

          //pone en esa posición la letra
          sopa_letras[fila][columna] = palabra.charAt(i);

          contador_letras++;

          //se posiciona en la siguiente columna
          if (direccion == horizontal)

            columna++;

          //se posiciona en la siguiente fila
          if (direccion == vertical)

            fila++;

          //se mueve hacia la siguiente fila y hacia la siguiente columna
          if (direccion == diagonal_izquierda_derecha) {

            columna++;
            fila++;
          }

        }

        //ingresa aquí cuando ya hay una palabra en cierta posición
        else {

          contador_letras = 0;
          break;
        }
      }

      //indica si ya la palabra se agregó toda, si sí, entonces se detiene para seguir con la otra palabra
      if (contador_letras == palabra.length()) {

        bandera--;
        break;
      }
    }
  }


  //completa el resto de la sopa

  public char[][] completar_sopa(Sopa_de_Letras sopa) {

    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();

    for (int i = 0; i < sopa.tamaño_sopa; i++) {
      for (int j = 0; j < sopa.tamaño_sopa; j++) {

        if (sopa.matrizLetras[i][j] == ' ') {

          sopa.matrizLetras[i][j] = characters.charAt(random.nextInt(characters.length()));

        }

      }
    }
    return matrizLetras;
  }

  public void imprimir(Sopa_de_Letras sopa) {

    //System.out.println("matrizLetras\n");
    for (int i = 0; i < sopa.tamaño_sopa; i++) {
      for (int j = 0; j < sopa.tamaño_sopa; j++) {
        System.out.print(sopa.matrizLetras[i][j] + " ");
      }
      System.out.print("\n");
    }

  }

}
