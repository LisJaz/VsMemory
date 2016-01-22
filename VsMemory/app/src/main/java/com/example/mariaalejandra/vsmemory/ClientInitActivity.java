package com.example.mariaalejandra.vsmemory;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

public class ClientInitActivity extends ActionBarActivity {

    static final int SocketServerPORT = 8080;

    LinearLayout loginPanel, nivel1, nivel2, nivel3;

    EditText editTextClave;
    Button buttonConnect;

    String msgLog = "";

    ChatClientThread chatClientThread = null;


    Button botones[] = new Button[18];
    int presionado;
    ArrayList<Integer> secuencia = new ArrayList();
    ArrayList<Integer> presiona2 = new ArrayList();
    private final String TAG = "UnJugador";
    private ProgressBar mProgress1,  mProgress2,  mProgress3;
    private int mProgressStatus;

    int nivel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);

        loginPanel = (LinearLayout)findViewById(R.id.loginpanel);
        nivel1 = (LinearLayout)findViewById(R.id.nivel1);
        nivel2 = (LinearLayout)findViewById(R.id.nivel2);
        nivel3 = (LinearLayout)findViewById(R.id.nivel3);

        editTextClave = (EditText) findViewById(R.id.clave);

        buttonConnect = (Button) findViewById(R.id.connect);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        mProgress1 = (ProgressBar) findViewById(R.id.progressBar1);
        mProgress2 = (ProgressBar) findViewById(R.id.progressBar2);
        mProgress3 = (ProgressBar) findViewById(R.id.progressBar3);
        botones[0] = (Button) findViewById(R.id.button1);
        botones[1] = (Button) findViewById(R.id.button2);
        botones[2] = (Button) findViewById(R.id.button3);
        botones[3] = (Button) findViewById(R.id.button4);
        botones[4] = (Button) findViewById(R.id.button5);
        botones[5] = (Button) findViewById(R.id.button6);
        botones[6] = (Button) findViewById(R.id.button7);
        botones[7] = (Button) findViewById(R.id.button8);
        botones[8] = (Button) findViewById(R.id.button9);
        botones[9] = (Button) findViewById(R.id.button10);
        botones[10] = (Button) findViewById(R.id.button11);
        botones[11] = (Button) findViewById(R.id.button12);
        botones[12] = (Button) findViewById(R.id.button13);
        botones[13] = (Button) findViewById(R.id.button14);
        botones[14] = (Button) findViewById(R.id.button15);
        botones[15] = (Button) findViewById(R.id.button16);
        botones[16] = (Button) findViewById(R.id.button17);
        botones[17] = (Button) findViewById(R.id.button18);



        for(int i = 0; i < 18; i++){
            final int finalI = i;
            botones[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presionado = finalI;
                    iluminaBoton(finalI);
                    if(chatClientThread==null){
                        return;
                    }

                    //chatClientThread.sendMsg(Integer.toString(finalI));
                    Log.d(TAG, "presiono" + finalI);
                    presiona2.add(presionado);
                }
            });
        }
    }

   public void desconectar() {
            if(chatClientThread==null){
                return;
            }
            chatClientThread.disconnect();
    }

    OnClickListener buttonConnectOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String textUserName = "jugador 2";
            if (textUserName.equals("")) {
                Toast.makeText(ClientInitActivity.this, "Enter User Name",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String textClave = editTextClave.getText().toString();
            if (textClave.equals("")) {
                Toast.makeText(ClientInitActivity.this, "Ingresa la clave",
                        Toast.LENGTH_LONG).show();
                return;
            }
            textClave = reemplazarClave(getIpAddress(),textClave);

            hideSoftKeyboard(ClientInitActivity.this);
            msgLog = "";

            chatClientThread = new ChatClientThread(
                    textUserName, textClave, SocketServerPORT);
            chatClientThread.start();

            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("INICAR");
            alertDialog.setMessage("Presiona OK para iniciar el juego.");
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    comenzarJuego();
                }
            });
            alertDialog.show();

            loginPanel.setVisibility(View.GONE);

            nivel1.setVisibility(View.VISIBLE);

        }

    };

   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void iluminaBoton(int entero) {

       botones[entero].setBackground(this.getResources().getDrawable(R.drawable.btn_circle_focused));
       esperarIluminar(500, entero);

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
            case 4:
                botones[4].setBackground(this.getResources().getDrawable(R.drawable.btn_circle));
                break;
            case 5:
                botones[5].setBackground(this.getResources().getDrawable(R.drawable.btn_circle2));
                break;
            case 6:
                botones[6].setBackground(this.getResources().getDrawable(R.drawable.btn_circle3));
                break;
            case 7:
                botones[7].setBackground(this.getResources().getDrawable(R.drawable.btn_circle7));
                break;
            case 8:
                botones[8].setBackground(this.getResources().getDrawable(R.drawable.btn_circle5));
                break;
            case 9:
                botones[9].setBackground(this.getResources().getDrawable(R.drawable.btn_circle6));
                break;
            case 10:
                botones[10].setBackground(this.getResources().getDrawable(R.drawable.btn_circle));
                break;
            case 11:
                botones[11].setBackground(this.getResources().getDrawable(R.drawable.btn_circle2));
                break;
            case 12:
                botones[12].setBackground(this.getResources().getDrawable(R.drawable.btn_circle4));
                break;
            case 13:
                botones[13].setBackground(this.getResources().getDrawable(R.drawable.btn_circle3));
                break;
            case 14:
                botones[14].setBackground(this.getResources().getDrawable(R.drawable.btn_circle5));
                break;
            case 15:
                botones[15].setBackground(this.getResources().getDrawable(R.drawable.btn_circle6));
                break;
            case 16:
                botones[16].setBackground(this.getResources().getDrawable(R.drawable.btn_circle7));
                break;
            case 17:
                botones[17].setBackground(this.getResources().getDrawable(R.drawable.btn_circle8));
                break;

        }
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Error! " + e.toString() + "\n";
        }

        return ip;
    }

    public String reemplazarClave(String ip, String clave){
        Log.d(TAG, "IP: " + ip);
        String ip2;

        int j=0;
        for (int i = 0; i < ip.length(); i++){
            if(ip.substring(i, i+1).equals(".")){
                j++;
            }
            if (j==3){
                ip2 = ip.substring(0, i + 1) +  clave;
                //String num = ip.substring(i + 1);
                Log.d(TAG, "ip2" + ip2);
                return ip2;
            }
        }
        return "";
    }
    /*Metodos juego*/

    public void comenzarJuego(){
        if((secuencia.size() == 6)&&(nivel==1)){
            nivel = 2;
            secuencia.clear();
            presiona2.clear();
            nivel1.setVisibility(View.GONE);
            nivel2.setVisibility(View.VISIBLE);
            chatClientThread.sendMsg("21");
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("¡¡¡MUY BIEN!!! NIVEL 2");
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setMessage("Presiona OK para continuar el juego.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    comenzarJuego();
                }
            });
            alertDialog.show();
            return;
        }else if((secuencia.size() == 9)&&(nivel==2)){
            nivel = 3;
            secuencia.clear();
            presiona2.clear();
            nivel2.setVisibility(View.GONE);
            nivel3.setVisibility(View.VISIBLE);
            chatClientThread.sendMsg("22");
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("MUY BIEN!!! NIVEL 3");
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setMessage("Presiona OK para continuar el juego.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    comenzarJuego();
                }
            });
            alertDialog.show();
            return;
        }else if((secuencia.size() == 15)&&(nivel==3)){
            nivel = 0;
            secuencia.clear();
            presiona2.clear();
            chatClientThread.sendMsg("23");
            AlertDialog alertDialogpGanaste = new AlertDialog.Builder(getContext()).create();
            alertDialogpGanaste.setTitle("FELICITACIONES GANASTE!!!");
            alertDialogpGanaste.setIcon(R.drawable.ic_launcher);
            alertDialogpGanaste.setMessage("");
            alertDialogpGanaste.setButton("Volver", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(ClientInitActivity.this, MainActivity.class);
                    startActivity(i);

                }
            });
            alertDialogpGanaste.show();
            return;
        }else if(nivel == 1){
            Random random = new Random();

            int r = random.nextInt(4 - 0) + 0;
            chatClientThread.sendMsg(Integer.toString(r));
            secuencia.add(r);
            //Log.d(TAG, "Random" + r);
            iluminaBotones();
        }else if(nivel == 2){
            Random random = new Random();

            int r = random.nextInt((9 - 4)+1) + 4;
            chatClientThread.sendMsg(Integer.toString(r));
            secuencia.add(r);
            //Log.d(TAG, "Random" + r);
            iluminaBotones();
        }else if(nivel == 3){

            Random random = new Random();

            int r = random.nextInt((17 - 10)+1) + 10;
            chatClientThread.sendMsg(Integer.toString(r));
            secuencia.add(r);
            //Log.d(TAG, "Random" + r);
            iluminaBotones();
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
    public void esperar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                confirmar();
            }
        }, milisegundos);
    }

    public void confirmar(){

        AlertDialog alertDialogpPerdiste = new AlertDialog.Builder(getContext()).create();
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
                    chatClientThread.sendMsg("20");
                    alertDialogpPerdiste.show();
                    return;
                }
                i++;

            }
            Log.d(TAG, "Muy Bien!");
            //if((secuencia.size()!=6)&&(secuencia.size()!=9)&&(secuencia.size()!=15)){
            comenzarJuego();
            //}
            presiona2.clear();
        }else{
            Log.d(TAG, "PERDISTE!!! TE DEMORASTE MUCHO");
            chatClientThread.sendMsg("20");
            alertDialogpPerdiste.setTitle("¡¡¡PERDISTE!!!");
            alertDialogpPerdiste.setMessage("¡Te demoraste mucho!");
            alertDialogpPerdiste.show();
        }
    }

    public void perdiste(){
        desconectar();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void ganaste(){
        desconectar();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public Context getContext() {
        return this;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void actualizaBarra(){
        mProgressStatus = 100;

        int oneMin= 1000 * secuencia.size()+600;; // 1 minute in milli seconds

        /** CountDownTimer starts with 1 minutes and every onTick is 1 second */
        CountDownTimer cdt = new CountDownTimer(oneMin, 100) {

            public void onTick(long millisUntilFinished) {
                //mProgressStatus = (int) ((2000/ 60) * 100);
                mProgressStatus = mProgressStatus-10;
                if(nivel==1){
                    mProgress1.setProgress(mProgressStatus);
                }else if(nivel==2){
                    mProgress2.setProgress(mProgressStatus);
                }else if(nivel==3){
                    mProgress3.setProgress(mProgressStatus);
                }

            }

            public void onFinish() {
                // DO something when 1 minute is up
            }
        }.start();

    }

    /*Clase ChatClientThread*/

    private class ChatClientThread extends Thread {

        String name;
        String dstAddress;
        int dstPort;

        String msgToSend = "";
        boolean goOut = false;

        ChatClientThread(String name, String address, int port) {
            this.name = name;
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF(name);
                dataOutputStream.flush();

                while (!goOut) {
                    if (dataInputStream.available() > 0) {
                        msgLog = dataInputStream.readUTF();
                        Log.d(TAG, "msgLog: " + msgLog);

                        ClientInitActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //chatMsg.setText(msgLog);
                                if(msgLog.equals("perdi")){
                                    AlertDialog alertDialogGanaste = new AlertDialog.Builder(getContext()).create();
                                    alertDialogGanaste.setTitle("FELICITACIONES GANASTE!!!");
                                    alertDialogGanaste.setMessage("");
                                    alertDialogGanaste.setIcon(R.drawable.ic_launcher);
                                    alertDialogGanaste.setButton("Volver", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ganaste();

                                        }
                                    });
                                    alertDialogGanaste.show();
                                }
                            }
                        });
                    }

                    if(!msgToSend.equals("")){
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }

                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                final String eString = e.toString();
                ClientInitActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(ClientInitActivity.this, eString, Toast.LENGTH_LONG).show();
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
                final String eString = e.toString();
                ClientInitActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(ClientInitActivity.this, eString, Toast.LENGTH_LONG).show();
                    }

                });
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                ClientInitActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loginPanel.setVisibility(View.VISIBLE);
                        nivel1.setVisibility(View.GONE);
                    }

                });
            }

        }

        private void sendMsg(String msg){
            msgToSend = msg;
        }

        private void disconnect(){
            goOut = true;
        }
    }

}