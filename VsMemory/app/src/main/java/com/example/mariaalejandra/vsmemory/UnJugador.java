package com.example.mariaalejandra.vsmemory;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UnJugador extends AppCompatActivity {

    ArrayList<Integer> secuencia = new ArrayList();
    ArrayList<Integer> presiona2 = new ArrayList();


    Button botones[] = new Button[4];
    private final String TAG = "UnJugador";
    int contador = 1;
    int presionado;

    private ProgressBar mProgress;
    private int mProgressStatus;
    //AlertDialog alertDialogpPerdiste = new AlertDialog.Builder(this).create();

    //private Timer timer = new Timer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_jugador);

        botones[0] = (Button) findViewById(R.id.button1);
        botones[1] = (Button) findViewById(R.id.button2);
        botones[2] = (Button) findViewById(R.id.button3);
        botones[3] = (Button) findViewById(R.id.button4);


        mProgress = (ProgressBar) findViewById(R.id.progressBar);

       //startService(new Intent(getBaseContext(), MyService.class));


        botones[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 0;
                iluminaBoton(0);
                Log.d(TAG, "presiono" + 0);
                presiona2.add(presionado);
            }
        });
        botones[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 1;
                iluminaBoton(1);
                Log.d(TAG, "presiono" + 1);
                presiona2.add(presionado);
            }
        });
        botones[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 2;
                iluminaBoton(2);
                Log.d(TAG, "presiono" + 2);
                presiona2.add(presionado);
            }
        });
        botones[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionado = 3;
                iluminaBoton(3);
                Log.d(TAG, "presiono" + 3);
                presiona2.add(presionado);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("INICAR");
        alertDialog.setMessage("presiona OK para iniciar el juego");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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
        int id = item.getItemId();

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
        iluminaBotones();

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

    public void iluminaBotones(){
        Log.d(TAG, "iluminabotones");
        int oneMin= 1000 * secuencia.size()+600;

        CountDownTimer cdt2 = new CountDownTimer(oneMin, 1000) {
            int i = 0;
            public void onTick(long millisUntilFinished) {
                if (i < secuencia.size()){
                    Log.d(TAG, "i:" +i);
                    iluminaBoton(secuencia.get(i));
                    i++;
                }
            }
            public void onFinish() {
                actualizaBarra();
                esperar(1000 * secuencia.size()+600);
            }
        }.start();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void iluminaBoton(int entero) {

        switch (entero) {
            case 0:
                botones[0].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(500, 0);
                break;
            case 1:
                botones[1].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(500, 1);
                break;
            case 2:
                botones[2].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(500, 2);
                break;
            case 3:
                botones[3].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
                esperarIluminar(500, 3);
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


    private void actualizaBarra(){
        mProgressStatus = 100;

        int oneMin= 1000 * secuencia.size()+600;; // 1 minute in milli seconds

        /** CountDownTimer starts with 1 minutes and every onTick is 1 second */
        CountDownTimer cdt = new CountDownTimer(oneMin, 100) {

            public void onTick(long millisUntilFinished) {
                //mProgressStatus = (int) ((2000/ 60) * 100);
                mProgressStatus = mProgressStatus-10;
                mProgress.setProgress(mProgressStatus);
            }

            public void onFinish() {
                // DO something when 1 minute is up
            }
        }.start();

        /*long length_in_milliseconds = 5000;
        long period_in_milliseconds = 1000;

        CountDownTimer countDownTimer = new CountDownTimer(length_in_milliseconds, period_in_milliseconds) {
            //private boolean warned = false;

            @Override
            public void onTick(long millisUntilFinished_) {
                mProgressStatus = mProgressStatus-20;
                mProgress.setProgress(mProgressStatus);
            }

            @Override
            public void onFinish() {
                // do whatever when the bar is full
            }
        }.start();*/
        
        /*final TimerTask task = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000, velocidiad);*/
        /*final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus >= 0) {
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgressStatus = mProgressStatus -1;
                            esperarBarra(8000);
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }

            }
        }).start();*/

    }
}
