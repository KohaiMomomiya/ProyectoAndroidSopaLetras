package com.tec.sopaletrassinonimosantonimos.app;

/**
 * Created by Esteban on 16/5/2016.
 */
interface ValidacionDatosUsuario {
  boolean validarNombre(String nombre);

  boolean validarEmail(String email);

  boolean validarPwd(String password);
}
