package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ResultadoJuego extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultado_juego);
  }

  private void registrarPuntuacion() {
    // TODO Registrar puntuación
  }

  public void volverAJugar(View view) {
    // TODO Iniciar nuevo juego

    // registrarPuntuacion();
  }

  public void volverAMenuPrincipal(View view) {

    // registrarPuntuacion();

    Intent intent = new Intent(this, MenuPrincipal.class);
    startActivity(intent);
    this.finish();
  }
}
