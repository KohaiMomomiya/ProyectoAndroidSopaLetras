package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultadoJuego extends AppCompatActivity {
<<<<<<< HEAD
=======
  private TextView textoResultadoPuntuacion;
  String Id;
  String puntaje;
>>>>>>> 0d2f76fcdb645ee7944e1d8a266559d4184455b3

  private int dificultad;  // 1 : Fácil, 2 : Media, 3 : Difícil
  private char tipoJuego;  // A : Antónimos, S : Sinónimos


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultado_juego);

    TextView textoResultadoPuntuacion = (TextView) findViewById(R.id.textoResultadoPuntuacion);
    tipoJuego = '-';
    dificultad = '-';

    Intent intent = getIntent();
    Id = intent.getStringExtra("Id");


    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if ((extras != null) && (extras.containsKey("puntuacion"))) {
<<<<<<< HEAD
        String strPuntuacion = Integer.toString(extras.getInt("puntuacion"));
        if (textoResultadoPuntuacion != null) {
          textoResultadoPuntuacion.setText(strPuntuacion);
        }
=======
        puntaje = Integer.toString(extras.getInt("puntuacion"));
        textoResultadoPuntuacion.setText(puntaje);
>>>>>>> 0d2f76fcdb645ee7944e1d8a266559d4184455b3
        registrarPuntuacion();
      }
      if ((extras != null) && (extras.containsKey("dificultad"))) {
        dificultad = extras.getInt("dificultad");
      }
      if ((extras != null) && (extras.containsKey("tipoJuego"))) {
        tipoJuego = extras.getChar("tipoJuego");
      }
    }
  }

  private void registrarPuntuacion() {
    try{
      getDatos datos = new getDatos();
      datos.setJson_url("http://proyectosopaletras.esy.es/registrarPuntuacion.php?" +
              "id="+Id+
              "&puntuacion="+puntaje);
      String valores = datos.execute().get();
      if(valores.equals("New record created successfully")){
        Toast.makeText(this,"Puntuacion registrada con exito", Toast.LENGTH_LONG).show();
      }else{
        Toast.makeText(this,"No se pudo registrar la puntuacion", Toast.LENGTH_LONG).show();
      }
    }catch(Exception e){

    }
  }

  public void volverAJugar(View view) {
<<<<<<< HEAD
    try {
      Intent intent = new Intent(this, ActividadJuego.class);
      intent.putExtra("dificultad", dificultad);
      intent.putExtra("tipoJuego", tipoJuego);
      startActivity(intent);
      finish();
    } catch (Exception e) {
      errorVolverAJugar();
    }
=======
    Intent intent = new Intent(this, ActividadJuego.class);
    intent.putExtra("dificultad", dificultad);
    intent.putExtra("tipoJuego", tipoJuego);
    intent.putExtra("Id",Id);
    finish();
    startActivity(intent);
>>>>>>> 0d2f76fcdb645ee7944e1d8a266559d4184455b3
  }

  public void onBackPressed() {
    Intent intent = new Intent(this, MenuPrincipal.class);
    startActivity(intent);
    finish();
  }

  public void volverAMenuPrincipal(View view) {
    onBackPressed();
  }

  private void errorVolverAJugar() {
    Toast.makeText(this, R.string.error_volverAJugar, Toast.LENGTH_LONG).show();
    onBackPressed();
  }
}
