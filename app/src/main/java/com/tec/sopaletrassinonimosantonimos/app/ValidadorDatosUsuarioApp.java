package com.tec.sopaletrassinonimosantonimos.app;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Esteban on 16/5/2016.
 */
class ValidadorDatosUsuarioApp implements ValidacionDatosUsuario {
  // Expresiones regulares para validar datos.
  // Usar \\pL para evaluar letras (Unicode).
  private Pattern regexNombres;
  private Pattern regexPwd;
  private Pattern regexEmail;

  ValidadorDatosUsuarioApp() {
    // Se permiten letras, espacios y los símbolos (' . -) para nombres y apellidos.
    // El nombre debe empezar con una letra. Símbolos y espacio deben ser seguidos por letras.
    this.regexNombres = Pattern.compile("^\\p{L}(['.-]?\\s?\\p{L}+['.-]?){1,49} $");

    // Se usa Patrón regex de Android
    this.regexEmail = Patterns.EMAIL_ADDRESS;

    // Contraseñas deben tener entre 4 y 16 caracteres.
    // Se reciben letras alfanuméricas (A-Z, a-z), dígitos numéricos, punto y guión (_).
    this.regexPwd = Pattern.compile("^[A-Za-z0-9\\d._]{4,16}$");
  }

  /**
   * Valida el nombre ingresado por el usuario. El nombre ingresado debe tener entre 2 y 50
   * caracteres (letras, espacios y símbolos permitidos). Usar también para validar apellidos.
   *
   * @param nombre Nombre ingresado.
   * @return Boolean indicando si el nombre es válido.
   */
  @Override
  public boolean validarNombre(String nombre) {
    return regexNombres.matcher(nombre).matches();
  }

  /**
   * Valida la dirección electrónica ingresada por el usuario.
   *
   * @param email Correo electrónico ingresado.
   * @return Boolean indicando si el nombre es válido.
   */
  @Override
  public boolean validarEmail(String email) {
    return (regexEmail.matcher(email).matches());
  }

  /**
   * Valida la contraseña ingresada por el usuario.
   *
   * @param password Contraseña ingresada.
   * @return Boolean indicando si la contraseña es válida.
   */
  @Override
  public boolean validarPwd(String password) {
    return (regexPwd.matcher(password).matches());
  }

}
