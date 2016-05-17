package com.tec.sopaletrassinonimosantonimos.app;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class SolicitanteWeb extends AsyncTask<Void, Void, String> {
  private String json_url;
  private Context context;

  SolicitanteWeb(Context context, String jsonUrl) {
    this.context = context;
    this.json_url = jsonUrl;
  }

  @Override
  protected String doInBackground(Void... params) {
    try {
      URL url = new URL(json_url);
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

      InputStream inputStream = httpURLConnection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder stringBuilder = new StringBuilder();
      String JSON_String;

      while ((JSON_String = bufferedReader.readLine()) != null) {
        stringBuilder.append(JSON_String);
        stringBuilder.append("\n");
      }

      bufferedReader.close();
      inputStream.close();
      httpURLConnection.disconnect();
      return stringBuilder.toString().trim();
    } catch (ConnectException e) {
      Toast.makeText(context, R.string.error_ConexionNoPosible, Toast.LENGTH_LONG).show();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
  }
}
