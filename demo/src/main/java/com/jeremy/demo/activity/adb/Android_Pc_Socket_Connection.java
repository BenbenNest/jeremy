package com.jeremy.demo.activity.adb;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Android_Pc_Socket_Connection extends Activity {
    private static final String TAG = "Android_Pc_Socket_Con";

    ServerThread serverThread;
    TextView textView;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.getData() != null) {
                String message = msg.getData().getString("MSG", "Toast");
//                message = Thread.currentThread().getName();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (textView != null) {
                    textView.setText(message);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setText("hello");
        setContentView(textView);
        serverThread = new ServerThread();
        serverThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverThread.setIsLoop(false);
    }

    class ServerThread extends Thread {

        boolean isLoop = true;

        public void setIsLoop(boolean isLoop) {
            this.isLoop = isLoop;
        }

        @Override
        public void run() {
            Log.d(TAG, "running");

            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(9000);

                while (isLoop) {
                    Socket socket = serverSocket.accept();

                    Log.d(TAG, "accept");

                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    String msg = inputStream.readUTF();

                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("MSG", msg);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    socket.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d(TAG, "destory");

                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
