package com.example.mariaalejandra.vsmemory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
public class ServerInitActivity extends ActionBarActivity {

    static final int SocketServerPORT = 8080;
    int jugadores = 0;

    TextView infoIp, info;

    String msgLog = "";
    List<ChatClient> userList;
    ServerSocket serverSocket;
    LinearLayout conectPanel, nivel1, nivel2, nivel3;

    Button botones[] = new Button[18];
    private ProgressBar mProgress1,  mProgress2,  mProgress3;
    private int mProgressStatus;
    int presionado;
    ArrayList<Integer> secuencia = new ArrayList();
    ArrayList<Integer> presiona2 = new ArrayList();
    private final String TAG = "UnJugador";
    int j=1;
    boolean loose = false;

    int nivel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_activity);
        infoIp = (TextView) findViewById(R.id.infoip);
        info = (TextView) findViewById(R.id.info);


        conectPanel = (LinearLayout)findViewById(R.id.conectpanel);
        nivel1 = (LinearLayout)findViewById(R.id.nivel1);
        nivel2 = (LinearLayout)findViewById(R.id.nivel2);
        nivel3 = (LinearLayout)findViewById(R.id.nivel3);

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
                    Log.d(TAG, "presiono" + finalI);
                    presiona2.add(presionado);
                }
            });
        }
        String ipAddress = getIpAddress();
        String numClave =numClave(ipAddress);
        infoIp.setText(numClave);

        userList = new ArrayList<ChatClient>();

        ChatServerThread chatServerThread = new ChatServerThread();
        chatServerThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ChatServerThread extends Thread {

        @Override
        public void run() {
            Socket socket = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                ServerInitActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        info.setText("Ingresa el número clave que ves debajo en el teléfono del otro jugador");
                    }
                });

                while (jugadores <= 1) {
                    socket = serverSocket.accept();
                    ChatClient client = new ChatClient();
                    userList.add(client);
                    jugadores++;
                    ConnectThread connectThread = new ConnectThread(client, socket);
                    connectThread.start();

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private class ConnectThread extends Thread {

        Socket socket;
        ChatClient connectClient;
        String msgToSend = "";

        ConnectThread(ChatClient client, Socket socket){
            connectClient = client;
            this.socket= socket;
            client.socket = socket;
            client.numJugador = jugadores;
            client.chatThread = this;
        }

        @Override
        public void run() {
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String n = dataInputStream.readUTF();

                connectClient.name = n;

                msgLog += connectClient.name + " conectado@" +
                        connectClient.socket.getInetAddress() +
                        ":" + connectClient.socket.getPort() + "\n";
                ServerInitActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        conectPanel.setVisibility(View.GONE);
                        nivel1.setVisibility(View.VISIBLE);
                    }
                });

                while (true) {
                    if (dataInputStream.available() > 0) {
                        String newMsg = dataInputStream.readUTF();
                        final int in = Integer.parseInt(newMsg);

                        msgLog += n + ": " + newMsg;
                        ServerInitActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                comenzarJuego(in);
                            }
                        });

                        broadcastMsg(newMsg);
                    }

                    if(!msgToSend.equals("")){
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
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

                userList.remove(connectClient);
                ServerInitActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(ServerInitActivity.this,
                                connectClient.name + " eliminado.", Toast.LENGTH_LONG).show();

                        msgLog += "-- " + connectClient.name + " se ha ido\n";
                        ServerInitActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                            }
                        });

                        broadcastMsg("-- " + connectClient.name + " se ha ido\n");
                    }
                });
            }

        }

        private void sendMsg(String msg){
            msgToSend = msg;
        }

    }

    private void broadcastMsg(String msg){
        for(int i=0; i<userList.size(); i++){
            userList.get(i).chatThread.sendMsg(msg);
            msgLog += "- enviar a " + userList.get(i).name + "\n";
        }

        ServerInitActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //chatMsg.setText(msgLog);
            }
        });
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
                        ip += "Direccion local: "
                                + inetAddress.getHostAddress() + "\n";
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

    class ChatClient {
        String name;
        Socket socket;
        int numJugador;
        ConnectThread chatThread;

    }

    public String numClave(String ip){
        Log.d(TAG, "IP: " + ip);

        int j=0;
        for (int i = 0; i < ip.length(); i++){
            if(ip.substring(i, i+1).equals(".")){
                j++;
            }
            if (j==3){
                String num = ip.substring(i + 1);
                Log.d(TAG, "clave" + num);
                return num;
            }
        }
       return "";
    }

    /*Métodos juego*/

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
        if(j == presiona2.size()){
            int i = 0;
            while (i < presiona2.size()) {
                Log.d(TAG, "" + i);
                if (presiona2.get(i) != secuencia.get(i)) {
                    broadcastMsg("perdi");
                    Log.d(TAG, "¡¡¡PERDISTE!!!");
                    alertDialogpPerdiste.show();
                    return;
                }
                i++;

            }
            Log.d(TAG, "¡Muy Bien!");
            j++;
            presiona2.clear();
        }else{
            broadcastMsg("perdi");
            Log.d(TAG, "secuencia: " + secuencia.size());
            Log.d(TAG, "presionados: " + presiona2.size());
            Log.d(TAG, "¡¡¡PERDISTE!!! TE DEMORASTE MUCHO");
            alertDialogpPerdiste.setTitle("¡¡¡PERDISTE!!!");
            alertDialogpPerdiste.setMessage("¡Te demoraste mucho!");
            alertDialogpPerdiste.show();
        }
    }

    public void perdiste(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void ganaste(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void comenzarJuego(int r){

        if(r == 20){
            AlertDialog alertDialogpGanaste = new AlertDialog.Builder(getContext()).create();
            alertDialogpGanaste.setTitle("¡¡¡FELICITACIONES GANASTE!!!");
            alertDialogpGanaste.setMessage("");
            alertDialogpGanaste.setIcon(R.drawable.ic_launcher);
            alertDialogpGanaste.setButton("Volver", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ganaste();

                }
            });
            alertDialogpGanaste.show();

        } else if(r==21){
            nivel = 2;
            j= 1;
            secuencia.clear();
            presiona2.clear();
            nivel1.setVisibility(View.GONE);
            nivel2.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(getContext(),"¡¡¡MUY BIEN!!! NIVEL 2", Toast.LENGTH_SHORT);
            toast.show();
        }else if(r==22){
            nivel = 3;
            j= 1;
            secuencia.clear();
            presiona2.clear();
            nivel2.setVisibility(View.GONE);
            nivel3.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(getContext(),"¡¡¡MUY BIEN!!! NIVEL 3", Toast.LENGTH_SHORT);
            toast.show();
        }else if(r==23){
            nivel = 3;
            j= 1;
            secuencia.clear();
            presiona2.clear();
            nivel2.setVisibility(View.GONE);
            nivel3.setVisibility(View.VISIBLE);
            AlertDialog alertDialogpGanaste = new AlertDialog.Builder(getContext()).create();
            alertDialogpGanaste.setTitle("FELICITACIONES GANASTE!!!");
            alertDialogpGanaste.setIcon(R.drawable.ic_launcher);
            alertDialogpGanaste.setMessage("");
            alertDialogpGanaste.setButton("Volver", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(ServerInitActivity.this, MainActivity.class);
                    startActivity(i);

                }
            });
            alertDialogpGanaste.show();
        }else {
            secuencia.add(r);
            Log.d(TAG, "r:" + r);
            iluminaBotones();
        }

    }

    public void iluminaBotones(){
        int oneMin= 1000 * secuencia.size()+600;

        CountDownTimer cdt2 = new CountDownTimer(oneMin, 1000) {
            int i = 0;
            public void onTick(long millisUntilFinished) {
                if (i < j){
                    iluminaBoton(secuencia.get(i));
                    i++;
                }
            }

            public void onFinish() {
                actualizaBarra();
                esperar(1000 * secuencia.size() + 600);
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


    public Context getContext() {
        return this;
    }


}
