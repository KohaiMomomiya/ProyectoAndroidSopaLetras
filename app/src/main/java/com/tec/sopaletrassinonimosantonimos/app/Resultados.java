package com.tec.sopaletrassinonimosantonimos.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Resultados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        getPuntajes();
    }

    public void getPuntajes(){
        try {
            getDatos datos  = new getDatos();
            datos.setJson_url("http://proyectosopaletras.esy.es/selectPartidas.php");
            String valores = datos.execute().get();
            JSONObject objetoP = new JSONObject(valores);
            JSONArray listaPuntajes = objetoP.getJSONArray("Puntajes");

            TableLayout tabla = (TableLayout) findViewById(R.id.tablaPuntajes);

            for(int i=0; i<listaPuntajes.length();i++){
                TableRow fila = new TableRow(this);
                TextView tv1 = new TextView(this);
                tv1.setText(listaPuntajes.getJSONObject(i).getString("Nombre"));
                tv1.setTextSize(20);
                tv1.setLeft(20);
                fila.addView(tv1);
                TextView tvespacio = new TextView(this);
                tvespacio.setText("         ");
                fila.addView(tvespacio);
                TextView tv2 = new TextView(this);
                tv2.setText(listaPuntajes.getJSONObject(i).getString("Puntaje"));
                tv2.setTextSize(20);
                tv1.setLeft(20);
                fila.addView(tv2);
                tabla.addView(fila);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
