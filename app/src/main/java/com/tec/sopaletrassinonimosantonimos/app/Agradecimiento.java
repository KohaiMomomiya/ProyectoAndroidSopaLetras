package com.tec.sopaletrassinonimosantonimos.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Agradecimiento extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agradecimiento);
  }

  @Override
  public void onBackPressed() {
    finish();
  }
}
