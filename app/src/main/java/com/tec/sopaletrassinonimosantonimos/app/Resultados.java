package com.tec.sopaletrassinonimosantonimos.app;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Resultados extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultados);

    ArrayList<String[]> puntuaciones = obtenerPuntuaciones();
    if (puntuaciones != null) {
      mostrarResultadosEnPantalla(puntuaciones);
    } else {
      onBackPressed();
    }
  }

  @Nullable
  private ArrayList<String[]> obtenerPuntuaciones() {
    try {
      String urlPuntajes = "http://proyectosopaletras.esy.es/selectPartidas.php";
      String valores = new SolicitanteWeb(this, urlPuntajes).execute().get();

      JSONObject objetoP = new JSONObject(valores);
      JSONArray listaPuntajes = objetoP.getJSONArray("Puntajes");

      TableLayout tabla = (TableLayout) findViewById(R.id.tablaPuntajes);
      ArrayList<String[]> arrayListPuntajes = new ArrayList<String[]>();


      for (int indice = 0; indice < listaPuntajes.length(); indice++) {
        try {
          arrayListPuntajes.add(new String[]{
              listaPuntajes.getJSONObject(indice).getString("Nombre"),
              listaPuntajes.getJSONObject(indice).getString("Puntaje")
          });
        } catch (JSONException e) {
          Log.e("Puntuaciones-", "Error en parsing de JSON, indice: " + Integer.toString(indice));
        }
      }

      return arrayListPuntajes;

    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(this, R.string.error_PuntuacionesNoDisponibles, Toast.LENGTH_LONG).show();
      return null;
    }
  }

  private void mostrarResultadosEnPantalla(ArrayList<String[]> resultados) {
    try {
      TableLayout tableLayout = (TableLayout) findViewById(R.id.tablaPuntajes);


      // Atributos para cada TableRow
      TableRow.LayoutParams rowParams = new TableRow.LayoutParams
          (TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);

      // Atributos para cada View en cada TableRow
      TableRow.LayoutParams colParams = new TableRow.LayoutParams
          (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
      colParams.weight = 1;


      for (int fila = 0; fila < resultados.size(); fila++) {
        String[] datosFila = resultados.get(fila);

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(rowParams);


        for (int columna = 0; columna < datosFila.length; columna++) {
          TextView textView = new TextView(this);
          textView.setText(datosFila[columna]);
          textView.setLayoutParams(colParams);
          textView.setGravity((columna > 0) ? Gravity.END : Gravity.START);

          // Asigna color a textView según versión de Android
          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textView.setTextAppearance(this, R.style.TextoPuntuacion);
          } else {
            textView.setTextAppearance(R.style.TextoPuntuacion);
          }

          tableRow.addView(textView, columna);
        }

        if (tableLayout != null) {
          tableLayout.addView(tableRow, fila);
        }
      }
    } catch (Exception e) {
      Toast.makeText(this, R.string.error_PuntuacionesNoDisponibles, Toast.LENGTH_LONG).show();
      onBackPressed();
    }
  }
}
