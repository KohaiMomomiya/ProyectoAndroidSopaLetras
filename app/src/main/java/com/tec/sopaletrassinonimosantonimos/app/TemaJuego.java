package com.tec.sopaletrassinonimosantonimos.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TemaJuego extends AppCompatActivity {
  char temaJuego;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    temaJuego = '0';
    setContentView(R.layout.activity_tema_juego);
  }
}
