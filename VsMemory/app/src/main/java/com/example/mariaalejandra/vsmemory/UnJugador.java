package com.example.mariaalejandra.vsmemory;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class UnJugador extends AppCompatActivity {

    ArrayList<Integer> secuencia = new ArrayList();
    ArrayList<Integer> presiona2 = new ArrayList();


    Button botones[] = new Button[4];
    private final String TAG = "UnJugador";
    int contador = 1;
    int presionado;

    private ProgressBar mProgress;
    private int mProgressStatus = 100;
    //AlertDialog alertDialogpPerdiste = new AlertDialog.Builder(this).create();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_jugador);

        botones[0] = (Button) findViewById(R.id.button1);
        botones[1] = (Button) findViewById(R.id.button2);
        botones[2] = (Button) findViewById(R.id.button3);
        botones[3] = (Button) findViewById(R.id.button4);

        //mProgress = (ProgressBar) findViewById(R.id.progressBar);

       //startService(new Intent(getBaseContext(), MyService.class));


        botones[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 0;
                Log.d(TAG, "presiono" + 0);
                presiona2.add(presionado);
            }
        });
        botones[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 1;
                Log.d(TAG, "presiono" + 1);
                presiona2.add(presionado);
            }
        });
        botones[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 2;
                Log.d(TAG, "presiono" + 2);
                presiona2.add(presionado);
            }
        });
        botones[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 3;
                Log.d(TAG, "presiono" + 3);
                presiona2.add(presionado);
            }
        });
 //alertDialog.show();
        //comenzarJuego();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("INICAR");
        alertDialog.setMessage("presiona OK para iniciar el juego");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
// aquí puedes añadir funciones
                comenzarJuego();
            }
        });
        alertDialog.show();
        //comenzarJuego();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_un_jugador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void comenzarJuego(){

        Random random = new Random();

        int r = random.nextInt(4 - 0) + 0;
        secuencia.add(r);
        Log.d(TAG, "Random" + r);
        iluminaBoton(r);

        //int aux = 10;
        //while(secuencia.size() != presiona2.size()){Log.d(TAG, "aun no");}

        //actualizaBarra();
        esperar(8000);
        //Log.d(TAG, "ya son iguales" );
       // confirmar();

    }

    public void esperar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                confirmar();
            }
        }, milisegundos);
    }

    public void esperarIluminar(int milisegundos, final int boton) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                desiluminaBoton(boton);
            }
        }, milisegundos);
    }

    public void esperarBarra(int milisegundos) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
            }
        }, milisegundos);
    }


    public void confirmar(){

        AlertDialog alertDialogpPerdiste = new AlertDialog.Builder(this).create();
        alertDialogpPerdiste.setTitle("PERDISTE!!!");
        alertDialogpPerdiste.setMessage("");
        alertDialogpPerdiste.setButton("Volver", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
// aquí puedes añadir funciones
                perdiste();
            }
        });
        if(secuencia.size() == presiona2.size()){
            int i = 0;
            while (i < presiona2.size()) {
                switch (presiona2.get(i)) {
                    case 0:
                        Log.d(TAG, "" + 0);
                        if (presiona2.get(i) != secuencia.get(i)) {
                            Log.d(TAG, "PERDISTE!!!");
                            alertDialogpPerdiste.show();
                            return;
                        }
                        break;
                    case 1:
                        Log.d(TAG, "" + 1);
                        if (presiona2.get(i) != secuencia.get(i)) {
                            Log.d(TAG, "PERDISTE!!!");
                            alertDialogpPerdiste.show();
                            return;
                        }
                        break;
                    case 2:
                        Log.d(TAG, "" + 2);
                        if (presiona2.get(i) != secuencia.get(i)) {
                            Log.d(TAG, "PERDISTE!!!");
                            alertDialogpPerdiste.show();
                            return;
                        }
                        break;
                    case 3:
                        Log.d(TAG, "" + 3);
                        if (presiona2.get(i) != secuencia.get(i)) {
                            Log.d(TAG, "PERDISTE!!!");
                            alertDialogpPerdiste.show();
                            return;
                        }
                        break;

                }
                i++;

            }
            Log.d(TAG, "Muy Bien!");
            comenzarJuego();
            presiona2.clear();
        }else{
        Log.d(TAG, "PERDISTE!!! TE DEMORASTE MUCHO");
            alertDialogpPerdiste.setTitle("PERDISTE!!!");
            alertDialogpPerdiste.setMessage("Te demoraste mucho!");
            alertDialogpPerdiste.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void iluminaBoton(int entero) {
        switch (entero) {
            case 0:
                botones[0].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(1000, 0);
                break;
            case 1:
                botones[1].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(1000, 1);
                break;
            case 2:
                botones[2].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(1000, 2);
                break;
            case 3:
                botones[3].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(1000, 3);
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void desiluminaBoton(int entero) {
        switch (entero) {
            case 0:
                botones[0].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_default));
                break;
            case 1:
                botones[1].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_default2));
                break;
            case 2:
                botones[2].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_default4));
                break;
            case 3:
                botones[3].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_default3));
                break;
        }
    }

    public void perdiste(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /*private void actualizaBarra(){
        mProgressStatus = 100;
        final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus >= 0) {
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgressStatus = mProgressStatus -1;
                            esperarBarra(800);
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }

            }
        }).start();
    }*/
}
