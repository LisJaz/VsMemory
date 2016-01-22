package com.example.mariaalejandra.vsmemory;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Level1 extends Fragment {

    ArrayList<Integer> secuencia = new ArrayList();
    ArrayList<Integer> presiona2 = new ArrayList();


    Button botones[] = new Button[4];
    private final String TAG = "UnJugador";
    int contador = 1;
    int presionado;

    private ProgressBar mProgress;
    private int mProgressStatus;

    public Level1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_level1, container, false);


        botones[0] = (Button) view.findViewById(R.id.button1);
        botones[1] = (Button) view.findViewById(R.id.button2);
        botones[2] = (Button) view.findViewById(R.id.button3);
        botones[3] = (Button) view.findViewById(R.id.button4);


        mProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        Activity actividad = getActivity();
        Log.d(TAG, "actividad:" + actividad);

        for(int i = 0; i < 4; i++){
            final int finalI = i;
            botones[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presionado = finalI;
                    iluminaBoton(finalI);
                    Log.d(TAG, "presiono" + finalI);
                    presiona2.add(presionado);
                }
            });
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("INICAR");
        alertDialog.setMessage("Presiona OK para iniciar el juego.");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                comenzarJuego();
            }
        });
        alertDialog.show();
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

        if(secuencia.size() == 6){
            // Create new fragment and transaction
            Fragment newFragment = new Level2();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            return;
        }

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

        AlertDialog alertDialogpPerdiste = new AlertDialog.Builder(getActivity()).create();
        alertDialogpPerdiste.setTitle("PERDISTE!!!");
        alertDialogpPerdiste.setMessage("");
        alertDialogpPerdiste.setIcon(R.drawable.ic_launcher);
        alertDialogpPerdiste.setButton("Volver", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
// aquí puedes añadir funciones
                perdiste();
            }
        });
        if(secuencia.size() == presiona2.size()){
            int i = 0;
            while (i < presiona2.size()) {
                Log.d(TAG, "" + i);
                if (presiona2.get(i) != secuencia.get(i)) {
                    Log.d(TAG, "PERDISTE!!!");
                    alertDialogpPerdiste.show();
                    return;
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
        botones[entero].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
        esperarIluminar(500,entero);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void desiluminaBoton(int entero) {
        switch (entero) {
            case 0:
                botones[0].setBackground(this.getResources().getDrawable(R.drawable.btn_circle));
                break;
            case 1:
                botones[1].setBackground(this.getResources().getDrawable(R.drawable.btn_circle2));
                break;
            case 2:
                botones[2].setBackground(this.getResources().getDrawable(R.drawable.btn_circle3));
                break;
            case 3:
                botones[3].setBackground(this.getResources().getDrawable(R.drawable.btn_circle7));
                break;
        }
    }

    public void perdiste(){
        Intent i = new Intent(getActivity(), MainActivity.class);
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

    }

}
