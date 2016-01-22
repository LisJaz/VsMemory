package com.example.mariaalejandra.vsmemory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void unJugador(View view) {
        Intent i = new Intent(this, UnJugador.class );
        startActivity(i);
    }

    public void multiJugador(View view) {
        Intent i = new Intent(this, MultiJugador.class );
        startActivity(i);
    }

    public void verInstrucciones(View view) {
        Intent i = new Intent(this, InstruccionesActivity.class );
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void salir(){
        //finish();
        System.exit(0);
    }
}
