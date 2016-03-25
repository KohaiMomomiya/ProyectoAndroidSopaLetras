package com.tec.sopaletrassinonimosantonimos.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TemaJuego extends AppCompatActivity {
  int dificultadJuego;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tema_juego);

    dificultadJuego = 0;

    if (savedInstanceState != null) {
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.getString("dificultad") != null)) {
        dificultadJuego = Integer.parseInt(extras.getString("dificultad"));
        Toast.makeText(this, Integer.toString(dificultadJuego), Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, "NONE1", Toast.LENGTH_LONG).show();
      }
    } else {
      Toast.makeText(this, "NONE2", Toast.LENGTH_LONG).show();
    }
  }
}
